package fr.paragoumba.bost.music;

import fr.paragoumba.bost.api.Plugin;
import fr.paragoumba.bost.music.commands.PlayCommand;
import fr.paragoumba.bost.music.commands.QueueCommand;
import fr.paragoumba.bost.music.commands.SkipCommand;
import fr.paragoumba.bost.music.commands.StopCommand;

import static fr.paragoumba.bost.CommandManager.registerCommand;

public class Music extends Plugin {

    private static Music instance;

    private QueuedAudioPlayerManager playerManager;
    private QueuedAudioPlayer player;

    @Override
    public void onEnable(){

        instance = this;

        playerManager = new QueuedAudioPlayerManager();

        player = (QueuedAudioPlayer) playerManager.createPlayer();

        registerCommand("play", new PlayCommand(),
                "\t`%p%c` - Pauses the player. If it is already paused, resumes the queue.\n" +
                        "\t`%p%c ytsearch: args` - Play the first track corresponding to `args` on Youtube.\n" +
                        "\t`%p%c id` - Play the video with the corresponding id.");
        registerCommand("skip", new SkipCommand(),
                "\t`%p%c` - Skip the currently playing track.");
        registerCommand("queue", new QueueCommand(),
                "\t`%p%c` - List the queued tracks.");
        registerCommand("stop", new StopCommand(),
                "\t`%p%c` - Stop the player and clear the queue.");

    }

    public static Music getInstance(){

        return instance;

    }

    public QueuedAudioPlayer getPlayer(){

        return player;

    }

    public QueuedAudioPlayerManager getPlayerManager(){

        return playerManager;

    }
}
