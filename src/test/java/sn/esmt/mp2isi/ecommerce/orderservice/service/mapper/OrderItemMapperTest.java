package sn.esmt.mp2isi.ecommerce.orderservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderItemMapperTest {

    private OrderItemMapper orderItemMapper;

    @BeforeEach
    public void setUp() {
        orderItemMapper = new OrderItemMapperImpl();
    }
}
