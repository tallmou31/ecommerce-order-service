package sn.esmt.mp2isi.ecommerce.orderservice.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.esmt.mp2isi.ecommerce.orderservice.client.ProductFeignClient;
import sn.esmt.mp2isi.ecommerce.orderservice.client.UserFeignClient;
import sn.esmt.mp2isi.ecommerce.orderservice.domain.Order;
import sn.esmt.mp2isi.ecommerce.orderservice.domain.OrderItem;
import sn.esmt.mp2isi.ecommerce.orderservice.domain.enumeration.OrderStatus;
import sn.esmt.mp2isi.ecommerce.orderservice.exception.CustomBadRequestException;
import sn.esmt.mp2isi.ecommerce.orderservice.exception.CustomUnauthorizedRequestException;
import sn.esmt.mp2isi.ecommerce.orderservice.repository.OrderItemRepository;
import sn.esmt.mp2isi.ecommerce.orderservice.repository.OrderRepository;
import sn.esmt.mp2isi.ecommerce.orderservice.service.MailService;
import sn.esmt.mp2isi.ecommerce.orderservice.service.OrderService;
import sn.esmt.mp2isi.ecommerce.orderservice.service.dto.*;
import sn.esmt.mp2isi.ecommerce.orderservice.service.mapper.OrderMapper;

/**
 * Service Implementation for managing {@link Order}.
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final UserFeignClient userFeignClient;
    private final ProductFeignClient productFeignClient;

    private final OrderMapper orderMapper;

    private final MailService mailService;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, UserFeignClient userFeignClient, ProductFeignClient productFeignClient, OrderMapper orderMapper, MailService mailService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userFeignClient = userFeignClient;
        this.productFeignClient = productFeignClient;
        this.orderMapper = orderMapper;
        this.mailService = mailService;
    }

    @Override
    public OrderDTO save(OrderDTO orderDTO) {
        log.debug("Request to save Order : {}", orderDTO);
        Order order = orderMapper.toEntity(orderDTO);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDTO update(OrderDTO orderDTO) {
        log.debug("Request to update Order : {}", orderDTO);
        Order order = orderMapper.toEntity(orderDTO);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public Optional<OrderDTO> partialUpdate(OrderDTO orderDTO) {
        log.debug("Request to partially update Order : {}", orderDTO);

        return orderRepository
            .findById(orderDTO.getId())
            .map(existingOrder -> {
                orderMapper.partialUpdate(existingOrder, orderDTO);

                return existingOrder;
            })
            .map(orderRepository::save)
            .map(orderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Orders");
        return orderRepository.findAll(pageable).map(orderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderDTO> findOne(Long id) {
        log.debug("Request to get Order : {}", id);
        return orderRepository.findById(id).map(orderMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Order : {}", id);
        orderRepository.deleteById(id);
    }

    @Override
    public OrderResponseDTO validateOrder(OrderRequestDTO orderRequest) {
        var user = getUser();
        var respOrder = productFeignClient.validateOrder(orderRequest);
        if(!respOrder.getStatusCode().equals(HttpStatus.OK)) {
            throw new CustomBadRequestException("Une erreur est survenue !");
        }
        var result = respOrder.getBody();
        if(result.getStatus() == OrderResponseStatus.OK) {
            var order = new Order();
            order.setStatus(OrderStatus.EN_COURS_LIVRAISON);
            order.setDate(Instant.now());
            order.setUserId(user.getId());
            order.setDeliveryAddress(orderRequest.getDeliveryAddress());
            order.setExpectedDeliveryDate(LocalDate.now().plusDays(3));
            var newOrder = orderRepository.save(order);
            result.getItems().forEach(item -> {
                var orderItem = new OrderItem();
                orderItem.setOrder(newOrder);
                orderItem.setActualPrice(item.getPrice());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setProductId(item.getProductId());
                orderItemRepository.save(orderItem);
            });

            var respAdminEmails = userFeignClient.getAdminEmails();
            if(respAdminEmails.getStatusCode() == HttpStatus.OK) {
                var to = respAdminEmails.getBody().toArray(new String[0]);
                mailService.sendCommandeEmailToAdmin(to, result);
            }

            mailService.sendCommandeEmailToCustomer(user, result, order.getExpectedDeliveryDate(), orderRequest.getDeliveryAddress());
        }
        return result;
    }

    @Override
    @Transactional
    public OrderDTO clodeOrder(Long id, OrderStatus status) {
        return orderRepository.findById(id).map(order -> {
            order.setStatus(status);
            var productsResp = productFeignClient.getAllProductsByIds(new ListHolderDTO<>(order.getOrderItems().stream().map(i -> i.getProductId()).collect(Collectors.toList()))).getBody();
            var orderItems = order.getOrderItems().stream().map(o -> new OrderItemResponseDTO(o, productsResp.stream().filter(p -> p.getId().equals(o.getProductId())).findFirst().orElse(null))).collect(Collectors.toSet());
            if(status == OrderStatus.LIVRE) {
                order.setDeliveryDate(LocalDate.now());
                mailService.sendDeliveredCommandeEmailToCustomer(getUserById(order.getUserId()), orderItems, order.getDeliveryAddress(), order.getTotal());
            }
            if(status == OrderStatus.ANNULE) {
                productFeignClient.cancelOrder(new OrderRequestDTO(order));
                mailService.sendCancelledCommandeEmailToCustomer(getUserById(order.getUserId()), orderItems, order.getTotal());
            }
            orderRepository.save(order);
            return orderMapper.toDto(order);
        }).orElseThrow(() -> new CustomBadRequestException("Commande introuvable"));
    }

    @Override
    public UserDTO getUser() {
        var respUser = userFeignClient.getAccount();
        if(!respUser.getStatusCode().equals(HttpStatus.OK)) {
            throw new CustomUnauthorizedRequestException("Veuillez vous connecter !");
        }
        return respUser.getBody();
    }

    @Override
    public UserDTO getUserById(Long id) {
        var respUser = userFeignClient.getUserById(id);
        if(!respUser.getStatusCode().equals(HttpStatus.OK)) {
            throw new CustomUnauthorizedRequestException("Veuillez vous connecter !");
        }
        return respUser.getBody();
    }
}
