package com.bu.s3.controller;


import com.bu.s3.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class S3Controller {
    @Autowired
    private S3Service service;

    @PostMapping("/upload")
    public String upload(@RequestParam(value = "file") MultipartFile multipartFile){
        try {
            return service.uploadFileS3(multipartFile) ;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return "";
    }

    @GetMapping("/getFile/{fileName}")
    public ResponseEntity<ByteArrayResource> getFile(@PathVariable String fileName){
        byte[] data = service.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity.ok()
                .contentLength(data.length)
                .header("Content-type","application/octet-stream")
                .header("Content-disposition","attachment; filename=\""+fileName+"\"")
                .body(resource);
    }

    @DeleteMapping("/delete/{fileName}")
    public String deleteFile(@PathVariable String fileName){
        return service.deleteFile(fileName);
    }
}
