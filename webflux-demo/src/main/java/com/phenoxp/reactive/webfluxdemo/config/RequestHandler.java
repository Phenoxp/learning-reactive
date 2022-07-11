package com.phenoxp.reactive.webfluxdemo.config;

import com.phenoxp.reactive.webfluxdemo.exception.OperatorException;
import com.phenoxp.reactive.webfluxdemo.exception.ValidationException;
import com.phenoxp.reactive.webfluxdemo.model.Request;
import com.phenoxp.reactive.webfluxdemo.model.Response;
import com.phenoxp.reactive.webfluxdemo.service.ReactiveMathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

@Service
public class RequestHandler {
  private static final String INPUT = "input";

  @Autowired
  private ReactiveMathService service;

  public Mono<ServerResponse> squareHandler(ServerRequest serverRequest) {
    int input = Integer.parseInt(serverRequest.pathVariable(INPUT));
    Mono<Response> monoResponse = service.square(input);

    return ServerResponse.ok().body(monoResponse, Response.class);
  }

  public Mono<ServerResponse> squareHandlerValidation(ServerRequest serverRequest) {
    int input = Integer.parseInt(serverRequest.pathVariable(INPUT));
    if (input < 10 || input > 20) {
      return Mono.error(new ValidationException(input));
    }

    Mono<Response> monoResponse = service.square(input);

    return ServerResponse.ok().body(monoResponse, Response.class);
  }

  public Mono<ServerResponse> tableHandler(ServerRequest serverRequest) {
    int input = Integer.parseInt(serverRequest.pathVariable(INPUT));
    Flux<Response> responseFlux = service.table(input);

    return ServerResponse.ok().body(responseFlux, Response.class);
  }

  public Mono<ServerResponse> tableStreamHandler(ServerRequest serverRequest) {
    int input = Integer.parseInt(serverRequest.pathVariable(INPUT));
    Flux<Response> responseFlux = service.table(input);

    return ServerResponse.ok()
        .contentType(MediaType.TEXT_EVENT_STREAM)
        .body(responseFlux, Response.class);
  }

  public Mono<ServerResponse> multiplyHandler(ServerRequest serverRequest) {
    Mono<Request> requestMono = serverRequest.bodyToMono(Request.class);
    Mono<Response> responseMono = service.multiply(requestMono);

    return ServerResponse.ok().body(responseMono, Response.class);
  }

  public Mono<ServerResponse> addHandler(ServerRequest serverRequest) {
    return process(serverRequest, (a, b) -> ServerResponse.ok().bodyValue(a + b));
  }

  public Mono<ServerResponse> subtractHandler(ServerRequest serverRequest) {
    return process(serverRequest, (a, b) -> ServerResponse.ok().bodyValue(a - b));
  }

  public Mono<ServerResponse> multiHandler(ServerRequest serverRequest) {
    return process(serverRequest, (a, b) -> ServerResponse.ok().bodyValue(a * b));
  }

  public Mono<ServerResponse> divideHandler(ServerRequest serverRequest) {
    return process(
        serverRequest,
        (a, b) ->
            b == 0
                ? Mono.error(new OperatorException("0 as second value for / "))
                : ServerResponse.ok().bodyValue(a + b));
  }

  private Mono<ServerResponse> process(
      ServerRequest serverRequest, BiFunction<Integer, Integer, Mono<ServerResponse>> operator) {

    int first = getValue(serverRequest, "first");
    int second = getValue(serverRequest, "second");

    return operator.apply(first, second);
  }

  // Calculator Method 1
  //  public Mono<ServerResponse> calculatorHandler(ServerRequest serverRequest) {
  //    Request request =
  //        Request.builder()
  //            .first(getValue(serverRequest, "first"))
  //            .second(getValue(serverRequest, "second"))
  //            .build();
  //    String header = serverRequest.headers().header(OPERATION).get(0);
  //
  //    if ("*".equals(header)) {
  //      return ServerResponse.ok().body(service.multiply(Mono.just(request)), Response.class);
  //    } else if ("/".equals(header)) {
  //      return request.getSecond() == 0
  //          ? Mono.error(new OperatorException("0 as second value for / "))
  //          : ServerResponse.ok().body(service.divide(Mono.just(request)), Response.class);
  //    } else if ("-".equals(header)) {
  //      return ServerResponse.ok().body(service.subtract(Mono.just(request)), Response.class);
  //    } else if ("+".equals(header)) {
  //      return ServerResponse.ok().body(service.add(Mono.just(request)), Response.class);
  //    } else {
  //      return Mono.error(new OperatorException(header));
  //    }
  //  }

  private int getValue(ServerRequest serverRequest, String key) {
    return Integer.parseInt(serverRequest.pathVariable(key));
  }
}
