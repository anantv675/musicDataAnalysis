package com.musicdataanalysis.dataanalysis.Services;

import com.musicdataanalysis.dataanalysis.Entities.Reviews;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewsService {

     Reviews createReview(Reviews reviews,String asin);
     Reviews updateReview(String brand, Reviews review, String asin);
     void deleteReview(String asin);
     List<Reviews> getReviews(String brand,String asin);
}
