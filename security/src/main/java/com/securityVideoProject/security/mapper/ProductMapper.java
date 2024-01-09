package com.securityVideoProject.security.mapper;

import com.securityVideoProject.security.dto.request.ProductRequestDto;
import com.securityVideoProject.security.dto.response.ProductResponseDto;
import com.securityVideoProject.security.entities.product.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductRequestDto productRequestDto);

    ProductResponseDto toDto(Product product);

    List<ProductResponseDto> toDtoList(List<Product> products);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product update(ProductRequestDto productRequestDto, @MappingTarget Product product);
}
