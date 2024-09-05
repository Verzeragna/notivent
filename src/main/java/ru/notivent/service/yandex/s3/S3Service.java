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

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

  private static final String IMAGES_BUCKET = "geopoint-images";

  public List<String> saveImages(Map<String, String> images, UUID userId, ClientS3 clientS3) {
    var urls = new ArrayList<String>();
    var client = clientS3.getClient();
    int i = 1;
    for (Map.Entry<String, String> image : images.entrySet()) {
      var key = userId + "/" + i + image.getValue();
      var createObjectRequest = PutObjectRequest.builder().bucket(IMAGES_BUCKET).key(key).build();
      var bytes = Base64.getDecoder().decode(image.getKey().getBytes());
      clientS3.getClient().putObject(createObjectRequest, RequestBody.fromBytes(bytes));
      var url = getObjectUrl(IMAGES_BUCKET, key, clientS3);
      urls.add(url.toString());
      i++;
    }
    return urls;
  }

  private URL getObjectUrl(String bucket, String key, ClientS3 clientS3) {
    var urlRequest = GetUrlRequest.builder().bucket(bucket).key(key).build();
    return clientS3.getClient().utilities().getUrl(urlRequest);
  }
}
