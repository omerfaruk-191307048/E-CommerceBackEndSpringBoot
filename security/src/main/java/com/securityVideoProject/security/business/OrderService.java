package com.securityVideoProject.security.business;

import com.securityVideoProject.security.auth.enums.EntityType;
import com.securityVideoProject.security.core.utilities.results.DataResult;
import com.securityVideoProject.security.core.utilities.results.ErrorDataResult;
import com.securityVideoProject.security.core.utilities.results.SuccessDataResult;
import com.securityVideoProject.security.dataAccess.abstracts.OrderRepository;
import com.securityVideoProject.security.dataAccess.abstracts.ProductRepository;
import com.securityVideoProject.security.dataAccess.abstracts.UserRepository;
import com.securityVideoProject.security.dto.response.OrderResponseDto;
import com.securityVideoProject.security.entities.order.Order;
import com.securityVideoProject.security.dto.request.OrderRequestDto;
import com.securityVideoProject.security.entities.product.Product;
import com.securityVideoProject.security.entities.user.User;
import com.securityVideoProject.security.exceptions.NotFoundException;
import com.securityVideoProject.security.mapper.OrderMapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final EntityManager entityManager;

    @Transactional
    public DataResult<OrderResponseDto> createOrder(OrderRequestDto orderRequestDto) {
        Order order = new Order();
        Product product = productRepository.findById(orderRequestDto.productId())
                .orElseThrow(() -> new NotFoundException(EntityType.Product + " not found"));
        Product managedProduct = entityManager.merge(product);

        User user = userRepository.findById(orderRequestDto.userId())
                .orElseThrow(() -> new NotFoundException(EntityType.User + " not found"));
        User managedUser = entityManager.merge(user);

        order.setProduct(managedProduct);
        order.setUser(managedUser);

        Order savedOrder = orderRepository.save(orderMapper.toEntity(orderRequestDto));


        OrderResponseDto responseDto = orderMapper.toDto(savedOrder);
        return new SuccessDataResult<OrderResponseDto>(responseDto, "Order successfully created");
    }

    public DataResult<List<OrderResponseDto>> findAllOrder() {
        if (orderRepository.findAll().isEmpty())
            throw new NotFoundException(EntityType.Order + " not found");
        return new SuccessDataResult<List<OrderResponseDto>>
                (orderMapper.toDtoList(orderRepository.findAll()),
                        "Orders successfully found");
    }

    public DataResult<OrderResponseDto> findOrderById(Integer orderId) {
        if (orderRepository.findById(orderId).isEmpty())
            throw new NotFoundException("Orders not found");
        return new SuccessDataResult<OrderResponseDto>
                (orderMapper.toDto(orderRepository.findById(orderId).get()),
                        "Order successfully found");
    }


    public DataResult<List<OrderResponseDto>> findOrderByUser(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty())
            throw new NotFoundException(EntityType.User + " not found");

        User user = userOptional.get();

        if (orderRepository.findOrderByUser(user).isEmpty())
            throw new NotFoundException(EntityType.User + " not found");
        return new SuccessDataResult<List<OrderResponseDto>>
                (orderMapper.toDtoList(orderRepository.findOrderByUser(user))
                        , "Orders successfully found");
    }

    public DataResult<List<OrderResponseDto>> findOrderByProduct(Integer productId) {
        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isPresent())
            throw new NotFoundException(EntityType.Product + " not found");

        Product product = productOptional.get();

        if (orderRepository.findOrderByProduct(product).isEmpty())
            throw new NotFoundException(EntityType.Product+ " not found");
        return new SuccessDataResult<List<OrderResponseDto>>
                (orderMapper.toDtoList(orderRepository.findOrderByProduct(product))
                        , "Orders successfully found");
    }

    public DataResult<List<OrderResponseDto>> findOrderByCreatedAt
            (String startDateInput, String endDateInput) throws ParseException {
        Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(startDateInput);
        Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(endDateInput);
        if (UserService.containsLetters(startDateInput) == true ||
                UserService.containsLetters(endDateInput) == true) {
            return new ErrorDataResult("You can not use letter when setting the date!");
        } else if (orderRepository.findOrdersByCreatedAtBetween(startDate, endDate).isEmpty())
            throw new NotFoundException(EntityType.Order + " not found between dates");
        return new SuccessDataResult<List<OrderResponseDto>>
                (orderMapper.toDtoList(orderRepository.findOrdersByCreatedAtBetween(startDate, endDate))
                        , "Orders successfully found between dates");
    }


    public DataResult<OrderResponseDto> updateOrder(Integer orderId, OrderRequestDto orderRequestDto) {
        Order order = orderRepository.findById(orderId).get();

        if (orderRepository.findById(order.getId()).isEmpty())
            throw new NotFoundException(EntityType.Order + " not found");
        return new SuccessDataResult<OrderResponseDto>
                (orderMapper.toDto(orderRepository.save(orderMapper.update(orderRequestDto, order)))
                        , "Order successfully updated");
    }

    public DataResult<OrderResponseDto> deleteOrderById(Integer orderId) {
        Order order = orderRepository.findById(orderId).get();
        if (orderRepository.findById(orderId).isEmpty())
            throw new NotFoundException(EntityType.Order + " not found");
        orderRepository.deleteById(orderId);
        return new SuccessDataResult<OrderResponseDto>("Order successfully deleted");
    }
}
