package com.example.telegrambotnbpcurrencyrates.model.meal_calculator;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "USERS")
@Getter
@Setter
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column(name = "daily_kilocalorie_intake")
    private Integer dailyKilocalorieIntake;

    @Column(name = "daily_proteins_intake")
    private BigDecimal dailyProteinsIntake;

    @Column(name = "daily_fats_intake")
    private BigDecimal dailyFatsIntake;

    @Column(name = "daily_carbohydrates_intake")
    private BigDecimal dailyCarbohydratesIntake;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    private Set<Meal> meals = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", dailyKilocalorieIntake=" + dailyKilocalorieIntake +
                '}';
    }
}
