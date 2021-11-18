package com.musicdataanalysis.dataanalysis.ServiceImpl;

import com.musicdataanalysis.dataanalysis.Repositories.ItemRepository;
import com.musicdataanalysis.dataanalysis.Services.ItemService;
import com.musicdataanalysis.dataanalysis.Entities.Item;
import com.musicdataanalysis.dataanalysis.exceptions.CustomItemException;
import com.musicdataanalysis.dataanalysis.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemRepository itemRepository;

    @Override
    public Item findItemByAsin(String asin) {
        Optional<Item> item = itemRepository.findByAsin(asin);
        return item.get();
    }

    @Override
    public void deleteItemByAsin(String asin) {
        itemRepository.deleteByAsin(asin);
    }

    @Override
    public Item findReviewsByBrandAndAsin(String brand, String asin) {
        return itemRepository.findByBrandAndAsin(brand,asin);
    }

    @Override
    public List<Item> findByBrand(String brand) {
        Optional<List<Item>> byBrand = itemRepository.findByBrand(brand);
        return byBrand.get();
    }
//    @Override
//    public Page<Item> findByBrand(String brand, Pageable pageable) {
//        return itemRepository.findByBrand(brand,pageable);
//    }


}
