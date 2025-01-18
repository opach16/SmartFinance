package com.konrad.smartfinance.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDetailsDto {
    private String username;
    private Long userId;
}
