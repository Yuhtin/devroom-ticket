package com.yuhtin.devroom;

import com.yuhtin.devroom.configuration.YamlConfiguration;
import com.yuhtin.devroom.core.Bot;
import com.yuhtin.devroom.listeners.BotReady;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.io.*;

public class TicketBot extends Bot {

    @Override
    public void onLoad() {
        File file = new File("configuration.yml");
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    YamlConfiguration.createDefault().write(file);
                    getLogger().info("Created the configuration file");
                }
            } catch (Exception exception) {
                getLogger().info("Error when creating the configuration file.");
                System.exit(0);
            }
        }

        setConfig(YamlConfiguration.of(file));
    }

    @Override
    public void onEnable() {
        try {
            JDA jda = JDABuilder.createDefault(getConfig().getString("token"))
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .setChunkingFilter(ChunkingFilter.ALL)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .addEventListeners(new BotReady())
                    .build();

            jda.upsertCommand("ticket", "Open a new ticket").queue();
            jda.upsertCommand("close", "(MODERATION) Close ticket").queue();

            getLogger().info("JDA connection pending");
        } catch (LoginException exception) {
            getLogger().severe("Error when connect to bot, authToken is incorrect");
            getLogger().severe("System shutdown");

            System.exit(0);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Bye!");
    }

}
