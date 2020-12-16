package com.telegram.bot.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString( callSuper = true )
@EqualsAndHashCode( callSuper = false )
@ApiModel( description = "DTO, Достопримечательности")
public class AttractionDto {

    @ApiModelProperty( value = "Id Достопримечательности")
    private Long id;

    @ApiModelProperty( value = "Достопримечательность")
    private String name;
}
