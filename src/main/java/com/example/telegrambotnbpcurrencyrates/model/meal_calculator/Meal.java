package com.example.telegrambotnbpcurrencyrates.model.meal_calculator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "MEALS")
@Getter
@Setter
@ToString
public class Meal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "energy_kcal")
    private BigDecimal energyKcal;

    @Column
    private BigDecimal proteins;

    @Column
    private BigDecimal fats;

    @Column
    private BigDecimal carbohydrates;

    @OneToMany(
            mappedBy = "meal",
            cascade = CascadeType.ALL
    )
    private List<MealDetail> mealDetails = new ArrayList<>();


    public void addMealDetail(MealDetail mealDetail) {
        this.mealDetails.add(mealDetail);
        mealDetail.setMeal(this);
    }

    public void removeMealDetail(MealDetail mealDetail) {
        this.mealDetails.remove(mealDetail);
        mealDetail.setMeal(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Meal)) return false;
        Meal meal = (Meal) o;
        return Objects.equals(id, meal.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

