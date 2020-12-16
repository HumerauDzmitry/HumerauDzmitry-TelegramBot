package com.telegram.bot.service.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public interface EntityMapper<D, E> {

    E toEntity(D dto);

    D toDto(E entity);

    List<E> toEntity(List<D> dtoList);

    List<D> toDto(List<E> entityList);

    default Page<D> toDto(Page<E> entityPage) {
        return new PageImpl<>(
                toDto(entityPage.getContent()),
                entityPage.getPageable(),
                entityPage.getTotalElements()
        );
    }
}
