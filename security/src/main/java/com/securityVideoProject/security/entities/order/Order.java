package com.securityVideoProject.security.entities.order;

import com.securityVideoProject.security.entities.product.Product;
import com.securityVideoProject.security.entities.user.BaseEntity;
import com.securityVideoProject.security.entities.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_order")
@EqualsAndHashCode(callSuper = true)
public class Order extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Integer id;
    private Integer orderQuantity;
    private String deliveryAddress;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;
}
