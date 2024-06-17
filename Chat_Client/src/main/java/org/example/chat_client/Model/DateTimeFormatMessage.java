package org.example.chat_client.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimeFormatMessage {
    public static String formatDateTime(LocalDateTime targetDateTime) {

        LocalDateTime now = LocalDateTime.now();

        long daysBetween = ChronoUnit.DAYS.between(targetDateTime.toLocalDate(), now.toLocalDate());
        String formattedDateTime;
        if (daysBetween == 0) {
            formattedDateTime = targetDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else if (daysBetween == 1) {
            formattedDateTime = "Yesterday " + targetDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else if (daysBetween > 1 && daysBetween <= 7) {
            formattedDateTime = targetDateTime.format(DateTimeFormatter.ofPattern("E HH:mm"));
        } else {
            formattedDateTime = targetDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        }

        return formattedDateTime;
    }
}
