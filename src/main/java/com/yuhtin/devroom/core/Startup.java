package com.yuhtin.devroom.core;

import com.yuhtin.devroom.TicketBot;
import lombok.Getter;

import java.util.Scanner;

public class Startup {

    @Getter private static final TicketBot bot = new TicketBot();

    public static void main(String[] args) {
        try {
            bot.onLoad();

            bot.onEnable();
            bot.getLogger().info("Bot started successfully.");
        } catch (Exception exception) {
            exception.printStackTrace();
            bot.onDisable();

            return;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(bot::onDisable));

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext() && scanner.nextLine().equalsIgnoreCase("stop")) { // shit code to support minecraft hosts rofl
            new Thread(bot::onDisable).start();
        }
    }

}
