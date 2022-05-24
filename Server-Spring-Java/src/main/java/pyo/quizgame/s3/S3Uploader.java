package pyo.quizgame.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pyo.quizgame.domain.S3FileInfo;
import pyo.quizgame.service.S3Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;
    private final S3Service s3Service;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.bucket.url}")
    private String s3Url;

    //전환
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));
        return upload(uploadFile, dirName);
    }


    //랜덤 UUID로 파일명 변경
    private String upload(File uploadFile, String dirName) {
        String name = uploadFile.getName();
        String[] exp = uploadFile.getName().split(".");
        String uuid = UUID.randomUUID().toString();
        String transFileName = dirName + "/" + uuid;
        /*log.info("==============="+randomString+"===============");*/
        String uploadImageUrl = putS3(uploadFile, transFileName);
        S3FileInfo s3FileInfo = new S3FileInfo();
        s3FileInfo.setFileOriginalName(uploadFile.getName());
        s3FileInfo.setFileName(uuid);
        s3FileInfo.setFilePath(s3Url+transFileName);
        s3Service.save(s3FileInfo);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }


    //실질적인 s3에 올리는 코드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    //멀티파트 파일 파일로변환
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }
}