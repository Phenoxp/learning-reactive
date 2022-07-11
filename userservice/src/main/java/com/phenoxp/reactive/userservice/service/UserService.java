package com.phenoxp.reactive.userservice.service;

import com.phenoxp.reactive.userservice.dto.UserDto;
import com.phenoxp.reactive.userservice.mapper.UserMapper;
import com.phenoxp.reactive.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserMapper userMapper;

  public Flux<UserDto> getAll() {
    return userRepository.findAll()
            .map(userMapper::map);
  }

  public Mono<UserDto> getUser(Integer id) {
    return userRepository.findById(id).map(userMapper::map);
  }

  public Mono<UserDto> saveUser(Mono<UserDto> userDtoMono) {
    return userDtoMono.map(userMapper::map)
            .flatMap(userRepository::save)
            .map(userMapper::map);
  }

  public Mono<UserDto> updateUser(Integer id, Mono<UserDto> userDtoMono) {
    return userRepository
        .findById(id)
        .flatMap(user -> userDtoMono.map(userMapper::map).doOnNext(u -> u.setId(id)))
        .flatMap(userRepository::save)
        .map(userMapper::map);
  }

  public Mono<Void> deleteUser(Integer id) {
    return userRepository.deleteById(id);
  }
}
