package com.ved.reviewms.review.impl;


import com.ved.reviewms.review.Review;
import com.ved.reviewms.review.ReviewRepository;
import com.ved.reviewms.review.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;


    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<Review> getAllReviews(Long companyId) {
        return reviewRepository.findByCompanyId(companyId);
    }

    @Override
    public boolean addReview(Long companyId,Review review) {
        if (companyId != null && review!=null){
            review.setCompanyId(companyId);
            reviewRepository.save(review);
            return true;
        }
        else return false;
    }

    @Override
    public Review getReview(Long reviewId) {
      return reviewRepository.findById(reviewId).orElse(null);
    }

    @Override
    public boolean updateReview(Long reviewId, Review updatedReview) {
        Review review=reviewRepository.findById(reviewId).orElse(null);
        if(review!=null)
        {
            review.setName(updatedReview.getName());
            review.setDescription(updatedReview.getDescription());
            review.setRating(updatedReview.getRating());
            review.setCompanyId(updatedReview.getCompanyId());
            reviewRepository.save(review);
            return true;
        }
        else return false;
    }

    @Override
    public boolean deleteReview(Long reviewId) {
        Review review=reviewRepository.findById(reviewId).orElse(null);
        if(review!=null)
        {
            reviewRepository.delete(review);
            return true;
        }
        return false;
    }

}
