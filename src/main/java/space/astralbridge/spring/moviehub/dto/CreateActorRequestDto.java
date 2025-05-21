package space.astralbridge.spring.moviehub.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateActorRequestDto {
    @NotNull
    private String name;

    @Nullable
    private String photo;

    private String description = "";
}
