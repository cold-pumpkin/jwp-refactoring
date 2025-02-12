package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Product;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;

    public ProductResponse() {
    }

    public ProductResponse(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse from(final Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }

    public static List<ProductResponse> fromToList(final List<Product> products) {
        return products.stream()
                .map(ProductResponse::from)
                .collect(toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
