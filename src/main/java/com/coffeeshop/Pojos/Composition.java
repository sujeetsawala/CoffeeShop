package com.coffeeshop.Pojos;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;



@Getter
@Setter
@Embeddable
public class Composition {

    @Enumerated(value = EnumType.STRING)
    @Column(name = "INGREDIENT")
    private Ingredients ingredient;
    @Column(name = "QUANTITY")
    private int ingredientQuantity;

}
