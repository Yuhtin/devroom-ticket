package com.yuhtin.devroom.command;

import com.yuhtin.devroom.util.TicketUtils;
import lombok.val;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;

public class CloseTicketCommand extends ListenerAdapter {

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (!event.getName().equals("close")) return;

        val channel = event.getTextChannel();
        val author = event.getMember();
        if (!author.hasPermission(Permission.MESSAGE_MANAGE)) {
            event.reply(":x: No permission!").setEphemeral(true).queue();
            return;
        }

        if (!TicketUtils.channelIsTicket(channel)) {
            event.reply(":x: This channel isn't a ticket.").setEphemeral(true).queue();
            return;
        }

        val member = channel.getGuild().getMemberById(channel.getTopic().split(" ")[3]);
        if (member != null) {
            channel.getManager().removePermissionOverride(member).queue();
            channel.getManager().putPermissionOverride(member,
                            Arrays.asList(
                                    Permission.MESSAGE_READ,
                                    Permission.MESSAGE_HISTORY
                            ),
                            Collections.singletonList(Permission.MESSAGE_WRITE))
                    .queue();
        }

        channel.delete().queue();
    }
}
