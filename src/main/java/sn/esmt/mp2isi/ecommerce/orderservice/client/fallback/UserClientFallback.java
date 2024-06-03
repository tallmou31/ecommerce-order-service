package sn.esmt.mp2isi.ecommerce.orderservice.client.fallback;

import org.springframework.http.ResponseEntity;
import sn.esmt.mp2isi.ecommerce.orderservice.client.UserFeignClient;
import sn.esmt.mp2isi.ecommerce.orderservice.exception.CustomUnauthorizedRequestException;
import sn.esmt.mp2isi.ecommerce.orderservice.service.dto.UserDTO;

import java.util.List;

public class UserClientFallback implements UserFeignClient {
    @Override
    public ResponseEntity<UserDTO> getAccount() {
        throw new CustomUnauthorizedRequestException("Service Utilisateur indisponible. Veuillez vous reconnecter !");
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(Long id) {
        throw new CustomUnauthorizedRequestException("Service Utilisateur indisponible. Veuillez vous reconnecter !");
    }

    @Override
    public ResponseEntity<List<String>> getAdminEmails() {
        throw new CustomUnauthorizedRequestException("Service Utilisateur indisponible. Veuillez vous reconnecter !");
    }
}
