package com.telegram.bot.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString( callSuper = true )
@EqualsAndHashCode( callSuper = false )

@Entity
@Table( name = "cities")
public class City extends BaseEntity{

    @Column( name = "name")
    String name;

    @Builder.Default
    @OneToMany( targetEntity = Attraction.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable( name = "cities_attractions", joinColumns = {@JoinColumn( name = "city_id" )},
        inverseJoinColumns = {@JoinColumn( name = "attraction_id" )})
    private List<Attraction> attractionList = new ArrayList<>();
}
