package com.musicdataanalysis.dataanalysis.Services;

import com.musicdataanalysis.dataanalysis.Entities.Item;
import com.musicdataanalysis.dataanalysis.Entities.Reviews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItemService {

    Item findItemByAsin(String asin);
    void deleteItemByAsin(String asin);
    Item findReviewsByBrandAndAsin(String brand,String asin);
    Page<Item> findByBrand(String brand, Pageable pageable);

}
