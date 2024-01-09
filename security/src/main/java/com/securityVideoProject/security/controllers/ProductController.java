package com.securityVideoProject.security.controllers;

import com.securityVideoProject.security.business.ProductService;
import com.securityVideoProject.security.core.utilities.results.DataResult;
import com.securityVideoProject.security.dto.request.ProductRequestDto;
import com.securityVideoProject.security.dto.response.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public DataResult<ProductResponseDto> createProduct(@RequestBody ProductRequestDto productRequestDto) {
        return productService.createProduct(productRequestDto);
    }

    @GetMapping
    public DataResult<List<ProductResponseDto>> findAllProduct() {
        return productService.findAllProduct();
    }

    @GetMapping("/find-by-id")
    public DataResult<ProductResponseDto> findProductById(@RequestParam Integer productId) {
        return productService.findProductById(productId);
    }

    @GetMapping("/find-by-name-contains")
    public DataResult<List<ProductResponseDto>> findByProductNameContains(@RequestParam String productName) {
        return productService.findProductNameContains(productName);
    }

    @GetMapping("/find-by-name")
    public DataResult<List<ProductResponseDto>> findProductByName(@RequestParam String productName) {
        return productService.findProductByName(productName);
    }

    @GetMapping("/find-by-created-at-between")
    public DataResult<List<ProductResponseDto>> findProductByCreatedAtBetween(@RequestParam String startDate, @RequestParam String endDate) throws ParseException {
        return productService.findProductByCreatedAt(startDate, endDate);
    }

    @PutMapping
    public DataResult<ProductResponseDto> updateProduct(@RequestParam Integer productId, @RequestBody ProductRequestDto productRequestDto) {
        return productService.updateProduct(productId, productRequestDto);
    }

    @DeleteMapping
    public DataResult<ProductResponseDto> deleteProductById(@RequestParam Integer productId) {
        return productService.deleteProductById(productId);
    }
}
