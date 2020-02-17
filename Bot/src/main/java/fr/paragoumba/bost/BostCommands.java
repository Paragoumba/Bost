package fr.paragoumba.bost;

import fr.paragoumba.bost.api.Command;
import fr.paragoumba.bost.api.CommandInfo;
import fr.paragoumba.bost.api.Plugin;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

import static fr.paragoumba.bost.CommandManager.registerCommand;

public class BostCommands extends Plugin {

    @Override
    public void onEnable(){

        final JDA jda = Bot.getJda();

        registerCommand("exit", new Command(){

            @Override
            public boolean execute(String command, String[] args, Member sender, MessageChannel channel){

                MessageEmbed message = new EmbedBuilder()
                        .setTitle(":white_check_mark: Shutting down")
                        .setColor(Color.SUCCESS)
                        .build();

                channel.sendMessage(message).queue();

                jda.shutdown();

                System.exit(0);

                return true;

            }
        });
    }
}
