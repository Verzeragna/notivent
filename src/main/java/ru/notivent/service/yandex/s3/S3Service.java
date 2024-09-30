package ru.notivent.service.yandex.s3;

import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

  private static final String PROFILE_IMAGE_NAME = "profile_image.jpg";

  @Value("${yandex.s3.image-bucket}")
  String imageBucket;

  @Value("${yandex.s3.profile-image-bucket}")
  String profileImageBucket;

  public List<String> saveImages(
      Map<String, String> images, UUID userId, UUID geoPointId, ClientS3 clientS3) {
    var urls = new ArrayList<String>();
    int i = 1;
    for (Map.Entry<String, String> image : images.entrySet()) {
      var key = userId + "/" + geoPointId + "/" + i + image.getValue();
      var createObjectRequest = PutObjectRequest.builder().bucket(imageBucket).key(key).build();
      var bytes = Base64.getDecoder().decode(image.getKey().getBytes());
      clientS3.getClient().putObject(createObjectRequest, RequestBody.fromBytes(bytes));
      var url = getObjectUrl(imageBucket, key, clientS3);
      urls.add(url.toString());
      i++;
    }
    return urls;
  }

  private URL getObjectUrl(String bucket, String key, ClientS3 clientS3) {
    var urlRequest = GetUrlRequest.builder().bucket(bucket).key(key).build();
    return clientS3.getClient().utilities().getUrl(urlRequest);
  }

  public String saveProfileImage(String image, UUID userId, ClientS3 clientS3) {
    var key = userId + "/" + PROFILE_IMAGE_NAME;
    var deleteObjectRequest = DeleteObjectRequest.builder().bucket(profileImageBucket).key(key).build();
    clientS3.getClient().deleteObject(deleteObjectRequest);
    var createObjectRequest = PutObjectRequest.builder().bucket(profileImageBucket).key(key).build();
    var bytes = Base64.getDecoder().decode(image.getBytes());
    clientS3.getClient().putObject(createObjectRequest, RequestBody.fromBytes(bytes));
    return getObjectUrl(profileImageBucket, key, clientS3).toString();
  }
}
