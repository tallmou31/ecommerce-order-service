package sn.esmt.mp2isi.ecommerce.orderservice.service.mapper;

import org.mapstruct.*;
import sn.esmt.mp2isi.ecommerce.orderservice.domain.Order;
import sn.esmt.mp2isi.ecommerce.orderservice.service.dto.OrderDTO;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {
    OrderDTO toDto (Order order);
}
