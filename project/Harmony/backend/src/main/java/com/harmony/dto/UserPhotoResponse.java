package com.harmony.dto;

import com.harmony.entity.UserPhoto;
import java.time.LocalDateTime;

public class UserPhotoResponse {
    private Long imageId;
    private String imageUrl;
    private LocalDateTime createdAt;

    public UserPhotoResponse() {}

    public UserPhotoResponse(UserPhoto userPhoto) {
        this.imageId = userPhoto.getImageId();
        this.imageUrl = userPhoto.getImageUrl();
        this.createdAt = userPhoto.getCreatedAt();
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
