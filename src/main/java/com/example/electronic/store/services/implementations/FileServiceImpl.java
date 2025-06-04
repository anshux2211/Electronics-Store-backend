package com.example.electronic.store.services.implementations;

import com.example.electronic.store.exception.BadApiRequestException;
import com.example.electronic.store.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{


    private Logger logger= LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
        String originalFileName=file.getOriginalFilename();
        logger.info("FileName: {}",originalFileName);

        String fileName= UUID.randomUUID().toString();
        String extension=originalFileName.substring(originalFileName.lastIndexOf('.'));
        String FileNameWithExtension=fileName+extension;
        String fullPath=path+ File.separator+FileNameWithExtension;

        if(extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")){
            File folder=new File(path);
            if(!folder.exists()){
                folder.mkdirs();
            }

            Files.copy(file.getInputStream(), Paths.get(fullPath));
            return FileNameWithExtension;
        }
        else{
            throw new BadApiRequestException("Image with above extension is not valid");
        }
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath=path+File.separator+name;
        InputStream inputStream=new FileInputStream(fullPath);
        return inputStream;
    }


}
