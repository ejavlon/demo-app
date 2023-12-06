package uz.ejavlon.demoapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.ejavlon.demoapp.dto.MealDto;
import uz.ejavlon.demoapp.dto.ResponseApi;
import uz.ejavlon.demoapp.service.MealService;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/mealplan")
@RequiredArgsConstructor
public class MealController {
    private final MealService mealService;

    @GetMapping
    public ResponseEntity<?> getAllMeals(){
        ResponseApi responseApi = mealService.getAllMeals();
        return ResponseEntity.status(responseApi.getSuccess() ? OK : CONFLICT).body(responseApi);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMealById(@PathVariable Integer id){
        ResponseApi responseApi = mealService.getMealById(id);
        return ResponseEntity.status(responseApi.getSuccess() ? OK : NOT_FOUND).body(responseApi);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchMeal(@RequestParam(name = "name") String name){
        ResponseApi responseApi = mealService.searchMealByName(name);
        return ResponseEntity.status(responseApi.getSuccess() ? OK : NOT_FOUND).body(responseApi);
    }

    @PostMapping
    public ResponseEntity<?> createMeal(@Valid @RequestBody MealDto mealDto){
        ResponseApi responseApi = mealService.createMeal(mealDto);
        return ResponseEntity.status(responseApi.getSuccess() ? CREATED : NOT_FOUND).body(responseApi);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeal(@PathVariable Integer id){
        ResponseApi responseApi = mealService.deleteMeal(id);
        return ResponseEntity.status(responseApi.getSuccess() ? NO_CONTENT : NOT_FOUND).body(responseApi);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMeal(@PathVariable Integer id,@RequestBody MealDto mealDto){
        ResponseApi responseApi = mealService.updateMeal(id,mealDto);
        return ResponseEntity.status(responseApi.getSuccess() ? OK : NOT_FOUND).body(responseApi);
    }

 }
