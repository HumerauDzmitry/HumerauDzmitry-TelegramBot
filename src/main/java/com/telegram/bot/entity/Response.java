package com.telegram.bot.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString( callSuper = true )
@EqualsAndHashCode( callSuper = false )

@Entity
@Table( name = "answers")
public class Response extends BaseEntity{

    @Column
    int amountAttractions;

    @Column( name = "text")
    String text;
}
