package com.moyuyo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyuyo.common.dto.review.CreateReviewRequest;
import com.moyuyo.common.dto.review.ReviewVO;

public interface ReviewService {

    Page<ReviewVO> getProductReviews(Long productId, int page, int size);

    ReviewVO createReview(Long userId, CreateReviewRequest request);

    ReviewVO getReviewDetail(Long reviewId);

    Page<ReviewVO> getUserReviews(Long userId, int page, int size);

    void deleteReview(Long reviewId, Long userId);
}
