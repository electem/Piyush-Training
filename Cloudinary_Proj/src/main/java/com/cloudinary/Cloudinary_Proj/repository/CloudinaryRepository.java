package com.cloudinary.Cloudinary_Proj.repository;

import com.cloudinary.Cloudinary_Proj.entity.CloudinaryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CloudinaryRepository extends JpaRepository<CloudinaryImage, Integer> {
}
