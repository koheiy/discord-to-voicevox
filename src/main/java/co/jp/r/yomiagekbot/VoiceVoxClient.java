package co.jp.r.yomiagekbot;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class VoiceVoxClient {

    private final String voiceVoxUrl;

    private final String wavDownloadDirectory;

    private final RestTemplate restTemplate;

    public VoiceVoxClient(
            @Value("${voicevox.base-url}") final String voiceVoxUrl,
            @Value("${voicevox.download-directory}") final String wavDownloadDirectory,
            final RestTemplate restTemplate) {
        this.voiceVoxUrl =  voiceVoxUrl;
        this.wavDownloadDirectory = wavDownloadDirectory;
        this.restTemplate = restTemplate;
    }

    public Optional<String> send(final Speaker speaker, final String text)  {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(
                restTemplate.exchange(voiceVoxUrl + "/audio_query?speaker=" + speaker.getSpeakerId() + "&text=" + text, HttpMethod.POST, null, String.class).getBody(),
                headers
        );

        try {
            String pathName = wavDownloadDirectory + LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYYMMDDHHmmss")) + ".wav";

            // FIXME:これ微妙かも
            IOUtils.write(
                    restTemplate.exchange(voiceVoxUrl + "/synthesis?speaker=" + speaker.getSpeakerId(), HttpMethod.POST, requestEntity, byte[].class).getBody(),
                    new FileOutputStream(pathName)
            );

            return Optional.ofNullable(pathName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();

    }
}
