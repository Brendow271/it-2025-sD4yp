package com.harmony.service;

import com.harmony.entity.UserPhoto;
import com.harmony.repository.UserPhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserPhotoService {
    
    @Autowired
    private UserPhotoRepository userPhotoRepository;
    
    @Value("${upload.images.path}")
    private String uploadPath;
    
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
        "image/jpeg",
        "image/jpg",
        "image/png"
    );
    
    //10MB = 10 * 1024 * 1024 = 10485760
    @Value("${MAX_FILE_SIZE:10485760}")
    private long MAX_FILE_SIZE;

    public UserPhoto uploadPhoto(Long userId, MultipartFile file) throws IOException {
        validateFile(file);
        
        String filename = generateUniqueFilename(file.getOriginalFilename());
        Path userDir = Paths.get(uploadPath, userId.toString());
        
        if (!Files.exists(userDir)) {
            Files.createDirectories(userDir);
        }
        
        Path filePath = userDir.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        String imageUrl = String.format("/uploads/images/%d/%s", userId, filename);
        
        UserPhoto userPhoto = new UserPhoto();
        userPhoto.setUserId(userId);
        userPhoto.setImageUrl(imageUrl);
        
        return userPhotoRepository.save(userPhoto);
    }
    
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Файл не может быть пустым");
        }
        
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Размер файла превышает максимально допустимый (10MB)");
        }
        
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Недопустимый тип файла. Разрешены: JPEG, PNG, WEBP, GIF");
        }
    }
    
    private String generateUniqueFilename(String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }
    
    public List<UserPhoto> getUserPhotos(Long userId) {
        return userPhotoRepository.findByUserId(userId);
    }
    
    public void deletePhoto(Long imageId, Long userId) throws IOException {
        UserPhoto photo = userPhotoRepository.findById(imageId)
            .orElseThrow(() -> new IllegalArgumentException("Фото не найдено"));
        
        if (!photo.getUserId().equals(userId)) {
            throw new SecurityException("Нет доступа к удалению этого фото");
        }
        
        String imageUrl = photo.getImageUrl();
        String filePath = imageUrl.replace("/uploads/images/", uploadPath + "/");
        Path path = Paths.get(filePath);
        
        if (Files.exists(path)) {
            Files.delete(path);
        }
        
        userPhotoRepository.delete(photo);
    }
}
