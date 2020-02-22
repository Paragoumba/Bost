package fr.paragoumba.bost;

import fr.paragoumba.bost.api.Command;
import fr.paragoumba.bost.api.CommandInfo;
import fr.paragoumba.bost.api.Plugin;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

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
                        .setColor(EmbedColor.SUCCESS)
                        .build();

                channel.sendMessage(message).queue();

                jda.shutdown();

                System.exit(0);

                return true;

            }
        }, "\t`%p%c` - Stops the bot.");

        registerCommand("help", new Command(){

            @Override
            public boolean execute(String command, String[] args, Member sender, MessageChannel channel){

                StringBuilder builder = new StringBuilder();

                for (Command c : CommandManager.getCommands()){

                    CommandInfo info = c.getInfo();

                    builder.append(info.getCommand()).append(":\n")
                            .append(info.getUsage()).append("\n\n");

                }

                MessageEmbed message = new EmbedBuilder()
                        .setTitle(":scroll: Usages")
                        .setDescription(builder.toString())
                        .setColor(EmbedColor.INFO)
                        .build();

                channel.sendMessage(message).queue();

                return true;

            }
        }, "\t`%p%c` - Displays the usages of the different commands.");
    }
}
