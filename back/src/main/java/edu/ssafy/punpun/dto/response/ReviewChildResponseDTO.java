package edu.ssafy.punpun.dto.response;

import edu.ssafy.punpun.entity.Keyword;
import edu.ssafy.punpun.entity.Review;
import edu.ssafy.punpun.entity.ReviewKeyword;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ReviewChildResponseDTO {
    private Long reviewId;
    private String reviewContent;
    private List<Keyword> keywords;

    public static ReviewChildResponseDTO entityToDto(Review review) {
        Long reviewId = review.getId();
        String reviewContent = review.getContent();
        List<Keyword> keywords = review.getReviewKeywords().stream()
                .map(ReviewKeyword::getKeyword)
                .collect(Collectors.toList());

        return new ReviewChildResponseDTO(reviewId, reviewContent, keywords);
    }
}
