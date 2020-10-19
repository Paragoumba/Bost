package fr.paragoumba.bost;

import fr.paragoumba.bost.events.MessageReceivedEventListener;
import fr.paragoumba.bost.events.ShutdownEventListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.logging.Logger;

public class Bot {

    private static JDA jda;
    private static final Logger logger = Logger.getGlobal();

    public static void main(String[] args){

        try {

            Configuration config = new Configuration();

            jda = JDABuilder.create(config.getString("token"), GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_VOICE_STATES)
                    .setChunkingFilter(ChunkingFilter.NONE)
                    .setMemberCachePolicy(MemberCachePolicy.NONE)
                    .disableCache(CacheFlag.ACTIVITY, CacheFlag.EMOTE, CacheFlag.CLIENT_STATUS)
                    .addEventListeners(new MessageReceivedEventListener())
                    .addEventListeners(new ShutdownEventListener())
                    .setActivity(Activity.listening("4'33\" by John Cage"))
                    .build();

            jda.awaitReady();

            PluginManager.loadPlugins();
            PluginManager.enablePlugins();

        } catch (LoginException | IOException e){

            logger.severe(e.getLocalizedMessage());
            System.exit(1);

        } catch (Exception e){

            logger.severe("Fatal error exiting.");
            logger.severe(e.getLocalizedMessage());
            e.printStackTrace();
            System.exit(1);

        }
    }

    public static JDA getJda(){

        return jda;

    }

    public static Logger getLogger(){

        return logger;

    }
}
