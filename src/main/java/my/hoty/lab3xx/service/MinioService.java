package my.hoty.lab3xx.service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class MinioService {
    private final MinioClient minioClient;
    @Value("${minio.bucket}")
    private String bucket;

    public MinioService(
            @Value("${minio.url}") String url,
            @Value("${minio.accessKey}") String accessKey,
            @Value("${minio.secretKey}") String secretKey
    ) {
        this.minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }

    public void uploadFile(String fileName, InputStream inputStream, long size, String contentType) throws Exception {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(fileName)
                        .stream(inputStream, size, -1)
                        .contentType(contentType)
                        .build()
        );
    }

    public String getFileUrl(String fileName) throws Exception {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucket)
                        .object(fileName)
                        .expiry(3600)
                        .build()
        );
    }

}
