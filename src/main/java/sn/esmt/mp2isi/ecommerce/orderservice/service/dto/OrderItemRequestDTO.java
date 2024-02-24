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
public class OrderItemRequestDTO {
    @NotNull
    @Min(1)
    private Long quantity;
    private Long productId;

    public OrderItemRequestDTO(OrderItem orderItem) {
        quantity = orderItem.getQuantity();
        productId = orderItem.getProductId();
    }
}
