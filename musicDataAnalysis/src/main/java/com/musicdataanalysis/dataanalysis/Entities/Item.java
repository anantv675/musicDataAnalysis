package com.musicdataanalysis.dataanalysis.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document("combinedMusicData")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Item {

    @Id
    String _id;
    String title;

    @Field("date")
    String date;

    @Field("brand")
    String brand;
    List<String> description;
    String main_cat;
    String price;
    String asin;
    List<String> feature;
    List<Reviews> Reviews;
    List<String> category;
    List<String> also_buy;

}