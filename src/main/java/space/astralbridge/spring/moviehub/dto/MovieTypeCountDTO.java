package space.astralbridge.spring.moviehub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieTypeCountDTO {
    private List<String> types;
    private List<Integer> counts;
} 