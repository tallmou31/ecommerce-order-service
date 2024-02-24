package sn.esmt.mp2isi.ecommerce.orderservice.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    List<OrderItemResponseDTO> items = new ArrayList<>();
    private OrderResponseStatus status;

    public void addItem(OrderItemResponseDTO item) {
        if(items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
    }

    public Double getTotal() {
        Double result = 0d;
        for (var item: items) {
            result += (item.getPrice() * item.getQuantity());
        }
        return result;
    }
}
