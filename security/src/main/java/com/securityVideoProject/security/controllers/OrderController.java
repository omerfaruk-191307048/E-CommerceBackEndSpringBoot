package com.securityVideoProject.security.controllers;

import com.securityVideoProject.security.business.OrderService;
import com.securityVideoProject.security.core.utilities.results.DataResult;
import com.securityVideoProject.security.dto.request.OrderRequestDto;
import com.securityVideoProject.security.dto.response.OrderResponseDto;
import com.securityVideoProject.security.entities.product.Product;
import com.securityVideoProject.security.entities.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public DataResult<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        return orderService.createOrder(orderRequestDto);
    }

    @GetMapping
    public DataResult<List<OrderResponseDto>> findAllOrder() {
        return orderService.findAllOrder();
    }

    @GetMapping("/find-by-id")
    public DataResult<OrderResponseDto> findOrderById(@RequestParam Integer orderId) {
        return orderService.findOrderById(orderId);
    }

    @GetMapping("/find-by-user")
    public DataResult<List<OrderResponseDto>> findOrderByUser(@RequestParam Integer userId) {
        return orderService.findOrderByUser(userId);
    }

    @GetMapping("/find-by-product")
    public DataResult<List<OrderResponseDto>> findOrderByProduct(@RequestParam Integer productId) {
        return orderService.findOrderByProduct(productId);
    }

    @GetMapping("/find-by-created-at-between")
    public DataResult<List<OrderResponseDto>> findOrderByCreatedAt(@RequestParam String startDate, @RequestParam String endDate) throws ParseException {
        return orderService.findOrderByCreatedAt(startDate, endDate);
    }

    @PutMapping
    public DataResult<OrderResponseDto> updateOrder(@RequestParam Integer orderId, @RequestBody OrderRequestDto orderRequestDto) {
        return orderService.updateOrder(orderId, orderRequestDto);
    }

    @DeleteMapping
    public DataResult<OrderResponseDto> deleteOrderById(@RequestParam Integer orderId) {
        return orderService.deleteOrderById(orderId);
    }
}
