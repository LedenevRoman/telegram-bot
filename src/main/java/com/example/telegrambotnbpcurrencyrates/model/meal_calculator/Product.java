package com.example.telegrambotnbpcurrencyrates.model.meal_calculator;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "PRODUCTS")
@Getter
@Setter
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "energy_kcal")
    private Integer energyKcal;

    @Column
    private BigDecimal proteins;

    @Column
    private BigDecimal fats;

    @Column
    private BigDecimal carbohydrates;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL
    )
    private List<MealDetail> mealDetails = new ArrayList<>();

    public void addMealDetail(MealDetail mealDetail) {
        this.mealDetails.add(mealDetail);
        mealDetail.setProduct(this);
    }

    public void removeMealDetail(MealDetail mealDetail) {
        this.mealDetails.remove(mealDetail);
        mealDetail.setProduct(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", energyKcal=" + energyKcal +
                ", proteins=" + proteins +
                ", fats=" + fats +
                ", carbohydrates=" + carbohydrates +
                '}';
    }
}
