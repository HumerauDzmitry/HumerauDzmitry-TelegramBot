package com.telegram.bot.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString( callSuper = true )
@EqualsAndHashCode( callSuper = false )
@Entity
@Table( name = "attractions")
public class Attraction extends BaseEntity{

    @Column( name = "name")
    private String name;
}
