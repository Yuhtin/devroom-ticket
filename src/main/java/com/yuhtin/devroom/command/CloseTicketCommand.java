package com.yuhtin.devroom.command;

import com.yuhtin.devroom.util.Logger;
import com.yuhtin.devroom.util.TicketUtils;
import lombok.val;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

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

        channel.delete().queue();
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        val component = event.getComponent();
        if (event.getGuild() == null
                || component == null
                || component.getId() == null
                || !event.getButton().getId().equals("close")) return;

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

        channel.delete().queue();
    }
}
