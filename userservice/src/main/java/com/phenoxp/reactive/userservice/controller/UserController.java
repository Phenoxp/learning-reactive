package com.phenoxp.reactive.userservice.controller;

import com.phenoxp.reactive.userservice.dto.UserDto;
import com.phenoxp.reactive.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("user")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping
  public Flux<UserDto> getUsers() {
    return userService.getAll();
  }

  @GetMapping("{id}")
  public Mono<ResponseEntity<UserDto>> getUser(@PathVariable Integer id) {
    return userService
        .getUser(id)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Mono<UserDto> saveUser(@RequestBody Mono<UserDto> userDtoMono) {
    return userService.saveUser(userDtoMono);
  }

  @PutMapping("{id}")
  public Mono<ResponseEntity<UserDto>> updateUser(
      @PathVariable Integer id, @RequestBody Mono<UserDto> userDtoMono) {
    return userService
        .updateUser(id, userDtoMono)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @DeleteMapping("{id}")
  public Mono<Void> deleteUser(@PathVariable Integer id) {
    return userService.deleteUser(id);
  }
}
