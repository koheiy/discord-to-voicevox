package co.jp.r.yomiagekbot;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class TrackScheduler implements AudioLoadResultHandler {
    private final AudioPlayer audioPlayer;

    public TrackScheduler(final AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    @Override
    public void trackLoaded(final AudioTrack track) {
        audioPlayer.playTrack(track);
    }

    @Override
    public void playlistLoaded(final AudioPlaylist playlist) {}

    @Override
    public void noMatches() {}

    @Override
    public void loadFailed(final FriendlyException exception) {}
}
