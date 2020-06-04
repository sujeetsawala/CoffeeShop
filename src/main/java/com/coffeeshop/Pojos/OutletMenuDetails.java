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
public class OutletMenuDetails {
    @Id
    @Column(name = "OUTLET_NAME")
    @Enumerated(value = EnumType.STRING)
    private OutletName outletName;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "OUTLET_MENU",
            joinColumns = @JoinColumn(name = "OUTLET"),
            inverseJoinColumns = @JoinColumn(name = "MENU")
    )
    Collection<MenuDetails> menuDetails = new ArrayList<>();

}
