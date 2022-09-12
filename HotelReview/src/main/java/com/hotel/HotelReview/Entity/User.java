package com.hotel.HotelReview.Entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    public String userId;
    public String username;
}
