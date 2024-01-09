package com.securityVideoProject.security.business;

import com.securityVideoProject.security.auth.enums.EntityType;
import com.securityVideoProject.security.core.utilities.results.DataResult;
import com.securityVideoProject.security.core.utilities.results.ErrorDataResult;
import com.securityVideoProject.security.core.utilities.results.SuccessDataResult;
import com.securityVideoProject.security.dataAccess.abstracts.ProductRepository;
import com.securityVideoProject.security.dto.response.ProductResponseDto;
import com.securityVideoProject.security.entities.product.Product;
import com.securityVideoProject.security.dto.request.ProductRequestDto;
import com.securityVideoProject.security.exceptions.NotFoundException;
import com.securityVideoProject.security.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public DataResult<ProductResponseDto> createProduct(ProductRequestDto productRequestDto) {
        return new SuccessDataResult<ProductResponseDto>
                (productMapper.toDto(productRepository.save(productMapper.toEntity(productRequestDto)))
                        , "Product created successfully");
    }

    public DataResult<List<ProductResponseDto>> findAllProduct() {
        if (productRepository.findAll().isEmpty())
            throw new NotFoundException(EntityType.Product + " not found");
        return new SuccessDataResult<List<ProductResponseDto>>
                (productMapper.toDtoList(productRepository.findAll())
                        , "Products successfully found");
    }

    public DataResult<ProductResponseDto> findProductById(Integer productId) {
        if (productRepository.findById(productId).isEmpty())
            throw new NotFoundException(EntityType.Product + " not found");
        return new SuccessDataResult<ProductResponseDto>
                (productMapper.toDto(productRepository.findById(productId).get())
                        , "Product successfully found");

    }

    public DataResult<List<ProductResponseDto>> findProductNameContains(String productName) {
        if (productRepository.findByProductNameContains(productName).isEmpty())
            throw new NotFoundException(EntityType.Product + " not found");
        return new SuccessDataResult<List<ProductResponseDto>>
                (productMapper.toDtoList(productRepository.findByProductNameContains(productName))
                        , "Products successfully found");
    }

    public DataResult<List<ProductResponseDto>> findProductByName(String productName) {
        if (productRepository.findProductByProductName(productName).isEmpty())
            throw new NotFoundException(EntityType.Product + " not found");
        return new SuccessDataResult<List<ProductResponseDto>>
                (productMapper.toDtoList(productRepository.findByProductName(productName))
                        , "Products successfully found");
    }

    public DataResult<List<ProductResponseDto>> findProductByCreatedAt
            (String startDateInput, String endDateInput) throws ParseException {
        Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(startDateInput);
        Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(endDateInput);
        if (UserService.containsLetters(startDateInput) == true ||
                UserService.containsLetters(endDateInput)) {
            return new ErrorDataResult("You can not use letter when setting the date!");
        } else if (productRepository.findProductByCreatedAtBetween(startDate, endDate).isEmpty())
            throw new NotFoundException(" not found between dates");
        return new SuccessDataResult<List<ProductResponseDto>>
                (productMapper.toDtoList(productRepository.findProductByCreatedAtBetween(startDate, endDate))
                        , "Products successfully found between dates");

    }

    public DataResult<ProductResponseDto> updateProduct
            (Integer productId, ProductRequestDto productRequestDto) {
        Product product = productRepository.findById(productId).get();

        if (productRepository.findById(product.getId()).isEmpty())
            throw new NotFoundException(EntityType.Product + " not found");
        return new SuccessDataResult<ProductResponseDto>
                (productMapper.toDto(productRepository.save
                        (productMapper.update(productRequestDto, product)))
                        , "Product successfully updated");
    }

    public DataResult<ProductResponseDto> deleteProductById(Integer productId) {
        Product product = productRepository.findById(productId).get();
        if (productRepository.findById(product.getId()).isEmpty())
            throw new NotFoundException(EntityType.Product + " not found");
        productRepository.deleteById(productId);
        return new SuccessDataResult<ProductResponseDto>("Product successfully deleted");
    }
}
