package me.minkyoung.buddy_back.service;

import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.model.CreatePreauthenticatedRequestDetails;
import com.oracle.bmc.objectstorage.model.PreauthenticatedRequest;
import com.oracle.bmc.objectstorage.requests.CreatePreauthenticatedRequestRequest;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
//@Profile("Prod") 되돌리기
public class OciPostImageService {

    private final ObjectStorageClient objectStorageClient;

    @Value("${oci.namespace}") private String namespace;
    @Value("${oci.bucket}") private String bucket;
    @Value("${oci.endpoint}") private String endpoint; // 예: https://objectstorage.ap-chuncheon-1.oraclecloud.com
    @Value("${image.par-expire-minutes:10}") private int parExpireMinutes;

    public String upload(Long postId, MultipartFile file) throws Exception{
        String ext = guessExtension(file.getOriginalFilename(), file.getContentType());
        String objectKey = "posts/" + postId + "/" + UUID.randomUUID() + ext;

        try (InputStream is = file.getInputStream()) {
            objectStorageClient.putObject(
                    PutObjectRequest.builder()
                            .namespaceName(namespace)
                            .bucketName(bucket)
                            .objectName(objectKey)
                            .contentType(file.getContentType())
                            .contentLength(file.getSize())
                            .putObjectBody(is)
                            .build()
            );
        }
        return objectKey;
    }

    public String createReadParUrl(String objectKey) {
        Date expires = Date.from(OffsetDateTime.now()
                .plusMinutes(parExpireMinutes)
                .toInstant());

        var details = CreatePreauthenticatedRequestDetails.builder()
                .name("read-" + UUID.randomUUID())
                .accessType(CreatePreauthenticatedRequestDetails.AccessType.ObjectRead)
                .objectName(objectKey)
                .timeExpires(expires)
                .build();

        PreauthenticatedRequest par = objectStorageClient.createPreauthenticatedRequest(
                CreatePreauthenticatedRequestRequest.builder()
                        .namespaceName(namespace)
                        .bucketName(bucket)
                        .createPreauthenticatedRequestDetails(details)
                        .build()
        ).getPreauthenticatedRequest();

        return endpoint + par.getAccessUri();
    }

    private String guessExtension(String originalName, String contentType) {
        if (originalName != null && originalName.contains(".")) {
            return originalName.substring(originalName.lastIndexOf(".")).toLowerCase();
        }
        if ("image/png".equals(contentType)) return ".png";
        if ("image/webp".equals(contentType)) return ".webp";
        return ".jpg";
    }
}
