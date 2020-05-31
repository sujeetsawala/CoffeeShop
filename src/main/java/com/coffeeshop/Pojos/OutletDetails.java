package com.coffeeshop.Pojos;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "OUTLET_DETAILS")
public class OutletDetails {
    @Id
    @Column(name = "OUTLET_NAME", unique = true)
    @Enumerated(value = EnumType.STRING)
    private OutletName outletName;

    @ElementCollection
    @JoinTable(name = "OUTLET_INGREDIENTS",
            joinColumns= @JoinColumn(name = "OUTLET")
    )
    Collection<Composition> availableComposition = new ArrayList<>();

}

