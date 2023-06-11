package co.jp.r.yomiagekbot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.voice.AudioProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class YomiagekBotApplication implements CommandLineRunner {

    // DiscordBotのToken.
    private final String token;

    private AudioPlayerManager playerManager;
    private AudioPlayer player;
    private AudioProvider provider;
    private TrackScheduler scheduler;

    private final DiscordClient client;
    private final GatewayDiscordClient gateway;

    private VoiceVoxClient voiceVoxClient;

    public YomiagekBotApplication(
            final VoiceVoxClient voiceVoxClient,
            @Value("${discord.token}") final String token) {
        this.voiceVoxClient = voiceVoxClient;
        this.token = token;
        this.client = DiscordClient.create(token);
        this.gateway = client.login().block();
    }

    public static void main(String[] args) {
        SpringApplication.run(YomiagekBotApplication.class, args);
    }

    @Override
    public void run(String... args) {

        init();

        gateway.on(MessageCreateEvent.class).subscribe(e -> {
            final Message message = e.getMessage();
            if (message.getAuthor().get().isBot()) return;
            String content = message.getContent();
            if (!"!dis".equals(message.getData().content()) && !"!join".equals(message.getData().content())) {
                voiceVoxClient.send(content).ifPresent((pathName -> playerManager.loadItem(
                        pathName,
                        scheduler
                )));
            }
        });

        // Disconnect
        gateway.on(MessageCreateEvent.class).subscribe(e -> {
            final Message message = e.getMessage();
            if (message.getAuthor().get().isBot()) return;
            if ("!dis".equals(message.getData().content())) {
                final Member member = e.getMember().orElse(null);
                if (member != null) {
                    final VoiceState voiceState = member.getVoiceState().block();
                    if (voiceState != null) {
                        final VoiceChannel channel = voiceState.getChannel().block();
                        if (channel != null) {
                            channel.sendDisconnectVoiceState().block();
                        }
                    }
                }
            }
        });

        // join event
        gateway.on(MessageCreateEvent.class).subscribe(e -> {
            final Message message = e.getMessage();
            // あとでなおす
            if (message.getAuthor().get().isBot()) return;
            if ("!join".equals(message.getData().content())) {
                final Member member = e.getMember().orElse(null);
                if (member != null) {
                    final VoiceState voiceState = member.getVoiceState().block();
                    if (voiceState != null) {
                        final VoiceChannel channel = voiceState.getChannel().block();
                        if (channel != null) {
                            channel.join(spec -> {
                                spec.setProvider(provider);
                            }).block();
                        }
                    }
                }
            }
        });

        gateway.onDisconnect().block();
    }

    private void init() {
        // audio player系の初期化
        playerManager = new DefaultAudioPlayerManager();
        playerManager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        AudioSourceManagers.registerLocalSource(playerManager);
        player = playerManager.createPlayer();
        provider = new LavaPlayerAudioProvider(player);
        scheduler = new TrackScheduler(player);
    }
}
