package com.coffeeshop.Pojos;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "INGREDIENT_THRESHOLD")
public class IngredientThreshold {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "INGREDIENT", unique = true)
    private Ingredients ingredientName;
    @Column(name = "QUANTITY")
    private int tresholdQuantity;
}
