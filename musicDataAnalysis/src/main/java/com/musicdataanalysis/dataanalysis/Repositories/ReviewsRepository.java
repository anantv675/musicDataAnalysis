package com.musicdataanalysis.dataanalysis.Repositories;

import com.musicdataanalysis.dataanalysis.Entities.Reviews;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewsRepository extends MongoRepository<Reviews,String> {

}
