package ru.notivent.service.yandex.s3;

import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Setter
@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

  private static final String IMAGES_BUCKET = "geopoint-images";

  private ClientS3 s3Client = new ClientS3();

  public List<String> saveImages(Map<String, String> images, UUID userId) {
    var urls = new ArrayList<String>();
    int i = 1;
    for (Map.Entry<String, String> image : images.entrySet()) {
      var key = userId + "/" + i + image.getValue();
      var createObjectRequest = PutObjectRequest.builder().bucket(IMAGES_BUCKET).key(key).build();
      var bytes = Base64.getDecoder().decode(image.getKey().getBytes());
      s3Client.getClient().putObject(createObjectRequest, RequestBody.fromBytes(bytes));
      var url = getObjectUrl(IMAGES_BUCKET, key);
      urls.add(url.toString());
      i++;
    }
    return urls;
  }

  private URL getObjectUrl(String bucket, String key) {
    var urlRequest = GetUrlRequest.builder().bucket(bucket).key(key).build();
    return s3Client.getClient().utilities().getUrl(urlRequest);
  }
}
