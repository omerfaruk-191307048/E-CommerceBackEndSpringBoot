package com.securityVideoProject.security.dataAccess.abstracts;

import com.securityVideoProject.security.dto.request.OrderRequestDto;
import com.securityVideoProject.security.entities.order.Order;
import com.securityVideoProject.security.entities.product.Product;
import com.securityVideoProject.security.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findOrdersByCreatedAtBetween(Date start, Date end);
    List<Order> findOrderByUser(User userId);
    List<Order> findOrderByProduct(Product productId);
}
