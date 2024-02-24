package sn.esmt.mp2isi.ecommerce.orderservice.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.esmt.mp2isi.ecommerce.orderservice.domain.enumeration.OrderStatus;
import sn.esmt.mp2isi.ecommerce.orderservice.service.dto.OrderDTO;
import sn.esmt.mp2isi.ecommerce.orderservice.service.dto.OrderRequestDTO;
import sn.esmt.mp2isi.ecommerce.orderservice.service.dto.OrderResponseDTO;
import sn.esmt.mp2isi.ecommerce.orderservice.service.dto.UserDTO;

/**
 * Service Interface for managing {@link sn.esmt.mp2isi.ecommerce.orderservice.domain.Order}.
 */
public interface OrderService {
    /**
     * Save a order.
     *
     * @param orderDTO the entity to save.
     * @return the persisted entity.
     */
    OrderDTO save(OrderDTO orderDTO);

    /**
     * Updates a order.
     *
     * @param orderDTO the entity to update.
     * @return the persisted entity.
     */
    OrderDTO update(OrderDTO orderDTO);

    /**
     * Partially updates a order.
     *
     * @param orderDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrderDTO> partialUpdate(OrderDTO orderDTO);

    /**
     * Get all the orders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrderDTO> findAll(Pageable pageable);

    /**
     * Get the "id" order.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrderDTO> findOne(Long id);

    /**
     * Delete the "id" order.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    OrderResponseDTO validateOrder(OrderRequestDTO order);

    OrderDTO clodeOrder(Long id, OrderStatus status);

    UserDTO getUser();

    UserDTO getUserById(Long id);
}
