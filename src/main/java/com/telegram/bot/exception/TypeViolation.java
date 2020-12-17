package com.telegram.bot.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeViolation implements Serializable {
    private String objectName;
    private List<String> messages;
}
