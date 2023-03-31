package com.example.security.responseBodyModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserData {

    private Long id;
    private Long userId;

    private String firstName;

    private String lastName;
@Nullable
    private LocalDateTime registrationDate;
}
