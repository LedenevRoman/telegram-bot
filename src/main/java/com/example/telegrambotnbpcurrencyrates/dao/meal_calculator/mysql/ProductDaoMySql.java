package com.example.telegrambotnbpcurrencyrates.dao.meal_calculator.mysql;

import com.example.telegrambotnbpcurrencyrates.dao.meal_calculator.ProductDao;
import com.example.telegrambotnbpcurrencyrates.model.meal_calculator.Product;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProductDaoMySql extends CrudDaoMySql<Product> implements ProductDao {

    public ProductDaoMySql() {
        setClazz(Product.class);
    }

    @Override
    public Optional<Product> findByName(String productName) {
        try {
            return Optional.ofNullable(getEntityManager().createQuery("from Product product " +
                            "where product.productName = :productName", Product.class)
                    .setParameter("productName", productName)
                    .getSingleResult());
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }
}
