package uz.ejavlon.demoapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.ejavlon.demoapp.dto.MealDto;
import uz.ejavlon.demoapp.dto.ResponseApi;
import uz.ejavlon.demoapp.entity.Meal;
import uz.ejavlon.demoapp.entity.User;
import uz.ejavlon.demoapp.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;

    public ResponseApi getAllMeals() {
        return ResponseApi.builder()
                .message("All meal plans")
                .success(true)
                .data(mealRepository.findAll())
                .build();
    }


    public ResponseApi getMealById(Integer id) {
        Optional<Meal> optionalMeal = mealRepository.findById(id);
        return ResponseApi.builder()
                .message(optionalMeal.isPresent() ? "Meal found" : "Meal not found")
                .success(true)
                .data(optionalMeal.orElse(null))
                .build();
    }

    public ResponseApi createMeal(MealDto mealDto) {
        if (mealRepository.findByName(mealDto.getName()).isPresent())
            return ResponseApi.builder()
                    .success(true)
                    .message("Meal found")
                    .build();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Meal meal = Meal.builder()
                .createdAt(LocalDateTime.now())
                .user(user)
                .name(mealDto.getName())
                .price(mealDto.getPrice())
                .build();
        meal = mealRepository.save(meal);

        return ResponseApi.builder()
                .data(meal)
                .success(true)
                .message("Meal successfully added")
                .build();
    }

    public ResponseApi searchMealByName(String name) {
        Optional<Meal> optionalMeal = mealRepository.findByName(name);
        return ResponseApi.builder()
                .data(optionalMeal.orElse(null))
                .message(optionalMeal.isPresent() ? "Meal found" : "Meal not found")
                .success(true)
                .build();
    }

    public ResponseApi deleteMeal(Integer id) {
        Optional<Meal> optionalMeal = mealRepository.findById(id);
        if(!optionalMeal.isPresent())
            return ResponseApi.builder()
                    .success(true)
                    .message("Meal not found")
                    .build();

        mealRepository.deleteById(id);
        return ResponseApi.builder()
                .success(true)
                .message("Meal successfully deleted")
                .build();
    }

    public ResponseApi updateMeal(Integer id,MealDto mealDto) {
        Optional<Meal> optionalMeal = mealRepository.findById(id);
        if(!optionalMeal.isPresent())
            return ResponseApi.builder()
                    .success(true)
                    .message("Meal not found")
                    .build();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Meal meal = optionalMeal.get();
        meal.setName(mealDto.getName());
        meal.setPrice(mealDto.getPrice());
        meal.setUser(user);
        mealRepository.save(meal);

        return ResponseApi.builder()
                .success(true)
                .message("Meal successfully updated")
                .build();
    }
}
