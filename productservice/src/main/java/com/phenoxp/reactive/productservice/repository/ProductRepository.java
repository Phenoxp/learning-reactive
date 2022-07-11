package com.phenoxp.reactive.productservice.repository;

import com.phenoxp.reactive.productservice.entity.Product;
import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

    //  min <= Price <= max
    Flux<Product> findAllByPriceLessThanEqualAndPriceGreaterThanEqual(int max, int min);

    //  min <= Price <= max
    Flux<Product> findByPriceBetween(Range<Integer> range);

    //  min < Price < max
    Flux<Product> findByPriceBetween(int min, int max);

//    findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(OffsetDateTime endDate, OffsetDateTime startDate);

}
