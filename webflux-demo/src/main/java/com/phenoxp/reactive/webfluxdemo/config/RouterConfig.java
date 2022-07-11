package com.phenoxp.reactive.webfluxdemo.config;

import com.phenoxp.reactive.webfluxdemo.exception.OperatorException;
import com.phenoxp.reactive.webfluxdemo.exception.ValidationException;
import com.phenoxp.reactive.webfluxdemo.model.ErrorOperator;
import com.phenoxp.reactive.webfluxdemo.model.ErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

@Configuration
public class RouterConfig {

  // Here is  the functional way of creating Route
  // Rather than the traditional @Controller/@GetMapping/@PostMapping/...
  private static final String FIRST_SECOND = "{first}/{second}";
  private static final String OPERATION = "x-operation";

  @Autowired
  private RequestHandler requestHandler;

  @Bean
  public RouterFunction<ServerResponse> highLevelRouting() {
    return RouterFunctions.route()
        .path("router", this::serverResponseForRouter)
        .path("calculator", this::serverResponseForCalculator)
        .build();
  }

  private RouterFunction<ServerResponse> serverResponseForRouter() {

    return RouterFunctions.route()
        .GET(
            "square/{input}",
            RequestPredicates.path("*/1?").or(RequestPredicates.path("*/20")),
            requestHandler::squareHandler)
        .GET(
            "square/{input}",
            req -> ServerResponse.badRequest().bodyValue("allowed range is 10 - 20"))
        //        .GET("square/{input}/validation", requestHandler::squareHandlerValidation) the one
        // above is the same with this
        .GET("table/{input}", requestHandler::tableHandler)
        .GET("table/{input}/stream", requestHandler::tableStreamHandler)
        .POST("multiply", requestHandler::multiplyHandler)
        .onError(ValidationException.class, exceptionHandler())
        .build();
  }

  // Calculator Method 1
  //  private RouterFunction<ServerResponse> serverResponseForCalculator() {
  //    return RouterFunctions.route()
  //        .GET(FIRST_SECOND, requestHandler::calculatorHandler)
  //        .onError(OperatorException.class, operatorHandler())
  //        .build();
  //  }

  private RouterFunction<ServerResponse> serverResponseForCalculator() {

    return RouterFunctions.route()
        .GET(FIRST_SECOND, isOperation("+"), requestHandler::addHandler)
        .GET(FIRST_SECOND, isOperation("-"), requestHandler::subtractHandler)
        .GET(FIRST_SECOND, isOperation("*"), requestHandler::multiHandler)
        .GET(FIRST_SECOND, isOperation("/"), requestHandler::divideHandler)
        .onError(OperatorException.class, operatorHandler())
        .build();
  }

  private RequestPredicate isOperation(String operation) {
    return RequestPredicates.headers(
        headers -> operation.equals(headers.asHttpHeaders().toSingleValueMap().get(OPERATION)));
  }

  private BiFunction<Throwable, ServerRequest, Mono<ServerResponse>> exceptionHandler() {
    return (err, request) -> {
      ValidationException ex = (ValidationException) err;
      var errorValidation =
          ErrorValidation.builder()
              .input(ex.getInput())
              .message(ex.getMessage())
              .errorCode(ex.ERROR_CODE)
              .build();

      return ServerResponse.badRequest().bodyValue(errorValidation);
    };
  }

  private BiFunction<Throwable, ServerRequest, Mono<ServerResponse>> operatorHandler() {
    return (err, request) -> {
      OperatorException ex = (OperatorException) err;
      var errorValidation =
          ErrorOperator.builder()
              .operator(ex.getInput())
              .message(ex.getMessage())
              .errorCode(ex.ERROR_CODE)
              .build();

      return ServerResponse.badRequest().bodyValue(errorValidation);
    };
  }
}
