package fr.paragoumba.bost.music;

import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import fr.paragoumba.bost.api.Plugin;
import fr.paragoumba.bost.music.commands.PlayCommand;
import fr.paragoumba.bost.music.commands.QueueCommand;
import fr.paragoumba.bost.music.commands.SkipCommand;

import static fr.paragoumba.bost.CommandManager.registerCommand;

public class Music extends Plugin {

    private static Music instance;

    private QueuedAudioPlayerManager playerManager;
    private QueuedAudioPlayer player;

    @Override
    public void onEnable(){

        instance = this;

        playerManager = new QueuedAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(playerManager);

        player = (QueuedAudioPlayer) playerManager.createPlayer();

        registerCommand("play", new PlayCommand());
        registerCommand("skip", new SkipCommand());
        registerCommand("queue", new QueueCommand());

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
