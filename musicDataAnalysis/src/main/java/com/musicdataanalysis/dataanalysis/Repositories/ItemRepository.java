package com.musicdataanalysis.dataanalysis.Repositories;

import com.musicdataanalysis.dataanalysis.Entities.Item;
import com.musicdataanalysis.dataanalysis.Entities.Reviews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends MongoRepository<Item,String> {

    @Query("{asin:'?0'}")
    Optional<Item> findByAsin(String asin);

    @Query("{asin:'?0'}")
    void deleteByAsin(String asin);

//    @Query(value = "{brand:'?0',asin:'?0'}",fields = "{'brand':1,'Reviews.reviewerName':1,'asin':1,'Reviews.summary':1}")
//    Item findByBrandAndAsin(String brand,String asin);
    // here, the input field is brand and the required fields in the o/p are reviewerName,asin,summary

    @Query(fields = "{'brand':1,'Reviews.reviewerName':1,'asin':1,'Reviews.summary':1}")
    Item findByBrandAndAsin(String brand,String asin);

//    Page<Item> findByBrand(String brand, Pageable pageable);
    Optional<List<Item>> findByBrand(String brand);

    public long count();

}

