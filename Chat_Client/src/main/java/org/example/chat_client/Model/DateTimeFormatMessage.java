package org.example.chat_client.Model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatMessage {
    public static String formatDateTime(LocalDateTime targetDateTime) {

        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(targetDateTime, now);
        String formattedDateTime;
        if (duration.toDays() == 0) {
            formattedDateTime = targetDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else if (duration.toDays() == 1) {
            formattedDateTime = "yesterday " + targetDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else if (duration.toDays() > 1 && duration.toDays() <= 7) {
            formattedDateTime = targetDateTime.format(DateTimeFormatter.ofPattern("E HH:mm"));
        } else {
            formattedDateTime = targetDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        }

        return formattedDateTime;
    }
}
