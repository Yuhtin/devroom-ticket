package com.yuhtin.devroom.util;

import net.dv8tion.jda.api.entities.TextChannel;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public class TicketUtils {

    public static boolean channelIsTicket(TextChannel channel) {
        return channel.getTopic() != null && channel.getTopic().startsWith("Ticket: ");
    }

}