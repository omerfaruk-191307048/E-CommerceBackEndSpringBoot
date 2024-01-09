package com.securityVideoProject.security.dataAccess.abstracts;

import com.securityVideoProject.security.entities.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByProductName(String productName);
    List<Product> findByProductNameContains(String productName);
    List<Product> findProductByProductName(String productName);
    List<Product> findProductByCreatedAtBetween(Date start, Date end);
}
