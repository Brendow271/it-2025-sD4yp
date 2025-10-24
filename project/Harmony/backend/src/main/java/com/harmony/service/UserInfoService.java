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

    public UserInfo updateInfo(Integer age, String[] genres, String[] instruments, String location, String about){
        UserInfo user = new UserInfo(age, genres, instruments, location, about);
        return userInfoRepository.save(user);
    }
}
