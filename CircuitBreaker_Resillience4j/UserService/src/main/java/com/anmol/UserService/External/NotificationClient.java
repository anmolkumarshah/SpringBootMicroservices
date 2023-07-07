package com.anmol.UserService.External;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "NOTIFICATION-SERVICE")
public interface NotificationClient {

    @GetMapping("/notification")
    ResponseEntity<String> getNotification();

}
