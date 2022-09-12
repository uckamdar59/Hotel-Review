package com.hotel.HotelReview.Entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Hotel {

    public String hotelId;
    public String hotelName;
    public String hotelCity;
    public String hotelCountry;
    public String hotelAddress;
    public double hotelRoomPrice;
    public int hotelRooms;

}
