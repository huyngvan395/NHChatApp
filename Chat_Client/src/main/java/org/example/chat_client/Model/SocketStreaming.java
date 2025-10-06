package org.example.chat_client.Model;

import com.github.sarxos.webcam.Webcam;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class SocketStreaming {
    private int VIDEO_PORT = 5000;
    private int AUDIO_PORT = 6000;
    private Webcam webcam;
    private volatile boolean running = false;
    private DatagramSocket videoReceiveSocket;
    private DatagramSocket audioReceiveSocket;

    // Thêm fields để lưu threads (cho interrupt)
    private Thread localPreviewThread;
    private Thread sendVideoThread;
    private Thread sendAudioThread;
    private Thread receiveVideoThread;
    private Thread receiveAudioThread;

    // Getter/setter để controller có thể set threads (hoặc truyền vào start methods)
    public void setThreads(Thread localPreview, Thread sendVideo, Thread sendAudio, Thread recvVideo, Thread recvAudio) {
        this.localPreviewThread = localPreview;
        this.sendVideoThread = sendVideo;
        this.sendAudioThread = sendAudio;
        this.receiveVideoThread = recvVideo;
        this.receiveAudioThread = recvAudio;
    }

    public void videoCallCaptureLoop(ImageView localView) {
        if (checkWebcam()) return;

        while (webcam.isOpen() && running) {
            BufferedImage raw = webcam.getImage();
            if (raw == null) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();  // Restore interrupt status
                    break;
                }
                continue;
            }
            BufferedImage previewImg = resize(raw, 160, 120);
            Platform.runLater(() -> localView.setImage(SwingFXUtils.toFXImage(previewImg, null)));
        }
    }

    public void sendVideoCallLoop(String REMOTE_IP) {
        if (checkWebcam()) return;

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress address = InetAddress.getByName(REMOTE_IP);
            while (running && webcam.isOpen()) {
                BufferedImage raw = webcam.getImage();
                if (raw == null) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    continue;
                }

                BufferedImage sendImg = resize(raw, 320, 240);

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(sendImg, "jpg", out);
                out.flush();
                byte[] data = out.toByteArray();
                out.close();
                if (data.length > 60000) {
                    int max = 60000;
                    int parts = (data.length + max - 1) / max;
                    int offset = 0;
                    for (int p = 0; p < parts; p++) {
                        int len = Math.min(max, data.length - offset);
                        byte[] chunk = new byte[len + 4];
                        chunk[0] = (byte) ((p >> 24) & 0xFF);
                        chunk[1] = (byte) ((p >> 16) & 0xFF);
                        chunk[2] = (byte) ((p >> 8) & 0xFF);
                        chunk[3] = (byte) (p & 0xFF);
                        System.arraycopy(data, offset, chunk, 4, len);
                        DatagramPacket packet = new DatagramPacket(chunk, chunk.length, address, VIDEO_PORT);
                        socket.send(packet);
                        offset += len;
                    }
                } else {
                    DatagramPacket packet = new DatagramPacket(data, data.length, address, VIDEO_PORT);
                    socket.send(packet);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi send video: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean checkWebcam() {
        if (webcam != null && webcam.isOpen()) {
            return false;
        }
        webcam = Webcam.getDefault();
        if (webcam == null) {
            System.err.println("No webcam available");
            return true;
        }
        webcam.setViewSize(new Dimension(320, 240));
        webcam.open(true);
        return false;
    }

    public void receiveVideoCallLoop(ImageView remoteView) {
        videoReceiveSocket = null;  // Reset
        try {
            videoReceiveSocket = new DatagramSocket(VIDEO_PORT);
            videoReceiveSocket.setReuseAddress(true);  // Giúp reuse port nhanh hơn
            videoReceiveSocket.setSoTimeout(100);  // Timeout 100ms để kiểm tra running
            byte[] buffer = new byte[65536];
            while (running) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try {
                    videoReceiveSocket.receive(packet);

                    byte[] imgBytes = new byte[packet.getLength()];
                    System.arraycopy(packet.getData(), packet.getOffset(), imgBytes, 0, packet.getLength());
                    ByteArrayInputStream bais = new ByteArrayInputStream(imgBytes);
                    BufferedImage img = ImageIO.read(bais);
                    if (img != null) {
                        Platform.runLater(() -> remoteView.setImage(SwingFXUtils.toFXImage(img, null)));
                    }
                } catch (SocketTimeoutException e) {
                    // Timeout: kiểm tra running và tiếp tục
                    if (!running) break;
                    continue;
                } catch (Exception ex) {
                    if (running) {
                        System.err.println("Lỗi nhận video packet: " + ex.getMessage());
                    }
                    break;
                }
            }
        } catch (SocketException se) {
            if (running) {
                System.err.println("Lỗi bind video socket (port " + VIDEO_PORT + " có thể đang dùng): " + se.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Lỗi receive video: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanupVideoSocket();
        }
    }

    public void sendAudioCallLoop(String REMOTE_IP) {
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress addr = InetAddress.getByName(REMOTE_IP);
            AudioFormat format = new AudioFormat(16000.0f, 16, 1, true, true);
            TargetDataLine mic = AudioSystem.getTargetDataLine(format);
            if (mic == null) {
                System.err.println("Không có microphone hỗ trợ format: " + format);
                return;
            }
            mic.open(format);
            mic.start();

            byte[] buffer = new byte[1024];
            while (running) {
                int read = mic.read(buffer, 0, buffer.length);
                if (read > 0) {
                    DatagramPacket packet = new DatagramPacket(buffer, read, addr, AUDIO_PORT);
                    socket.send(packet);
                }
                // Kiểm tra interrupt định kỳ (không sleep để tránh delay audio)
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
            }
            mic.drain();
            mic.stop();
            mic.close();
        } catch (Exception e) {
            System.err.println("Lỗi send audio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void receiveAudioCallLoop() {
        SourceDataLine speakers = null;
        audioReceiveSocket = null;  // Reset
        try {
            audioReceiveSocket = new DatagramSocket(AUDIO_PORT);
            audioReceiveSocket.setReuseAddress(true);  // Giúp reuse port
            audioReceiveSocket.setSoTimeout(100);  // Timeout 100ms để thoát nhanh
            AudioFormat format = new AudioFormat(16000.0f, 16, 1, true, true);
            speakers = AudioSystem.getSourceDataLine(format);
            if (speakers == null) {
                throw new Exception("Không có loa hỗ trợ format: " + format);
            }
            speakers.open(format);
            speakers.start();

            System.out.println("Bắt đầu nhận audio trên port " + AUDIO_PORT);

            byte[] buffer = new byte[1024];
            while (running) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try {
                    audioReceiveSocket.receive(packet);
                    int length = packet.getLength();
                    if (length > 0) {
                        speakers.write(packet.getData(), 0, length);
                    }
                } catch (SocketTimeoutException e) {
                    // Timeout: kiểm tra running
                    if (!running) break;
                    continue;
                } catch (Exception e) {
                    if (running) {
                        System.err.println("Lỗi nhận audio packet: " + e.getMessage());
                    }
                    break;
                }
            }
            if (speakers != null) {
                speakers.drain();
            }
        } catch (SocketException se) {
            if (running) {
                System.err.println("Lỗi bind audio socket (port " + AUDIO_PORT + " có thể đang dùng): " + se.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Lỗi receive audio: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (speakers != null && speakers.isOpen()) {
                try {
                    speakers.drain();
                    speakers.stop();
                    speakers.close();
                } catch (Exception ignored) {}
            }
            cleanupAudioSocket();
            System.out.println("Dừng nhận audio.");
        }
    }

    // Cleanup riêng cho từng socket
    private void cleanupVideoSocket() {
        if (videoReceiveSocket != null && !videoReceiveSocket.isClosed()) {
            try {
                videoReceiveSocket.close();
            } catch (Exception ignored) {}
        }
        videoReceiveSocket = null;
    }

    private void cleanupAudioSocket() {
        if (audioReceiveSocket != null && !audioReceiveSocket.isClosed()) {
            try {
                audioReceiveSocket.close();
            } catch (Exception ignored) {}
        }
        audioReceiveSocket = null;
    }

    private static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return dimg;
    }

    public void start() {
        running = true;
        System.out.println("SocketStreaming started.");
    }

    public void stop() {
        running = false;
        System.out.println("Đang dừng SocketStreaming...");

        // Interrupt tất cả threads để break blocking calls
        if (localPreviewThread != null) localPreviewThread.interrupt();
        if (sendVideoThread != null) sendVideoThread.interrupt();
        if (sendAudioThread != null) sendAudioThread.interrupt();
        if (receiveVideoThread != null) receiveVideoThread.interrupt();
        if (receiveAudioThread != null) receiveAudioThread.interrupt();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {}

        // Cleanup resources
        if (webcam != null) {
            try {
                if (webcam.isOpen()) {
                    webcam.close();
                }
                webcam = null;
            } catch (Exception ignored) {}
        }
        cleanupVideoSocket();
        cleanupAudioSocket();

        localPreviewThread = null;
        sendVideoThread = null;
        sendAudioThread = null;
        receiveVideoThread = null;
        receiveAudioThread = null;

        System.out.println("SocketStreaming stopped.");
    }
}
