package com.capstone.workspace.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Setter(AccessLevel.NONE)
@Data
public class ResetPasswordDto {
    @NotBlank
    @NotNull
    private String username;

    @NotBlank
    @NotNull
    private String password;

    @NotBlank
    @NotNull
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#]).{8,}$")
    private String newPassword;
}
