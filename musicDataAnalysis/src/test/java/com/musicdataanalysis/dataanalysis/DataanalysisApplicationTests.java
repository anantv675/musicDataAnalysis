package com.musicdataanalysis.dataanalysis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.musicdataanalysis.dataanalysis.Controllers.ItemController;
import com.musicdataanalysis.dataanalysis.Entities.Reviews;
import com.musicdataanalysis.dataanalysis.Repositories.ItemRepository;
import com.musicdataanalysis.dataanalysis.Entities.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class DataanalysisApplicationTests {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemController itemController;

    @Test
    public void getData(){
        Optional<Item> item = itemRepository.findByAsin("0000098906");
        if(item.isPresent()){
            System.out.println(item.get().getBrand());
        }else{
            System.out.println("No item found");
        }

    }

    @Test
    public void getItemThroughController(){
        ResponseEntity<Item> itemByAsin = itemController.getItemByAsin("0000098906");
        System.out.println(itemByAsin.getBody().getBrand());
    }

    @Test
    public void getItemData() throws JsonProcessingException {
        Item item = itemRepository.findByBrandAndAsin("Yamaha", "B000222DX8");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(item);
        System.out.println(json);

//        System.out.println(item);
    }

}
