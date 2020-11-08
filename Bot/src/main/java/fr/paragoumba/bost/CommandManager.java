package fr.paragoumba.bost;

import fr.paragoumba.bost.api.Command;
import fr.paragoumba.bost.api.CommandInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommandManager {

    private static final Logger logger = Bot.getLogger();
    private static final HashMap<String, Command> commands = new HashMap<>();
    public static final String prefix = ";";

    public static void registerCommand(String command, Command commandHandler){

        registerCommand(command, commandHandler, "");

    }

    public static void registerCommand(String command, Command commandHandler, String... usage){

        if (commands.containsKey(command)){

            logger.warn("Command '" + command + "' already exists.");
            return;

        }

        for (int i = 0; i < usage.length; ++i){

            usage[i] = usage[i].replaceAll("%p", prefix).replaceAll("%c", command);

        }

        commandHandler.setInfo(new CommandInfo(command, usage));
        commands.put(command, commandHandler);

    }

    public static void interpretMessageAsCommand(Message message){

        String messageContent = message.getContentRaw();

        if (messageContent.startsWith(prefix)){

            List<String> argsList = new ArrayList<>();
            Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(messageContent);

            while (m.find()){

                argsList.add(m.group(1).replace("\"", ""));

            }

            String[] args = argsList.toArray(new String[0]);

            args[0] = args[0].replaceFirst(prefix, "");

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
                        .setDescription(Arrays.stream(commandHandler.getInfo().getUsage()).map(s -> '\t' + s + '\n').collect(Collectors.joining()))
                        .setColor(EmbedColor.ERROR)
                        .build();

                channel.sendMessage(message).queue();

            }

        } catch (Exception e){

            logger.error("Error while trying to execute command " + command
                    + " with following parameters: [\"" + String.join("\", \"", args) + "\"].");
            e.printStackTrace();

        }
    }

    public static List<Command> getCommands(){

        return new ArrayList<>(commands.values());

    }
}
