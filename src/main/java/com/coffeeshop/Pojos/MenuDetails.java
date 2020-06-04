package com.coffeeshop.Pojos;

import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@Entity
@Getter
@Setter
public class MenuDetails {

    @Id
    @Column(name="MENU_NAME")
    @Enumerated(EnumType.STRING)
    private Menus menuName;

    @ManyToMany(mappedBy = "menuDetails", cascade = CascadeType.PERSIST)
    private Collection<OutletMenuDetails> OutletMenuDetails = new HashSet<>();

    @ElementCollection
    @JoinTable(
            name="MENU_COMPOSITION",
            joinColumns=@JoinColumn(name = "MENU")
    )
    private Collection<Composition> menuCompositions = new ArrayList<>();
}
