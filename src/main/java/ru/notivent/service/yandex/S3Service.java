package ru.notivent.service.yandex;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@Slf4j
public class S3Service {

  private static final String YANDEX_HOST = "https://storage.yandexcloud.net";
  private static final String IMAGES_BUCKET = "geopoint-images";

  @Value("${yandex.s3.accessKeyId}")
  String accessKeyId;

  @Value("${yandex.s3.secretAccessKey}")
  String secretAccessKey;

  private S3Client s3Client;

  private void initS3Client() {
    if (s3Client == null) {
      var credentials =
          AwsBasicCredentials.builder()
              .accessKeyId(accessKeyId)
              .secretAccessKey(secretAccessKey)
              .build();
      s3Client =
          S3Client.builder()
              .credentialsProvider(StaticCredentialsProvider.create(credentials))
              .region(Region.US_EAST_1)
              .endpointOverride(URI.create(YANDEX_HOST))
              .build();
    }
  }

  public List<String> saveImages(Map<String, String> images, UUID userId) {
    var urls = new ArrayList<String>();
    initS3Client();
    int i = 1;
    for (Map.Entry<String, String> image : images.entrySet()) {
      var key = userId + "/" + i + image.getValue();
      var createObjectRequest =
          PutObjectRequest.builder()
              .bucket(IMAGES_BUCKET)
              .key(key)
              .build();
      var bytes = Base64.getDecoder().decode(image.getKey().getBytes());
      s3Client.putObject(createObjectRequest, RequestBody.fromBytes(bytes));
      var url = getObjectUrl(IMAGES_BUCKET, key);
      urls.add(url.toString());
      i++;
    }
    return urls;
  }

  private URL getObjectUrl(String bucket, String key) {
    var urlRequest = GetUrlRequest.builder()
            .bucket(bucket)
            .key(key)
            .build();
    return s3Client.utilities().getUrl(urlRequest);
  }
}
