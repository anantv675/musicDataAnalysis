package com.musicdataanalysis.dataanalysis;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoWriteException;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.*;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.InsertOneModel;

import com.musicdataanalysis.dataanalysis.Repositories.ItemRepository;

import org.apache.tomcat.util.json.JSONParser;
import org.bson.Document;
import static com.mongodb.client.model.Aggregates.lookup;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import static java.util.Collections.singletonList;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;

//mongodb://localhost:27017
@Component
public class PopulateMongo {

    @Autowired
    ItemRepository itemRepository;

    public static void readJsonAndInsertInDb(String fileName) throws IOException {

        com.mongodb.client.MongoClient client = MongoClients.create( "mongodb://localhost:27017");
        MongoDatabase database = client.getDatabase("musicAnalysis");
        MongoCollection<org.bson.Document> coll = database.getCollection("Musical_Instruments");

        try {

            //drop previous import
            coll.drop();

            //Bulk Approach:
            int count = 0;
            int batch = 100;
            List<InsertOneModel<Document>> docs = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader("/home/anant/Documents/musicAnalysis/json_files/Musical_Instruments.json"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    docs.add(new InsertOneModel<>(Document.parse(line)));
                    count++;
                    if (count == batch) {
                        coll.bulkWrite(docs, new BulkWriteOptions().ordered(false));
                        docs.clear();
                        count = 0;
                    }
                }
            }

            if (count > 0) {
                BulkWriteResult bulkWriteResult = coll.bulkWrite(docs, new BulkWriteOptions().ordered(false));
                System.out.println("Inserted" + bulkWriteResult);
            }

        } catch (MongoWriteException e) {
            System.out.println("Error");
        }

    }

    public static void joinCollections(){
        System.out.println("Inside joinCollections");
        String connectionString = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase db = mongoClient.getDatabase("musicAnalysis");


            MongoCollection<Document> Musical_Instruments = db.getCollection("Musical_Instruments");
            MongoCollection<Document> meta_Musical_Instruments = db.getCollection("meta_Musical_Instruments");
            MongoCollection<Document> combinedMusicData = db.getCollection("combinedMusicData");
            combinedMusicData.drop();

            //Bulk Approach:
            int count = 0;
            int batch = 10000;
            List<InsertOneModel<Document>> combinedMusicDataDocs = new ArrayList<>();

            FindIterable<Document> meta_Musical_Instruments_docs = meta_Musical_Instruments.find().noCursorTimeout(true);
            Iterator it = meta_Musical_Instruments_docs.iterator();
            while (it.hasNext()) {
                Document doc = (Document) it.next(); // primary document
                String s = doc.toJson();
                JSONObject jsonObject = new JSONObject(s);
                String asin = doc.get("asin").toString();
                BasicDBObject query1 = new BasicDBObject("asin", asin);
                FindIterable<Document> document2 = Musical_Instruments.find(query1);
                JSONArray array = new JSONArray();
                for(Document doc2 : document2) {
                    String s2 = doc2.toJson();
                    JSONObject j = new JSONObject(s2);
                    array.put(j);
                }
                jsonObject.put("Reviews", array);
                System.out.println(jsonObject);
                Document doc3 = Document.parse( jsonObject.toString());
                combinedMusicDataDocs.add(new InsertOneModel<>(doc3));
                count++;
                System.out.println("count: "+count);
                if (count == batch) {
                    System.out.println(">>>>>>>Inserting now<<<<<<<");
                    combinedMusicData.bulkWrite(combinedMusicDataDocs, new BulkWriteOptions().ordered(false));
                    combinedMusicDataDocs.clear();
                    count = 0;
                }

            }
            if (count > 0) {
                System.out.println("entering second loop");
                BulkWriteResult bulkWriteResult = combinedMusicData.bulkWrite(combinedMusicDataDocs, new BulkWriteOptions().ordered(false));
                System.out.println("Inserted" + bulkWriteResult);
            }
        }catch(Exception e){
            System.out.println("exception : "+e.getMessage());
        }




        System.exit(0);

    }

    public static void populateReviewCollection(){

        String connectionString = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase db = mongoClient.getDatabase("musicAnalysis");
            MongoCollection<Document> combinedMusicData = db.getCollection("combinedMusicData");
            MongoCollection<Document> reviews = db.getCollection("Reviews");
            reviews.drop();
            int count = 0;
            int batch = 100;
            List<InsertOneModel<Document>> reviewDocs = new ArrayList<>();

            FindIterable<Document> combinedMusicData_docs = combinedMusicData.find().noCursorTimeout(true);
            Iterator it = combinedMusicData_docs.iterator();
            while (it.hasNext()) {
                Document doc = (Document) it.next(); // primary document

                String s = doc.toJson();
                JSONObject jsonObject = new JSONObject(s);
                if(jsonObject.has("Reviews")){
                    Object reviews1 = jsonObject.get("Reviews");
                    System.out.println(reviews1);
                    JSONArray jsonArray = new JSONArray(reviews1.toString());
                    System.out.println(jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                        Document doc3 = Document.parse( jsonobject.toString());
                        reviewDocs.add(new InsertOneModel<>(doc3));
                        count++;
                        System.out.println("count: "+count);
                        if (count == batch) {
                            System.out.println("Before inserting count value : "+count+ " batch value: "+batch);
                            System.out.println(">>>>>>>Inserting now<<<<<<<");
                            reviews.bulkWrite(reviewDocs, new BulkWriteOptions().ordered(false));
                            reviewDocs.clear();
                            count = 0;
                            System.out.println("count value: "+count+ " batch value: "+batch);
                        }
                    }
                }else
                    System.out.println("reviews dont exist");
            }
            if (count > 0) {
                System.out.println("entering second loop");
                BulkWriteResult bulkWriteResult = reviews.bulkWrite(reviewDocs, new BulkWriteOptions().ordered(false));
                System.out.println("Inserted" + bulkWriteResult);
            }


        }

    }


//    private static Consumer<Document> printDocuments(MongoClient mongoClient) {
//        System.out.println("Inside printDocuments");
//        MongoDatabase db = mongoClient.getDatabase("musicAnalysis");
//        MongoCollection<Document> collection = db.getCollection("combinedMusicalData");
//        collection.drop();
//        return doc -> collection.insertOne(new Document("_id",new ObjectId()).append("rec",doc));
//    }


}

