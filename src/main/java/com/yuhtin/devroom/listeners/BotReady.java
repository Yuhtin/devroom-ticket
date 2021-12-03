package com.yuhtin.devroom.listeners;

import com.yuhtin.devroom.util.Logger;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class BotReady extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Logger.getLogger().info("Bot is online!");
    }
}
