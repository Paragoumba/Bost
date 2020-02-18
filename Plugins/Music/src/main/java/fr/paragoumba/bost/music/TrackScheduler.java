package fr.paragoumba.bost.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.LinkedList;

public class TrackScheduler extends AudioEventAdapter {

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason){

        super.onTrackEnd(player, track, endReason);

        if (player instanceof QueuedAudioPlayer && endReason == AudioTrackEndReason.FINISHED){

            QueuedAudioPlayer queuedAudioPlayer = (QueuedAudioPlayer) player;
            LinkedList<AudioTrack> queuedTracks = queuedAudioPlayer.getQueuedTracks();

            if (!queuedTracks.isEmpty() && queuedAudioPlayer.getQueuedTracks().getFirst() == track){

                queuedAudioPlayer.skipTrack();

            }
        }
    }
}
