package fr.paragoumba.bost;

import fr.paragoumba.bost.api.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;

public class CommandManager {

    private static final Logger logger = Bot.getLogger();
    private static final HashMap<String, Command> commands = new HashMap<>();
    public static final String prefix = ";";

    public static void registerCommand(String command, Command commandHandler){

        if (commands.containsKey(command)){

            Logger.getGlobal().warning("Command '" + command + "' already exists.");
            return;

        }

        commands.put(command, commandHandler);

    }

    static void interpretMessageAsCommand(Message message){

        String messageContent = message.getContentRaw();

        if (messageContent.startsWith(prefix)){

            String[] args = messageContent.replaceFirst(prefix, "").split(" ");

            executeCommand(
                    args[0],
                    args.length < 2 ? new String[]{} : Arrays.copyOfRange(args, 1, args.length),
                    message.getMember(),
                    message.getChannel());

        }
    }

    private static void executeCommand(String command, String[] args, Member sender, MessageChannel channel){

        logger.info("Command " + command);

        Command commandHandler = commands.get(command);

        if (commandHandler == null){

            if (true) return;

            MessageEmbed message = new EmbedBuilder()
                    .setTitle(":x: Error")
                    .setDescription("Command " + prefix + command + " not found.")
                    .setColor(Color.RED)
                    .build();

            channel.sendMessage(message).queue();

            return;

        }

        try {

            if (!commandHandler.execute(command, args, sender, channel)){

                MessageEmbed message = new EmbedBuilder()
                        .setTitle(":x: Wrong arguments")
                        .setDescription("Usage:")
                        .setColor(Color.RED)
                        .build();

                channel.sendMessage(message).queue();

            }

        } catch (Exception e){

            logger.severe("Fatal error exiting.");
            e.printStackTrace();
            e.printStackTrace();

        }
    }
}
