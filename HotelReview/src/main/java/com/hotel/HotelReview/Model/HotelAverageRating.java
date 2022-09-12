package com.hotel.HotelReview.Model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HotelAverageRating {

    public String hotelId;
    public String hotelName;
    public String hotelCity;
    public String hotelCountry;
    public String hotelAddress;
    public double hotelRoomPrice;
    public double averageRating;
}
