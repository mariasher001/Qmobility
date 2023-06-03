package com.mariasher.qmobilitybusiness.Utils;

import java.time.Duration;
import java.time.LocalDateTime;

public class DateTimeUtils {
    public static String convertDateAndTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }

        StringBuilder resultDateTime = new StringBuilder();
        resultDateTime.append("")
                .append(dateTime.getDayOfMonth())
                .append("-")
                .append(dateTime.getMonthValue())
                .append("-")
                .append(dateTime.getYear());

        resultDateTime.append("T")
                .append(dateTime.getHour())
                .append("-")
                .append(dateTime.getMinute())
                .append("-")
                .append(dateTime.getSecond())
                .append("-")
                .append(dateTime.getNano());
        return resultDateTime.toString();
    }

    public static LocalDateTime convertStringToLocalDateTime(String dateTime) {
        if (dateTime.isEmpty()) {
            return null;
        }

        String date = dateTime.split("T")[0];
        String time = dateTime.split("T")[1];

        String[] splitDate = date.split("-");
        String[] splitTime = time.split("-");

        int dayOfMonth = Integer.parseInt(splitDate[0]);
        int month = Integer.parseInt(splitDate[1]);
        int year = Integer.parseInt(splitDate[2]);

        int hour = Integer.parseInt(splitTime[0]);
        int minutes = Integer.parseInt(splitTime[1]);
        int seconds = Integer.parseInt(splitTime[2]);
        int nano = Integer.parseInt(splitTime[3]);

        return LocalDateTime.of(year, month, dayOfMonth, hour, minutes, seconds, nano);
    }

    public static String getDateAndTimeAsStringForViews(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }

        StringBuilder resultDateTime = new StringBuilder();
        resultDateTime.append("")
                .append(dateTime.getDayOfMonth())
                .append(".")
                .append(dateTime.getMonthValue())
                .append(".")
                .append(dateTime.getYear());

        int hour = dateTime.getHour();
        String amPm = "am";
        if (hour > 12) {
            hour = hour - 12;
            amPm = "pm";
        }
        int min = dateTime.getMinute();
        String minute = "" + min;
        if (min < 10) {
            minute = "0" + min;
        }

        resultDateTime.append(" ")
                .append(hour)
                .append(":")
                .append(minute)
                .append(" ")
                .append(amPm);
        return resultDateTime.toString();
    }

    public static double calculateTimeDifference(LocalDateTime startTime, LocalDateTime endTime) {
        Duration duration = Duration.between(startTime, endTime);
        double seconds = duration.getSeconds();
        double nanoseconds = duration.getNano() / 1_000_000_000.0;
        return seconds + nanoseconds;
    }

    public static double calculateTimeDifference(String startTime, String endTime) {
        LocalDateTime startTime1 = DateTimeUtils.convertStringToLocalDateTime(startTime);
        LocalDateTime endTime1 = DateTimeUtils.convertStringToLocalDateTime(endTime);
        Duration duration = Duration.between(startTime1, endTime1);
        double seconds = duration.getSeconds();
        double nanoseconds = duration.getNano() / 1_000_000_000.0;
        return seconds + nanoseconds;
    }

    public static String formatTimeSecondsToMinutesAndHours(double seconds) {
        int hours = (int) seconds / 3600;
        int minutes = (int) (seconds % 3600) / 60;
        int secs = (int) seconds % 60;
        return String.format("%02dh %02dm %02ds", hours, minutes, secs);
    }

}
