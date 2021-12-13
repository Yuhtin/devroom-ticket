package com.yuhtin.devroom;

import com.yuhtin.devroom.command.CloseTicketCommand;
import com.yuhtin.devroom.command.TicketCommand;
import com.yuhtin.devroom.configuration.YamlConfiguration;
import com.yuhtin.devroom.core.Bot;
import com.yuhtin.devroom.listeners.BotReady;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import javax.security.auth.login.LoginException;
import java.io.File;

public class TicketBot extends Bot {

    @Override
    public void onLoad() {
        File file = new File("configuration.yml");
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    YamlConfiguration.createDefault().write(file);
                    getLogger().info("Created the configuration file!");
                    getLogger().info("Configure the token and start the bot again!");

                    System.exit(0);
                }
            } catch (Exception exception) {
                getLogger().info("Error when creating the configuration file.");
                System.exit(0);
            }
        }

        setConfig(YamlConfiguration.of(file));
        getConfig().buildMenu();
    }

    @Override
    public void onEnable() {
        try {
            JDA jda = JDABuilder.createDefault(getConfig().getString("token"))
                    .enableIntents(GatewayIntent.GUILD_MESSAGES)
                    .addEventListeners(getEventWaiter(), new BotReady(), new TicketCommand(), new CloseTicketCommand())
                    .build();

            CommandListUpdateAction commands = jda.updateCommands();
            commands.addCommands(new CommandData("ticket", "Open a new ticket"));
            commands.addCommands(new CommandData("close", "(MODERATION) Close ticket"));

            commands.queue();

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
