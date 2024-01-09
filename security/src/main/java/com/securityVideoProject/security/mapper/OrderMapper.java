package com.securityVideoProject.security.mapper;

import com.securityVideoProject.security.dataAccess.abstracts.ProductRepository;
import com.securityVideoProject.security.dataAccess.abstracts.UserRepository;
import com.securityVideoProject.security.dto.request.OrderRequestDto;
import com.securityVideoProject.security.dto.response.OrderResponseDto;
import com.securityVideoProject.security.entities.order.Order;
import com.securityVideoProject.security.entities.product.Product;
import com.securityVideoProject.security.entities.user.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {UserRepository.class,
        ProductRepository.class})
public interface OrderMapper {

    @Mappings({
            @Mapping(target = "user.id", source = "orderRequestDto.userId"),
            @Mapping(target = "product.id", source = "orderRequestDto.productId"),
            @Mapping(target = "createdAt", expression = "java(new java.util.Date())"),
            @Mapping(target = "updatedAt", expression = "java(new java.util.Date())")
    })
    Order toEntity(OrderRequestDto orderRequestDto);

    @Mappings({
            @Mapping(target = "userId", source = "order.user.id"),
            @Mapping(target = "productId", source = "order.product.id")
    })
    OrderResponseDto toDto(Order order);

    @Mappings({
            @Mapping(target = "userId", source = "order.user.id"),
            @Mapping(target = "productId", source = "order.product.id")
    })
    List<OrderResponseDto> toDtoList(List<Order> orders);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Order update(OrderRequestDto orderRequestDto, @MappingTarget Order order);

    default User getUserIdToUser(Integer userId, @Context UserRepository userRepository) {
        return userId != null ? userRepository.findById(userId).orElse(null) : null;
    }

    default Product getProductIdToProduct(Integer productId, @Context ProductRepository productRepository) {
        return productId != null ? productRepository.findById(productId).orElse(null) : null;
    }
}
