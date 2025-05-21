package space.astralbridge.spring.moviehub.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateUserRequestDto {
    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String email;

    private Integer userType = 0;

    private Integer status = 1;

}
