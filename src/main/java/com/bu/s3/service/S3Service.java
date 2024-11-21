package com.bu.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@Service
public class S3Service {
    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 amazonS3;

    public String uploadFileS3(MultipartFile multipartFile) throws FileNotFoundException {
        File file = convertMultipartFileToFIle(multipartFile);
        amazonS3.putObject(bucketName,multipartFile.getOriginalFilename(),file);
        file.delete();
        return "File uploaded :"+ multipartFile.getOriginalFilename();
    }

    private File convertMultipartFileToFIle(MultipartFile multipartFile) throws FileNotFoundException {
        File file = new File(multipartFile.getOriginalFilename());
        try(FileOutputStream fileOutputStream = new FileOutputStream(file)){
            fileOutputStream.write(multipartFile.getBytes());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return file;
    }

    public byte[] downloadFile(String fileName){
        S3Object s3Object = amazonS3.getObject(bucketName,fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public String deleteFile(String fileName){
        amazonS3.deleteObject(bucketName,fileName);
        return fileName+" removed...............";
    }
}
