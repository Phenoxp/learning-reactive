package com.phenoxp.reactive.webfluxdemo.controller;

import com.phenoxp.reactive.webfluxdemo.model.Response;
import com.phenoxp.reactive.webfluxdemo.service.MathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("math")
public class MathController {

  @Autowired
  private MathService mathService;

  @GetMapping("square/{input}")
  public Response square(@PathVariable int input) {
    return mathService.square(input);
  }

  @GetMapping("table/{input}")
  public List<Response> table(@PathVariable int input) {
    return mathService.table(input);
  }
}
