package org.example.chat_client.Model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatMessage {
    public static String formatDateTime(LocalDateTime targetDateTime) {
        // Thời điểm hiện tại
        LocalDateTime now = LocalDateTime.now();

        // Tính khoảng thời gian
        Duration duration = Duration.between(targetDateTime, now);

        // Xác định định dạng ngày và giờ dựa trên khoảng thời gian
        String formattedDateTime;
        if (duration.toDays() == 0) {
            // Cùng ngày
            formattedDateTime = targetDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else if (duration.toDays() == 1) {
            // Ngày hôm qua
            formattedDateTime = "yesterday " + targetDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else if (duration.toDays() > 1 && duration.toDays() <= 7) {
            // Trong 7 ngày qua
            formattedDateTime = targetDateTime.format(DateTimeFormatter.ofPattern("E HH:mm"));
        } else {
            // Hơn 7 ngày
            formattedDateTime = targetDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        }

        return formattedDateTime;
    }
}
