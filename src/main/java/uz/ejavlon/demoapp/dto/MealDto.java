package uz.ejavlon.demoapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MealDto {
    @NotNull(message = "meal name empty")
    private String name;

    @NotNull(message = "mela price empty")
    private Double price;
}
