package com.yuhtin.devroom.command;

import com.yuhtin.devroom.TicketBot;
import com.yuhtin.devroom.configuration.YamlConfiguration;
import com.yuhtin.devroom.core.Startup;
import com.yuhtin.devroom.util.EventWaiter;
import com.yuhtin.devroom.util.Logger;
import lombok.val;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class TicketCommand extends ListenerAdapter {

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent command) {
        if (!command.getName().equals("ticket")) return;

        TicketBot bot = Startup.getBot();
        YamlConfiguration config = bot.getConfig();
        long ticketCategoryID = (long) config.getObject("ticketCategoryID");

        Category category = command.getGuild().getCategoryById(ticketCategoryID);
        TextChannel ticketChannel = findChannelByTopic(category, textChannel ->
                textChannel.getTopic() != null && textChannel.getTopic().contains(command.getUser().getId())
        );

        if (ticketChannel != null) {
            val hasTicketMessage = "🏷 "
                    + command.getUser().getAsMention()
                    + " You already have an open ticket. <#"
                    + ticketChannel.getId()
                    + ">";

            command.reply(hasTicketMessage).setEphemeral(true).queue();
            return;
        }

        List<Component> components = new ArrayList<>();
        for (String entries : config.getStringList("ticketTypes")) {
            components.add(Button.success(entries.toLowerCase(), entries));
        }

        command.reply("🛎️ Select the ticket type below")
                .addActionRow(components)
                .setEphemeral(true)
                .queue();

        AtomicReference<String> entry = new AtomicReference<>();
        EventWaiter eventWaiter = bot.getEventWaiter();
        eventWaiter.waitForEvent(ButtonClickEvent.class, buttonClick -> {
            val component = buttonClick.getComponent();
            if (buttonClick.getGuild() == null
                    || component == null
                    || component.getId() == null
                    || buttonClick.getButton().getId().equals("close")) return false;

            for (String entries : config.getStringList("ticketTypes")) {
                if (entries.equalsIgnoreCase(buttonClick.getButton().getId())) {
                    entry.set(entries);
                    break;
                }
            }

            return entry.get() != null;
        }, buttonClick -> buttonClick.reply("🚨 Why are you opening this ticket?").setEphemeral(true).queue($ -> {
            long supportRoleID = (long) config.getObject("supportRoleID");
            eventWaiter.waitForEvent(
                    GuildMessageReceivedEvent.class,
                    event -> event.getAuthor().getIdLong() == command.getUser().getIdLong(),
                    event -> category.createTextChannel("ticket-" + command.getUser().getName())
                            .setTopic("Ticket: " + command.getUser().getId())
                            .addPermissionOverride(
                                    event.getGuild().getPublicRole(),
                                    Collections.emptyList(),
                                    Arrays.asList(
                                            Permission.MESSAGE_READ,
                                            Permission.MESSAGE_WRITE
                                    )
                            )
                            .addPermissionOverride(
                                    event.getMember(),
                                    Arrays.asList(
                                            Permission.MESSAGE_READ,
                                            Permission.MESSAGE_WRITE,
                                            Permission.MESSAGE_EMBED_LINKS,
                                            Permission.MESSAGE_ATTACH_FILES
                                    ),
                                    Collections.emptyList()
                            )
                            .addPermissionOverride(
                                    event.getGuild().getRoleById(supportRoleID),
                                    Arrays.asList(
                                            Permission.MESSAGE_READ,
                                            Permission.MESSAGE_WRITE,
                                            Permission.MESSAGE_EMBED_LINKS,
                                            Permission.MESSAGE_ATTACH_FILES
                                    ),
                                    Collections.emptyList()
                            )
                            .queue(textChannel -> {
                                val embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("📫 | " + entry.get());
                                embedBuilder.setDescription("Open reason: " + event.getMessage().getContentRaw());
                                embedBuilder.setThumbnail(textChannel.getGuild().getIconUrl());

                                textChannel.sendMessageEmbeds(embedBuilder.build())
                                        .mentionRoles(supportRoleID).setActionRow(
                                        Button.secondary("close", "❌ Close")
                                ).queue();

                                val sucessMessage = "🎉 "
                                        + command.getUser().getAsMention()
                                        + " You have successfully opened a ticket "
                                        + textChannel.getAsMention();

                                event.getMessage().delete().queue();
                                event.getChannel().sendMessage(sucessMessage).queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
                            }));
        }));
    }

    /**
     * Search channel by filter
     *
     * @param category to search channel
     * @param filter   specifications that the channel should have
     * @return found channel
     */
    private TextChannel findChannelByTopic(Category category, Predicate<TextChannel> filter) {
        return category.getTextChannels()
                .stream()
                .filter(filter)
                .findAny()
                .orElse(null);
    }
}
