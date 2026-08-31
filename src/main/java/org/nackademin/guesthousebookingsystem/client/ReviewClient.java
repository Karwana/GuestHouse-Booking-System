package org.nackademin.guesthousebookingsystem.client;

import lombok.RequiredArgsConstructor;
import org.nackademin.guesthousebookingsystem.dto.ReviewDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewClient {

    private final RestClient restClient;

    @Value("${review.service.url}")
    private String reviewServiceUrl;

    public List<ReviewDto> getReviewsByRoom(Long roomId) {
        try {
            ReviewDto[] reviews = restClient.get()
                    .uri(reviewServiceUrl
                            + "/api/reviews/room/"
                            + roomId)
                    .retrieve()
                    .body(ReviewDto[].class);
            return reviews != null
                    ? Arrays.asList(reviews)
                    : Collections.emptyList();
        } catch (ResourceAccessException e) {
            throw new RuntimeException(
                    "Recensionstjänsten är inte tillgänglig "
                            + "just nu, försök igen senare");
        }
    }

    public List<ReviewDto> getReviewsByCustomer(Long customerId) {
        try {
            ReviewDto[] reviews = restClient.get()
                    .uri(reviewServiceUrl
                            + "/api/reviews/customer/"
                            + customerId)
                    .retrieve()
                    .body(ReviewDto[].class);
            return reviews != null
                    ? Arrays.asList(reviews)
                    : Collections.emptyList();
        } catch (ResourceAccessException e) {
            throw new RuntimeException(
                    "Recensionstjänsten är inte tillgänglig "
                            + "just nu, försök igen senare");
        }
    }

    public ReviewDto saveReview(ReviewDto reviewDto) {
        try {
            return restClient.post()
                    .uri(reviewServiceUrl + "/api/reviews")
                    .body(reviewDto)
                    .retrieve()
                    .body(ReviewDto.class);
        } catch (ResourceAccessException e) {
            throw new RuntimeException(
                    "Recensionstjänsten är inte tillgänglig "
                            + "just nu, försök igen senare");
        }
    }

    public void deleteReview(Long id) {
        try {
            restClient.delete()
                    .uri(reviewServiceUrl + "/api/reviews/" + id)
                    .retrieve()
                    .toBodilessEntity();
        } catch (ResourceAccessException e) {
            throw new RuntimeException(
                    "Recensionstjänsten är inte tillgänglig "
                            + "just nu, försök igen senare");
        }
    }
}