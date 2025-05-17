package space.astralbridge.spring.moviehub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordResetRequest {
    private String username;

    private String token;

    @NotBlank
    @Size(min = 6, max = 50)
    private String newPassword;
}
