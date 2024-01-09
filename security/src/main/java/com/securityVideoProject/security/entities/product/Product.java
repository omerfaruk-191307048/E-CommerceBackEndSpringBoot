package com.securityVideoProject.security.entities.product;

import com.securityVideoProject.security.entities.order.Order;
import com.securityVideoProject.security.entities.user.BaseEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_product")
@Component
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private Integer id;
    @NotNull(message = "Can not be null")
    private String productName;
    private Integer productPrice;
    private Integer quantity;

    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST)
    private List<Order> orders;
}
