package uz.ejavlon.demoapp.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    User user;

    @Column(unique = true,length = 50,nullable = false)
    String name;

    Double price;

    @Column(name = "created_at")
    LocalDateTime createdAt;
}
