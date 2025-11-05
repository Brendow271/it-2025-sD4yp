package com.harmony.service;

import com.harmony.entity.UserInfo;
import com.harmony.repository.UserInfoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserInfoService {
    @Autowired
    private UserInfoRepository userInfoRepository;

    public UserInfo createDefaultUserInfo(Long userId) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setAge(18);
        userInfo.setGenres(new String[]{});
        userInfo.setInstrument(new String[]{});
        userInfo.setLocation("");
        userInfo.setAbout("");
        
        return userInfoRepository.save(userInfo);
    }

    public UserInfo updateInfo(Long userId, Integer age, String[] genres, String[] instruments, String location, String about){
        try {
            UserInfo user = userInfoRepository.findById(userId).orElseThrow(() -> 
                new RuntimeException("Пользователь не найден"));

            if (age != null) user.setAge(age);
            if (genres != null) user.setGenres(genres);
            if (instruments != null) user.setInstrument(instruments);
            if (location != null) user.setLocation(location);
            if (about != null) user.setAbout(about);
            
            return userInfoRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обновлении информации: " + e.getMessage());
        }
    }
}
