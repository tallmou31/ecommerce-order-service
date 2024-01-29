package sn.esmt.mp2isi.ecommerce.orderservice.service.mapper;

import org.mapstruct.*;
import sn.esmt.mp2isi.ecommerce.orderservice.domain.Order;
import sn.esmt.mp2isi.ecommerce.orderservice.domain.OrderItem;
import sn.esmt.mp2isi.ecommerce.orderservice.service.dto.OrderDTO;
import sn.esmt.mp2isi.ecommerce.orderservice.service.dto.OrderItemDTO;

/**
 * Mapper for the entity {@link OrderItem} and its DTO {@link OrderItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderItemMapper extends EntityMapper<OrderItemDTO, OrderItem> {
    @Mapping(target = "order", source = "order", qualifiedByName = "orderId")
    OrderItemDTO toDto(OrderItem s);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);
}
