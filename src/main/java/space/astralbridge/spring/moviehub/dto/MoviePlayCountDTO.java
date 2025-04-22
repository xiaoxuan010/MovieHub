package space.astralbridge.spring.moviehub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoviePlayCountDTO {
    private List<String> titles;
    private List<Integer> playCounts;
}
