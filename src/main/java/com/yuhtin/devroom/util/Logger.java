package com.yuhtin.devroom.util;

import java.time.LocalDateTime;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public class Logger {

    private static final Logger INSTANCE = new Logger();

    public static Logger getLogger() {
        return INSTANCE;
    }

    public void log(LogType logType, String message, Exception exception) {
        log(message, logType);
        exception.printStackTrace();
    }

    public void info(String message) {
        log(message, LogType.INFO);
    }

    public void warning(String message) {
        log(message, LogType.WARNING);
    }

    public void severe(String message) {
        log(message, LogType.SEVERE);
    }

    public void log(Object message, LogType logType) {
        if (message == null) message = "Generated a null content";

        StackTraceElement[] stackTrace = getStackTrace();
        String className = stackTrace[stackTrace.length > 3 ? 3 : 2].getFileName().replace(".java", "");

        LocalDateTime now = LocalDateTime.now();

        String hour = String.valueOf(now.getHour());
        String minute = String.valueOf(now.getMinute());
        String second = String.valueOf(now.getSecond());

        // fix time
        if (hour.length() == 1) hour = 0 + hour;
        if (minute.length() == 1) minute = 0 + minute;
        if (second.length() == 1) second = 0 + second;

        String time = "[" + hour + ":" + minute + ":" + second + "] ";

        message = time + logType.toString() + "> " + "[" + className + "] " + message;

        System.out.println(message);
    }

    public void log(String... message) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length; i++) {
            if (i + 1 == message.length) builder.append(message[i]);
            else builder.append(message[i]).append("\n");
        }

        log(builder.toString(), LogType.INFO);
    }

    public StackTraceElement[] getStackTrace() {
        Throwable throwable = new Throwable();
        throwable.fillInStackTrace();

        return throwable.getStackTrace();
    }

    public enum LogType {
        INFO,
        WARNING,
        SEVERE,
    }
}
