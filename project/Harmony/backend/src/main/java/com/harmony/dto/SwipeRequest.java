package com.harmony.dto;

import jdk.incubator.foreign.SymbolLookup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SwipeRequest {
    private Long userId1;
    private Long userId2;
    private Boolean decision;
}
