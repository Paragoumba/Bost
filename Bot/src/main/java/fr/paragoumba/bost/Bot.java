package fr.paragoumba.bost;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.util.logging.Logger;

public class Bot {

    private static JDA jda;
    private static Logger logger = Logger.getGlobal();
    private static Configuration config = new Configuration();

    public static void main(String[] args){

        try {

            jda = new JDABuilder(config.getToken())
                    .addEventListeners(new MessageReceivedEventListener())
                    .setActivity(Activity.listening("4'33\" by John Cage"))
                    .build();

            jda.awaitReady();

            PluginManager.loadPlugins();
            PluginManager.enablePlugins();

        } catch (LoginException e){

            logger.severe(e.getLocalizedMessage());
            System.exit(1);

        } catch (Exception e){

            logger.severe("Fatal error exiting.");
            e.printStackTrace();
            System.exit(1);

        }
    }

    public static JDA getJda(){

        return jda;

    }

    public static Configuration getConfig(){

        return config;

    }

    public static Logger getLogger(){

        return logger;

    }
}
