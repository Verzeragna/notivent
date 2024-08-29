package ru.notivent.service.yandex.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Component
public class ClientS3 {

    private static final String YANDEX_HOST = "https://storage.yandexcloud.net";

    private software.amazon.awssdk.services.s3.S3Client client;

    @Value("${yandex.s3.accessKeyId}")
    String accessKeyId;

    @Value("${yandex.s3.secretAccessKey}")
    String secretAccessKey;

    public ClientS3() {
    }

    public ClientS3(S3Client client) {
        this.client = client;
    }

    public S3Client getClient() {
        if (client != null) {
            return client;
        }
        var credentials =
                AwsBasicCredentials.builder()
                        .accessKeyId(accessKeyId)
                        .secretAccessKey(secretAccessKey)
                        .build();
        client = S3Client.builder()
                        .credentialsProvider(StaticCredentialsProvider.create(credentials))
                        .region(Region.US_EAST_1)
                        .endpointOverride(URI.create(YANDEX_HOST))
                        .build();
        return client;
    }
}
