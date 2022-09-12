package com.hotel.HotelReview.Entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Reservation {

    public String hotelId;

    public Date reservationDate;

    public int roomsAvailable;


}
