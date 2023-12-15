package com.example.sb3product.mapper;

import com.example.sb3product.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.openapitools.model.Product;

@Mapper(config = MapstructConfigs.class)
public interface ProductMapper extends IMapper<ProductEntity, Product> {
}
