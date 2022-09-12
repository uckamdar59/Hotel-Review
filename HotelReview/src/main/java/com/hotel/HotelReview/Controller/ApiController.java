package com.hotel.HotelReview.Controller;

import com.hotel.HotelReview.Entity.Hotel;
import com.hotel.HotelReview.Entity.Reservation;
import com.hotel.HotelReview.Entity.Review;
import com.hotel.HotelReview.Entity.User;
import com.hotel.HotelReview.Model.HotelAverageRating;
import com.hotel.HotelReview.Model.HotelRating;
import com.hotel.HotelReview.Service.IHotelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class ApiController {

    @Autowired
    IHotelService hotelService;

    @GetMapping("hotels")
    public ResponseEntity<List<Hotel>> getHotels()  {
        log.info("Get Controller Started");
        return new ResponseEntity<>(hotelService.getHotels(), HttpStatus.OK);
    }

    @GetMapping("hotel/{hotelId}")
    public ResponseEntity<HotelRating> getHotelById(@PathVariable String hotelId) {
        log.info("Get Controller Started");
        return new ResponseEntity<>(hotelService.getHotelById(hotelId), HttpStatus.OK);
    }

    @PostMapping("hotel")
    public ResponseEntity<String> addHotel(@RequestBody Hotel hotel) throws Exception {
        log.info("Post Controller Started");
        return new ResponseEntity<>(hotelService.addHotel(hotel), HttpStatus.OK);
    }

    @PutMapping("hotel/{hotelId}")
    public ResponseEntity<Boolean> updateHotel(@PathVariable String hotelId, @RequestBody Hotel hotel) throws Exception {
        log.info("Put Controller Started");
        return new ResponseEntity<>(hotelService.updateHotel(hotelId, hotel), HttpStatus.OK);
    }

    @GetMapping("hotel/{hotelId}/reviews")
    public ResponseEntity<List<Review>> getReviews(@PathVariable String hotelId)  {
        log.info("Get Controller Started");
        return new ResponseEntity<>(hotelService.getReviews(hotelId), HttpStatus.OK);
    }

    @GetMapping("review/{reviewId}")
    public ResponseEntity<Review> getReviewById(@PathVariable String reviewId) {
        log.info("Get Controller Started");
        return new ResponseEntity<>(hotelService.getReviewById(reviewId), HttpStatus.OK);
    }

    @PostMapping("hotel/{hotelId}/review")
    public ResponseEntity<Boolean> addReview(@PathVariable String hotelId, @RequestBody Review review) throws Exception {
        log.info("Post Controller Started");
        return new ResponseEntity<>(hotelService.addReview(hotelId, review), HttpStatus.OK);
    }

    @PutMapping("review/{reviewId}")
    public ResponseEntity<Boolean> updateReview(@PathVariable String reviewId, @RequestBody Review review) throws Exception {
        log.info("Put Controller Started");
        return new ResponseEntity<>(hotelService.updateReview(reviewId, review), HttpStatus.OK);
    }

    @PostMapping("hotel/{hotelId}/reservation")
    public ResponseEntity<Boolean> addReservation(@PathVariable String hotelId, @RequestBody Reservation reservation) throws Exception {
        log.info("Post Controller Started");
        return new ResponseEntity<>(hotelService.addReservation(hotelId, reservation), HttpStatus.OK);
    }

    @PutMapping("reservation/{hotelId}")
    public ResponseEntity<Boolean> updateReservation(@PathVariable String hotelId, @RequestBody Reservation reservation) throws Exception {
        log.info("Put Controller Started");
        return new ResponseEntity<>(hotelService.updateReservation(hotelId, reservation), HttpStatus.OK);
    }

    @PostMapping("user")
    public ResponseEntity<Boolean> addUser(@RequestBody User user) throws Exception {
        log.info("Post Controller Started");
        return new ResponseEntity<>(hotelService.addUser(user), HttpStatus.OK);
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) throws Exception {
        log.info("Get Controller Started");
        return new ResponseEntity<>(hotelService.getUserById(userId), HttpStatus.OK);
    }


    @GetMapping("hotel/search")
    public ResponseEntity<List<HotelAverageRating>> searchHotels(@RequestParam Map<String,String> allParams) throws Exception {
        log.info("Get Controller Started");
        return new ResponseEntity<>(hotelService.searchHotels(allParams), HttpStatus.OK);
    }
}
