package com.hotel.HotelReview.Model;

import com.hotel.HotelReview.Entity.Hotel;
import com.hotel.HotelReview.Entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelRating {

    public Hotel hotel;
    public List<Review> reviews;
}
