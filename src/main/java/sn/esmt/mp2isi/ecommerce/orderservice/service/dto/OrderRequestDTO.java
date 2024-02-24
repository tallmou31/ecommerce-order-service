package sn.esmt.mp2isi.ecommerce.orderservice.service.dto;

import lombok.*;
import sn.esmt.mp2isi.ecommerce.orderservice.domain.Order;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    private Set<OrderItemRequestDTO> items = new HashSet<>();
    private String deliveryAddress;

    public OrderRequestDTO(Order order) {
        deliveryAddress = order.getDeliveryAddress();
        items = order.getOrderItems().stream().map(OrderItemRequestDTO::new).collect(Collectors.toSet());
    }
}
