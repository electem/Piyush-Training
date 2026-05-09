package com.cloudinary.Cloudinary_Proj.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Cloudinary_Proj.entity.CloudinaryImage;
import com.cloudinary.Cloudinary_Proj.repository.CloudinaryRepository;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private CloudinaryRepository repository;

    public String uploadFile(MultipartFile file) throws IOException {

        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.emptyMap()
        );

        String imageUrl = uploadResult.get("url").toString();

        CloudinaryImage image = new CloudinaryImage();
        image.setUrl(imageUrl);

        repository.save(image);

        return imageUrl;
    }
}
