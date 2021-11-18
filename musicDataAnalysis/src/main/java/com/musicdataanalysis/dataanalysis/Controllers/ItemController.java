package com.musicdataanalysis.dataanalysis.Controllers;

import com.musicdataanalysis.dataanalysis.Entities.Item;
import com.musicdataanalysis.dataanalysis.Entities.Reviews;
import com.musicdataanalysis.dataanalysis.ServiceImpl.ItemServiceImpl;
import com.musicdataanalysis.dataanalysis.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
public class ItemController {

    @Autowired
    private ItemServiceImpl itemService;

    @GetMapping("/getItemByAsin/{asin}")
    public ResponseEntity<Item> getItemByAsin(@PathVariable String asin){
        Item itemByAsin = null;
        try{
            itemByAsin = itemService.findItemByAsin(asin);
        }catch (Exception e){
            throw new ResourceNotFoundException("Could not find any item with id = " + asin);
        }
        return new ResponseEntity<>(itemByAsin, HttpStatus.OK);
    }

//    @GetMapping("getAllItemsByBrand/{brand}")
//    public ResponseEntity<Map<String, Object>> getAllItemsByBrand(@PathVariable String brand,
//                                                                  @RequestParam(defaultValue = "0") int page,
//                                                                  @RequestParam(defaultValue = "5") int size){
//
//        Map<String, Object> response = new HashMap<>();
//        try{
//            List<Item> itemsByBrand;
//            Pageable paging = PageRequest.of(page, size);
//            Page<Item> pageItems;
//            pageItems = itemService.findByBrand(brand,paging);
//
//            itemsByBrand = pageItems.getContent();
//
//            response.put("Items", itemsByBrand);
//            response.put("pageIndex", pageItems.getNumber());
//            response.put("pageSize", pageItems.getTotalElements());
//            response.put("pageCount", pageItems.getTotalPages());
//
//        }catch (Exception e){
//            throw new ResourceNotFoundException("Could not find any item of brand = " + brand);
//        }
//
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

    @GetMapping("getAllItemsByBrand/{brand}")
    public ResponseEntity<List<Item>> getAllItemsByBrand(@PathVariable String brand){
        List<Item> byBrand = null;
        try{
            byBrand = itemService.findByBrand(brand);
        }catch (Exception e){
            throw new ResourceNotFoundException("Could not find any item of brand = " + brand);
        }

        return new ResponseEntity<>(byBrand, HttpStatus.OK);
    }

//    public Reviews getItemReviews(){
//
//    }

}
