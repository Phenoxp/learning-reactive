package com.phenoxp.reactive.webfluxdemo.service;

import com.phenoxp.reactive.webfluxdemo.model.Response;
import com.phenoxp.reactive.webfluxdemo.util.SleepUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class MathService {

  public Response square(int input) {
    return new Response(input * input);
  }

  public List<Response> table(int input) {
    return IntStream.rangeClosed(1, 10)
        .peek(i -> SleepUtil.sleepSeconds(1))
        .peek(i -> System.out.println("Math-service processing " + i))
        .mapToObj(i -> new Response(i * input))
        .collect(Collectors.toList());
  }
}
