package com.example.telegrambotnbpcurrencyrates.model.meal_calculator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "MEAL_DETAILS")
@Getter
@Setter
public class MealDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Meal meal;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column(name = "product_weight")
    private Integer productWeight;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MealDetail)) return false;
        MealDetail that = (MealDetail) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MealDetail{" +
                "product=" + product +
                ", productWeight=" + productWeight +
                '}';
    }
}
