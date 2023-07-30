package com.example.telegrambotnbpcurrencyrates.service.meal_calculator;

import com.example.telegrambotnbpcurrencyrates.dao.meal_calculator.ProductDao;
import com.example.telegrambotnbpcurrencyrates.model.meal_calculator.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ProductService {
    private static final int NAME_INDEX = 0;
    private static final int CALORIES_INDEX = 1;
    private static final int PROTEINS_INDEX = 2;
    private static final int FATS_INDEX = 3;
    private static final int CARBOHYDRATES_INDEX = 4;

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public void registerNewProduct(String inputMessage) {
        Product product = new Product();
        String[] productInfo = inputMessage.split("/");
        product.setProductName(productInfo[NAME_INDEX]);
        product.setEnergyKcal(Integer.parseInt(productInfo[CALORIES_INDEX]));
        product.setProteins(BigDecimal.valueOf(Double.parseDouble(productInfo[PROTEINS_INDEX])));
        product.setFats(BigDecimal.valueOf(Double.parseDouble(productInfo[FATS_INDEX])));
        product.setCarbohydrates(BigDecimal.valueOf(Double.parseDouble(productInfo[CARBOHYDRATES_INDEX])));
        productDao.save(product);
    }

    @Transactional
    public Optional<Product> getProductByName(String productName) {
        return productDao.findByName(productName);
    }
}
