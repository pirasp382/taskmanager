package org.example.dto;

import lombok.Builder;

@Builder
public record UserData(
        String username,
        String fullname,
        String email,
        String bio,
        String token
) {}
