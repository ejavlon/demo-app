package uz.ejavlon.demoapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.ejavlon.demoapp.entity.Meal;

import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal,Integer> {
    Optional<Meal> findByName(String name);
}
