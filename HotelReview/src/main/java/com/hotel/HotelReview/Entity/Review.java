package com.hotel.HotelReview.Entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Review {

    public String reviewId;
    public String hotelId;
    public String userId;
    public int rating;
    public String comments;

}
