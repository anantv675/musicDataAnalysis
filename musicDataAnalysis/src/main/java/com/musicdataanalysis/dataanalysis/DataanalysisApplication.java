package com.musicdataanalysis.dataanalysis;

import com.musicdataanalysis.dataanalysis.Repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class DataanalysisApplication {


    public static void main(String[] args) throws IOException {

        SpringApplication.run(DataanalysisApplication.class, args);
//        String fileName1 = "D:\\musicDataAnalysis\\Musical_Instruments.json";
//        String fileName2 = "D:\\musicDataAnalysis\\meta_Musical_Instruments.json";
////        PopulateMongo.readJsonAndInsertInDb(fileName2);
////        PopulateMongo.joinCollections();
//        PopulateMongo.populateReviewCollection();


    }

}
