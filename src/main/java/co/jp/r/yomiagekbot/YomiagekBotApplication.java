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
import org.springframework.util.StringUtils;

@SpringBootApplication
public class YomiagekBotApplication implements CommandLineRunner {

    // DiscordBotのToken.
    private final String token;

    // デフォルトはずんだもん
    private Speaker speaker = Speaker.Zundamon_Normal;

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
            if (!containsForbiddenCharacters(message.getData().content())) {
                voiceVoxClient.send(speaker, content).ifPresent((pathName -> playerManager.loadItem(
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

        // Speakerを変える
        gateway.on(MessageCreateEvent.class).subscribe(e -> {
            final Message message = e.getMessage();
            // あとでなおす
            if (message.getAuthor().get().isBot()) return;
            if (message.getData().content().startsWith("!setspeaker=")) {
                String s = message.getData().content().replace("!setspeaker=", "");
                this.speaker = Speaker.of(Integer.parseInt(s));
                e.getMessage()
                        .getChannel().block()
                        .createMessage(speaker.getSpeaker() + ":" + speaker.getEmotions() + "に設定しました。").block();
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

    private boolean containsForbiddenCharacters(final String src) {
        if (!StringUtils.hasLength(src)) return true;
        if (src.startsWith("!")) return true;
        // URLを無視したい
        if (src.startsWith("http://") || src.startsWith("https://")) return true;
        return false;
    }
}
