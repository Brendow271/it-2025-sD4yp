package com.harmony.service;

import com.harmony.dto.SwipeResponse;
import com.harmony.dto.UserCardResponse;
import com.harmony.entity.Swipe;
import com.harmony.entity.UserAuth;
import com.harmony.entity.UserInfo;
import com.harmony.repository.SwipeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SwipeService {

    @Autowired
    private SwipeRepository swipeRepository;

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private UserPhotoService userPhotoService;

    public SwipeResponse createSwipe(Long userId1, Long userId2, Boolean decision){
        if (userId1.equals(userId2)){
            throw new RuntimeException("Нельзя свапнуть самого себя");
        }

        Optional<Swipe> existingSwipe = swipeRepository.findSwipeBetweenUsers(userId1,userId2);

        Swipe swipe;
        if (existingSwipe.isPresent()){
            swipe = existingSwipe.get();
            if (swipe.getUserId1().equals(userId1)){
                swipe.setDecision1(decision);
            } else {
                swipe.setDecision2(decision);
            }
        } else {
            swipe = new Swipe();
            swipe.setUserId1(userId1);
            swipe.setUserId2(userId2);
            swipe.setDecision1(decision);
            swipe.setDecision2(null);
        }

        Swipe savedSwipe = swipeRepository.save(swipe);

        recommendationService.onSwipe(userId1);

        return new SwipeResponse(savedSwipe);
    }

    public List<SwipeResponse> getMatches(Long userId){
        List<Swipe> matches = swipeRepository.findMatchesForUser(userId);
        return matches.stream()
                .map(SwipeResponse::new)
                .collect(Collectors.toList());
    }

    public List<UserCardResponse> getMatchCards(Long userId) {
        return swipeRepository.findMatchesForUser(userId).stream()
                .map(swipe -> buildUserCard(getOtherUserId(swipe, userId)))
                .collect(Collectors.toList());
    }

    public List<UserCardResponse> getIncomingLikeCards(Long userId) {
        return swipeRepository.findIncomingLikesForUser(userId).stream()
                .map(swipe -> buildUserCard(getOtherUserId(swipe, userId)))
                .collect(Collectors.toList());
    }

    private Long getOtherUserId(Swipe swipe, Long currentUserId) {
        return swipe.getUserId1().equals(currentUserId) ? swipe.getUserId2() : swipe.getUserId1();
    }

    private UserCardResponse buildUserCard(Long userId) {
        UserAuth auth = userAuthService.getUserById(userId);
        UserInfo info = userInfoService.getUserInfo(userId);

        UserCardResponse card = new UserCardResponse();
        card.setUserId(userId);
        card.setName(auth.getName());
        card.setAge(info.getAge());
        card.setGenres(info.getGenres());
        card.setInstruments(info.getInstrument());
        card.setLocation(info.getLocation());
        card.setAbout(info.getAbout());
        card.setPhotoUrl(userPhotoService.getLatestPhotoUrl(userId));
        return card;
    }

    public List<SwipeResponse> getSwipeHistory(Long userId){
        List<Swipe> swipes = swipeRepository.findAllByUserId(userId);
        return swipes.stream()
                .map(SwipeResponse::new)
                .collect(Collectors.toList());
    }

    public boolean hasSwiped(Long userId1, Long userId2){
        Optional<Swipe> swipe = swipeRepository.findSwipeBetweenUsers(userId1, userId2);
        if (swipe.isEmpty()){
            return false;
        }
        Swipe s = swipe.get();
        if (s.getUserId1().equals(userId1)){
            return s.getDecision1() != null;
        } else {
            return s.getDecision2() != null;
        }

    }
}
