package kitchenpos.domain.product.domain;

import kitchenpos.domain.product.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product entity);

    Optional<Product> findById(Long id);

    List<Product> findAll();
}