package com.musicdataanalysis.dataanalysis.ServiceImpl;

import com.musicdataanalysis.dataanalysis.Repositories.ReviewsRepository;
import com.musicdataanalysis.dataanalysis.Services.ReviewsService;
import com.musicdataanalysis.dataanalysis.Entities.Reviews;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ReviewServiceImpl implements ReviewsService {

    @Autowired
    private ReviewsRepository reviewsRepository;

    @Override
    public Reviews createReview(Reviews reviews, String asin) {
        return null;
    }

    @Override
    public Reviews updateReview(String brand, Reviews review, String asin) {
        return null;
    }

    @Override
    public void deleteReview(String asin) {

    }

    @Override
    public List<Reviews> getReviews(String brand, String asin) {
        return null;
    }
}
