package com.anmol.UserService;

import com.anmol.UserService.External.NotificationClient;
import com.anmol.UserService.model.User;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.print.attribute.standard.MediaSize;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/user")
@EnableFeignClients
public class UserServiceApplication {

	List<User> li = new ArrayList<>();

	{
		li.add(User.builder().id(11).email("anmol@gmail.com").name("Anmol").build());
		li.add(User.builder().id(12).email("saloni@gmail.com").name("Saloni").build());
		li.add(User.builder().id(13).email("suman@gmail.com").name("Suman").build());
		li.add(User.builder().id(14).email("rajesh@gmail.com").name("Rajesh").build());
	}

	@Autowired
	NotificationClient notificationClient;

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@GetMapping
	@CircuitBreaker(name = "notification-service",fallbackMethod = "getAllUserFallbackMethod")
	public ResponseEntity<List<User>> getAllUsers(){
		li.forEach(user -> user.setNotification(notificationClient.getNotification().getBody()));
		return ResponseEntity.ok(li);
	}

	public ResponseEntity<List<User>> getAllUserFallbackMethod(Exception ex){
		return ResponseEntity.ok(List.of());
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id") Long id){
		List<User> users = li.stream().filter(user -> user.getId() == id).toList();
		return ResponseEntity.ok(users.get(0));
	}


}
