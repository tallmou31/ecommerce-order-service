package sn.esmt.mp2isi.ecommerce.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sn.esmt.mp2isi.ecommerce.orderservice.client.fallback.UserClientFallback;
import sn.esmt.mp2isi.ecommerce.orderservice.service.dto.UserDTO;

import java.util.List;

@FeignClient(name = "userservice", fallback = UserClientFallback.class)
public interface UserFeignClient {
    @GetMapping("/api/account")
    ResponseEntity<UserDTO> getAccount();

    @GetMapping("/api/admin/users/byId/{id}")
    ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id);

    @GetMapping("/api/admin-emails")
    ResponseEntity<List<String>> getAdminEmails();
}
