package com.example.sb3product.mapper;

import java.util.List;

public interface IMapper <E,D>{
    D toDto(E entity);
    E toEntity(D dto);
    List<D> toDtoList(List<E> list);
    List<E> toEntityList(List<D> list);
}
