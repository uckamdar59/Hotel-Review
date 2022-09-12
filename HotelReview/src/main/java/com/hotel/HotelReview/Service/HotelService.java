package com.hotel.HotelReview.Service;

import com.hotel.HotelReview.DataAccess.HotelRecordService;
import com.hotel.HotelReview.Entity.Hotel;
import com.hotel.HotelReview.Entity.Reservation;
import com.hotel.HotelReview.Entity.Review;
import com.hotel.HotelReview.Entity.User;
import com.hotel.HotelReview.Model.HotelAverageRating;
import com.hotel.HotelReview.Model.HotelRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class HotelService implements IHotelService{

    @Autowired
    HotelRecordService hotelRecordService;
    @Override
    public List<Hotel> getHotels() {
        return hotelRecordService.getHotels();
    }

    @Override
    public HotelRating getHotelById(String hotelId) {
        HotelRating hotelRating = hotelRecordService.getHotelByIdWithReviews(hotelId);
        if(hotelRating.getHotel() == null) {
            return hotelRecordService.getHotelById(hotelId);
        }
        return hotelRating;
    }

    @Override
    public String addHotel(Hotel hotel) throws Exception {
        String id = String.valueOf(UUID.randomUUID());
        hotel.setHotelId(id);
        try {
            hotelRecordService.addHotel(hotel);
            return id;
        } catch(Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Boolean updateHotel(String hotelId, Hotel hotel) throws Exception {
        try {
            hotelRecordService.updateHotel(hotelId, hotel);
            return true;
        } catch(Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public List<Review> getReviews(String hotelId) {
        return hotelRecordService.getReviews(hotelId);
    }

    @Override
    public Review getReviewById(String reviewId) {
        return hotelRecordService.getReviewById(reviewId);
    }

    @Override
    public Boolean addReview(String hotelId, Review review) throws Exception {
        String id = String.valueOf(UUID.randomUUID());
        review.setReviewId(id);
        try {
            hotelRecordService.addReview(hotelId, review);
            return true;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Boolean updateReview(String reviewId, Review review) throws Exception {
        try {
            hotelRecordService.updateReview(reviewId, review);
            return true;
        } catch(Exception e) {
            throw new Exception(e);
        }

    }

    @Override
    public List<Reservation> getReservations(String hotelId) {
        return hotelRecordService.getReservations(hotelId);
    }

    @Override
    public Boolean addReservation(String hotelId, Reservation reservation) throws Exception {
        try {
            hotelRecordService.addReservation(hotelId, reservation);
            return true;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Boolean updateReservation(String hotelId, Reservation reservation) throws Exception {
        try {
            hotelRecordService.updateReservation(hotelId, reservation);
            return true;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Boolean addUser(User user) throws Exception {
        String id = String.valueOf(UUID.randomUUID());
        user.setUserId(id);
        try {
            hotelRecordService.addUser(user);
            return true;
        } catch (Exception e) {
            throw new Exception(e);
        }

    }

    @Override
    public User getUserById(String userId) throws Exception {
        return hotelRecordService.getUserById(userId);
    }


    @Override
    public List<HotelAverageRating> searchHotels(Map<String, String> allParams) throws Exception {
//      setting default values
        String city = "Pune";
        Integer roomsRequired = 2;
        LocalDate reservationStartDate = LocalDate.now();
        LocalDate reservationEndDate = LocalDate.now();
        Integer averageRating = 3;

        if(allParams.toString().contains("city")) {
            city = allParams.get("city");
        }
        if(allParams.toString().contains("roomsRequired")) {
            roomsRequired = Integer.valueOf(allParams.get("roomsRequired"));
        }
        if(allParams.toString().contains("reservationStartDate")) {
            reservationStartDate = LocalDate.parse(allParams.get("reservationStartDate"));
        }
        if(allParams.toString().contains("reservationEndDate")) {
            reservationEndDate = LocalDate.parse(allParams.get("reservationEndDate"));
        }
        if(allParams.toString().contains("averageRating")) {
            averageRating = Integer.parseInt(allParams.get("averageRating"));
        }
        return hotelRecordService.search(city, roomsRequired, reservationStartDate, reservationEndDate, averageRating);
    }

}
