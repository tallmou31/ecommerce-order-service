package sn.esmt.mp2isi.ecommerce.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sn.esmt.mp2isi.ecommerce.orderservice.service.dto.ListHolderDTO;
import sn.esmt.mp2isi.ecommerce.orderservice.service.dto.OrderRequestDTO;
import sn.esmt.mp2isi.ecommerce.orderservice.service.dto.OrderResponseDTO;
import sn.esmt.mp2isi.ecommerce.orderservice.service.dto.ProductDTO;

import java.util.List;

@FeignClient(name = "productservice")
public interface ProductFeignClient {
    @PostMapping("/api/products/validate-order")
    ResponseEntity<OrderResponseDTO> validateOrder(
        @RequestBody OrderRequestDTO order
    );

    @PostMapping("/api/products/cancel-order")
    ResponseEntity<Void> cancelOrder(
        @RequestBody OrderRequestDTO order
    );

    @PostMapping("/api/products/get-all-ids")
    ResponseEntity<List<ProductDTO>> getAllProductsByIds(@RequestBody ListHolderDTO<Long> ids);
}
