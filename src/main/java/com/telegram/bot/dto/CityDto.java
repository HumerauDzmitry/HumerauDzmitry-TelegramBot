package com.telegram.bot.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString( callSuper = true )
@EqualsAndHashCode( callSuper = false )
@ApiModel( description = "DTO, Города")
public class CityDto {

    @ApiModelProperty( value = "Id Города")
    private Long id;

    @ApiModelProperty( value = "Город")
    private String name;
}
