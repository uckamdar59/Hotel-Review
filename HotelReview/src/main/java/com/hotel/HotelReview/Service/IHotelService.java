package com.hotel.HotelReview.Service;

import com.hotel.HotelReview.Entity.Hotel;
import com.hotel.HotelReview.Entity.Reservation;
import com.hotel.HotelReview.Entity.Review;
import com.hotel.HotelReview.Entity.User;
import com.hotel.HotelReview.Model.HotelAverageRating;
import com.hotel.HotelReview.Model.HotelRating;

import java.util.List;
import java.util.Map;

public interface IHotelService {

    public List<Hotel> getHotels();

    public HotelRating getHotelById(String hotelId);

    public String addHotel(Hotel hotel) throws Exception;

    public Boolean updateHotel(String hotelId, Hotel hotel) throws Exception;

    List<Review> getReviews(String hotelId);

    public Review getReviewById(String reviewId);

    Boolean addReview(String hotelId, Review review) throws Exception;

    public Boolean updateReview(String reviewId, Review review) throws Exception;

    public List<Reservation> getReservations(String hotelId);

    public Boolean addReservation(String hotelId, Reservation reservation) throws Exception;

    public Boolean updateReservation(String hotelId, Reservation reservation) throws Exception;

    public Boolean addUser(User user) throws Exception;

    public User getUserById(String userId) throws Exception;

    public List<HotelAverageRating> searchHotels(Map<String, String> allParams) throws Exception;
}
