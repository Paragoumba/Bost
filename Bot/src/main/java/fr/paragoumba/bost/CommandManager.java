package fr.paragoumba.bost;

import fr.paragoumba.bost.api.Command;
import fr.paragoumba.bost.api.CommandInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.util.*;
import java.util.logging.Logger;

public class CommandManager {

    private static final Logger logger = Bot.getLogger();
    private static final HashMap<String, Command> commands = new HashMap<>();
    public static final String prefix = ";";

    public static void registerCommand(String command, Command commandHandler){

        registerCommand(command, commandHandler, "");

    }

    public static void registerCommand(String command, Command commandHandler, String usage){

        if (commands.containsKey(command)){

            Logger.getGlobal().warning("Command '" + command + "' already exists.");
            return;

        }

        usage = usage.replaceAll("%p", prefix).replaceAll("%c", command);

        commandHandler.setInfo(new CommandInfo(command, usage));
        commands.put(command, commandHandler);

    }

    public static void interpretMessageAsCommand(Message message){

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

            return;

        }

        try {

            if (!commandHandler.execute(command, args, sender, channel)){

                MessageEmbed message = new EmbedBuilder()
                        .setTitle(":x: Wrong arguments")
                        .setDescription(commandHandler.getInfo().getUsage())
                        .setColor(EmbedColor.ERROR)
                        .build();

                channel.sendMessage(message).queue();

            }

        } catch (Exception e){

            logger.severe("Error while trying to execute command " + command
                    + " with following parameters: [\"" + String.join("\", \"", args) + "\"].");
            e.printStackTrace();

        }
    }

    public static List<Command> getCommands(){

        return new ArrayList<>(commands.values());

    }
}
