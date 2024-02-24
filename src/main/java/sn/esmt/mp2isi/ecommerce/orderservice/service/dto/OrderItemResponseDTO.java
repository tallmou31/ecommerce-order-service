package sn.esmt.mp2isi.ecommerce.orderservice.service.dto;

import lombok.*;
import sn.esmt.mp2isi.ecommerce.orderservice.domain.OrderItem;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderItemResponseDTO {
    @NotNull
    @Min(1)
    private Long quantity;
    private Long available;
    private Long productId;
    private Double price;
    private String productReference;
    private String productName;

    public OrderItemResponseDTO(OrderItem orderItem, ProductDTO product) {
        quantity = orderItem.getQuantity();
        price = orderItem.getActualPrice();
        if(product != null) {
            productReference = product.getReference();
            productName = product.getName();
        }

    }
}
