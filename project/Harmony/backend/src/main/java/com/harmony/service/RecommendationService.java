package com.harmony.service;

import com.harmony.entity.Swipe;
import com.harmony.entity.UserInfo;
import com.harmony.repository.SwipeRepository;
import com.harmony.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private SwipeRepository swipeRepository;

    @Autowired
    private UserInfoService userInfoService;

    private static final int QUEUE_SIZE = 20;
    private static final int MIN_QUEUE_SIZE = 5;

    private final Map<Long, Queue<Long>> recommendationQueues = new ConcurrentHashMap<>();

    public Long getNextRecommendation(Long userId){
        Queue<Long> queue = recommendationQueues.get(userId);

        if (queue == null || queue.isEmpty()){
            refillQueue(userId);
            queue = recommendationQueues.get(userId);
        }

        if (queue != null && !queue.isEmpty()){
            Long recommendedUserId = queue.poll();

            if (queue.size() < MIN_QUEUE_SIZE){
                refillQueueAsync(userId);
            }

            return recommendedUserId;
        }

        return null;
    }

    @Async
    public void refillQueueAsync(Long userId){
        refillQueue(userId);
    }

    public void refillQueue(Long userId){
        UserInfo currentUser = userInfoRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!userInfoService.isProfileComplete(userId)){
            return;
        }

        Set<Long> swipedUserIds = getSwipedUserIds(userId);

        swipedUserIds.add(userId);

        List<UserInfo> candidates = userInfoRepository.findAll().stream()
                .filter(user -> !swipedUserIds.contains(user.getUserId()))
                .filter(user -> userInfoService.isProfileComplete(user.getUserId()))
                .collect(Collectors.toList());

        List<ScoredUser> scoredUsers = candidates.stream()
                .map(candidate -> new ScoredUser(
                        candidate.getUserId(),
                        calculateScore(currentUser, candidate)
                ))
                .sorted((a, b) -> Double.compare(b.score, a.score))
                .collect(Collectors.toList());

        Queue<Long> queue = recommendationQueues.getOrDefault(userId, new LinkedList<>());
        
        for (ScoredUser scoredUser : scoredUsers) {
            if (queue.size() >= QUEUE_SIZE) break;
            queue.offer(scoredUser.userId);
        }
        
        recommendationQueues.put(userId, queue);
    }

    private Set<Long> getSwipedUserIds(Long userId) {
        List<Swipe> swipes = swipeRepository.findAllByUserId(userId);
        return swipes.stream()
                .map(swipe -> {
                    if (swipe.getUserId1().equals(userId)){
                        return swipe.getUserId2();
                    } else {
                        return swipe.getUserId1();
                    }
                })
                .collect(Collectors.toSet());
    }

    private double calculateScore(UserInfo currentUser, UserInfo candidate){
        double genreScore = calculateGenreScore(currentUser.getGenres(), candidate.getGenres());
        double instrumentScore = calculateInstumentScore(currentUser.getInstrument(), candidate.getInstrument());
        double locationScore = calculateLocationScore(currentUser.getLocation(), candidate.getLocation());
        double ageScore = calculateAgeScore(currentUser.getAge(), candidate.getAge());

        return genreScore * 0.4 + instrumentScore * 0.3 + locationScore * 0.2 + ageScore * 0.1;
    }

    private double calculateGenreScore(String[] userGenres, String[] candidateGenres){
        if (userGenres == null || candidateGenres == null || userGenres.length == 0 || candidateGenres.length == 0){
            return 0.0;
        }

        Set<String> userSet = new HashSet<>(Arrays.asList(userGenres));
        Set<String> candidateSet = new HashSet<>(Arrays.asList(candidateGenres));

        long matches = userSet.stream().filter(candidateSet :: contains).count();
        if (matches == 0) return 0.0;

        return (double) matches / Math.max(userSet.size(), candidateSet.size());
    }

    private double calculateInstumentScore(String[] userIntruments, String[] candidateIntruments){
        if (userIntruments == null || candidateIntruments == null || userIntruments.length == 0 || candidateIntruments.length == 0){
            return 0.0;
        }

        Set<String> userSet = new HashSet<>(Arrays.asList(userIntruments));
        Set<String> candidateSet = new HashSet<>(Arrays.asList(candidateIntruments));

        long matches = userSet.stream().filter(candidateSet :: contains).count();
        if (matches == 0) return 0.0;

        return (double) matches / Math.max(userSet.size(), candidateSet.size());
    }
    //TODO: Add PostGIS distance calculation
    private double calculateLocationScore(String userLocation, String candidateLocation){
        if (userLocation == null || candidateLocation == null){
            return 0.0;
        }

        if (userLocation.equalsIgnoreCase(candidateLocation.trim())) {
            return 1.0;
        }
        
        return 0.5;
    }

    private double calculateAgeScore(Integer userAge, Integer candidateAge) {
        if (userAge == null || candidateAge == null) {
            return 0.0;
        }
        
        int ageDiff = Math.abs(userAge - candidateAge);
        if (ageDiff <= 2) return 1.0;
        if (ageDiff <= 5) return 0.7;
        if (ageDiff <= 10) return 0.4;
        return 0.1;
    }

    private static class ScoredUser {
        Long userId;
        double score;

        ScoredUser(Long userId, double score) {
            this.userId = userId;
            this.score = score;
        }
    }

    public void onSwipe(Long userId) {
        refillQueueAsync(userId);
    }

    public void invalidateQueue(Long userId) {
        recommendationQueues.remove(userId);
    }
}














