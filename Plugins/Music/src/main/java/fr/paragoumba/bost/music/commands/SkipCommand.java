package fr.paragoumba.bost.music.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.paragoumba.bost.music.Music;
import fr.paragoumba.bost.music.QueuedAudioPlayer;
import fr.paragoumba.bost.api.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class SkipCommand extends Command {

    @Override
    public boolean execute(String command, String[] args, Member sender, MessageChannel channel){

        Integer trackNumber = null;

        if (args.length > 0){

            try {

                trackNumber = Integer.parseInt(args[0]);

            } catch (NumberFormatException ignored){}
        }

        Music plugin = Music.getInstance();
        QueuedAudioPlayer player = plugin.getPlayer();

        AudioTrack skippedTrack = trackNumber == null ?
                player.skipTrack() :
                player.removeTrack(trackNumber);

        MessageEmbed message = new EmbedBuilder()
                .setTitle(":next_track: Skipped track")
                .setDescription("Skipped " + skippedTrack.getInfo().title)
                .build();

        channel.sendMessage(message).queue();

        return true;

    }
}
