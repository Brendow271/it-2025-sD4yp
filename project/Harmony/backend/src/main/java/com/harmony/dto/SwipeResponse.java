package com.harmony.dto;

import com.harmony.entity.Swipe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SwipeResponse {
    private Long swipeId;
    private Long userId1;
    private Long userId2;
    private Boolean decision1;
    private Boolean decision2;
    private LocalDateTime createdAt;
    private Boolean isMatch;
    private String message;

    public SwipeResponse(Swipe swipe){
        this.swipeId = swipe.getSwipeId();
        this.userId1 = swipe.getUserId1();
        this.userId2 = swipe.getUserId2();
        this.decision1 = swipe.getDecision1();
        this.decision2 = swipe.getDecision2();
        this.createdAt = swipe.getCreatedAt();
        this.isMatch = swipe.getDecision1() != null && swipe.getDecision2() != null && swipe.getDecision1() && swipe.getDecision2();
        this.message = isMatch ? "Мэтч" : "Свайп сохранен";
    }
}
