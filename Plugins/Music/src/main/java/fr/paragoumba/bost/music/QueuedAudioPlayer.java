package fr.paragoumba.bost.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.LinkedList;

public class QueuedAudioPlayer extends DefaultAudioPlayer {

    public QueuedAudioPlayer(DefaultAudioPlayerManager manager){

        super(manager);

        queuedTracks = new LinkedList<>();

        setPaused(true);

        addListener(new AudioEventAdapter(){

            @Override
            public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason){

                super.onTrackEnd(player, track, endReason);

                System.out.println("onTrackEnd: " + track.getInfo().title + " (" + endReason.toString() + ")");

                if (!queuedTracks.isEmpty()){

                    queuedTracks.removeFirst().stop();

                    if (!queuedTracks.isEmpty()){

                        playTrack(queuedTracks.getFirst());

                    }

                } else {

                    setPaused(true);

                }
            }
        });
    }

    private LinkedList<AudioTrack> queuedTracks;

    public void queueTrack(AudioTrack track){

        queuedTracks.add(track);

        if (isPaused()){

            setPaused(false);
            playTrack(queuedTracks.getFirst());
            System.out.println("Playing track: " + queuedTracks.getFirst().getInfo().title);

        }
    }

    @Override
    public void setPaused(boolean value){

        super.setPaused(value);

        if (!queuedTracks.isEmpty()){
            if (value){

                queuedTracks.getFirst().stop();

            } else {

                playTrack(queuedTracks.getFirst());

            }
        }
    }

    public void skipTrack(){

        System.out.println("skipTrack");

        System.out.println("Queue size: " + queuedTracks.size());

        if (!queuedTracks.isEmpty()){

            System.out.println("skipTrack: not empty");
            queuedTracks.removeFirst().stop();

            if (!queuedTracks.isEmpty()){

                System.out.println("skipTrack: not empty2");
                playTrack(queuedTracks.getFirst());

            }
        }
    }

    public LinkedList<AudioTrack> getQueuedTracks(){

        return (LinkedList<AudioTrack>) queuedTracks.clone();

    }
}
