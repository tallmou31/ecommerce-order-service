package sn.esmt.mp2isi.ecommerce.orderservice.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListHolderDTO<E> {
    private List<E> items = new ArrayList<>();
}
