package com.hotel.HotelReview.DataAccess;

import com.hotel.HotelReview.Entity.Hotel;
import com.hotel.HotelReview.Entity.Reservation;
import com.hotel.HotelReview.Entity.Review;
import com.hotel.HotelReview.Entity.User;
import com.hotel.HotelReview.Model.HotelAverageRating;
import com.hotel.HotelReview.Model.HotelRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;



@Service
@PropertySource("classpath:database.properties")
public class HotelRecordService {

    protected enum ColumnName {
        HOTEL_ID("hotelId"),
        HOTEL_NAME("hotelName"),
        HOTEL_CITY("hotelCity"),
        HOTEL_COUNTRY("hotelCountry"),
        HOTEL_ADDRESS("hotelAddress"),
        HOTEL_ROOMS("hotelRooms"),
        HOTEL_ROOM_PRICE("hotelRoomPrice"),
        REVIEW_ID("reviewId"),

        USER_ID("userId"),
        RATING("rating"),
        COMMENTS("comments"),
        ROOMS_AVAILABLE("roomsAvailable"),
        RESERVATION_DATE("reservationDate"),
        USERNAME("username"),
        START_DATE("startDate"),
        END_DATE("endDate"),
        AVERAGE_RATING("averageRating"),

        ROOMS_REQUIRED("roomsRequired");

        private String name;

        ColumnName(String columnName) {
            this.name = columnName;
        }

        protected String getColumnName() {
            return this.name;
        }
        }

    protected JdbcTemplate jdbcTemplate;
    @Autowired
    private Environment queries;
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    protected DataSource dataSource;

    int defaultFetchSize = 100;

    /**
     * Set the data source
     *
     * @param dataSource the JDBC data source
     */
    @Value("#{dataSource}")
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcTemplate.setFetchSize(defaultFetchSize);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.dataSource = dataSource;
    }

    /**
     * Returns the sql query from the properties object by name.
     *
     * @param key the properties key corresponding to the sql statement
     */
    protected String sql(String key) {
        return queries.getProperty(key);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<Hotel> getHotels() {
        MapSqlParameterSource params = new MapSqlParameterSource();

        return namedParameterJdbcTemplate.query(sql("get_all_hotels"), params, new HotelRecordMapper());
    }

    static class HotelRecordMapper implements ResultSetExtractor<List<Hotel>> {

        @Override
        public List<Hotel> extractData(ResultSet rs) throws SQLException {

            List<Hotel> hotelList = new ArrayList<>();
            while (rs.next()) {
                Hotel hotel = Hotel.builder()
                        .hotelId(rs.getString(ColumnName.HOTEL_ID.getColumnName()))
                        .hotelName(rs.getString(ColumnName.HOTEL_NAME.getColumnName()))
                        .hotelCity(rs.getString(ColumnName.HOTEL_CITY.getColumnName()))
                        .hotelCountry(rs.getString(ColumnName.HOTEL_COUNTRY.getColumnName()))
                        .hotelAddress(rs.getString(ColumnName.HOTEL_ADDRESS.getColumnName()))
                        .hotelRooms(rs.getInt(ColumnName.HOTEL_ROOMS.getColumnName()))
                        .hotelRoomPrice(rs.getDouble(ColumnName.HOTEL_ROOM_PRICE.getColumnName())).build();
                hotelList.add(hotel);
            }
            return hotelList;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HotelRating getHotelByIdWithReviews(String hotelId) {
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue(ColumnName.HOTEL_ID.getColumnName(), hotelId);

        return namedParameterJdbcTemplate.query(sql("get_hotel_by_id_with_reviews"), source, new HotelRatingRecordMapper());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HotelRating getHotelById(String hotelId) {
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue(ColumnName.HOTEL_ID.getColumnName(), hotelId);

        SqlRowSet rs = namedParameterJdbcTemplate.queryForRowSet(sql("get_hotel_by_id"), source);
        Hotel hotel = null;
        if (rs.next()) {
                hotel = Hotel.builder()
                    .hotelId(rs.getString(ColumnName.HOTEL_ID.getColumnName()))
                    .hotelName(rs.getString(ColumnName.HOTEL_NAME.getColumnName()))
                    .hotelCity(rs.getString(ColumnName.HOTEL_CITY.getColumnName()))
                    .hotelCountry(rs.getString(ColumnName.HOTEL_COUNTRY.getColumnName()))
                    .hotelAddress(rs.getString(ColumnName.HOTEL_ADDRESS.getColumnName()))
                    .hotelRooms(rs.getInt(ColumnName.HOTEL_ROOMS.getColumnName()))
                    .hotelRoomPrice(rs.getDouble(ColumnName.HOTEL_ROOM_PRICE.getColumnName())).build();
        }

        return new HotelRating(hotel, new ArrayList<>());
    }


    static class HotelRatingRecordMapper implements ResultSetExtractor<HotelRating> {

        @Override
        public HotelRating extractData(ResultSet rs) throws SQLException {

            List<Review> reviewList = new ArrayList<>();
            Hotel hotel = null;
            while (rs.next()) {
                hotel = Hotel.builder()
                        .hotelId(rs.getString(ColumnName.HOTEL_ID.getColumnName()))
                        .hotelName(rs.getString(ColumnName.HOTEL_NAME.getColumnName()))
                        .hotelCity(rs.getString(ColumnName.HOTEL_CITY.getColumnName()))
                        .hotelCountry(rs.getString(ColumnName.HOTEL_COUNTRY.getColumnName()))
                        .hotelAddress(rs.getString(ColumnName.HOTEL_ADDRESS.getColumnName()))
                        .hotelRooms(rs.getInt(ColumnName.HOTEL_ROOMS.getColumnName()))
                        .hotelRoomPrice(rs.getDouble(ColumnName.HOTEL_ROOM_PRICE.getColumnName())).build();

                reviewList.add(Review.builder()
                        .reviewId(rs.getString(ColumnName.REVIEW_ID.getColumnName()))
                        .hotelId(rs.getString(ColumnName.HOTEL_ID.getColumnName()))
                        .userId(rs.getString(ColumnName.USER_ID.getColumnName()))
                        .rating(rs.getInt(ColumnName.RATING.getColumnName()))
                        .comments(rs.getString(ColumnName.COMMENTS.getColumnName())).build());

            }

            return new HotelRating(hotel, reviewList);
        }
    }

        @Transactional(propagation = Propagation.REQUIRED)
        public void addHotel(Hotel hotel) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(ColumnName.HOTEL_ID.getColumnName(), hotel.hotelId);
            params.addValue(ColumnName.HOTEL_NAME.getColumnName(), hotel.hotelName);
            params.addValue(ColumnName.HOTEL_CITY.getColumnName(), hotel.hotelCity);
            params.addValue(ColumnName.HOTEL_COUNTRY.getColumnName(), hotel.hotelCountry);
            params.addValue(ColumnName.HOTEL_ADDRESS.getColumnName(), hotel.hotelAddress);
            params.addValue(ColumnName.HOTEL_ROOMS.getColumnName(), hotel.hotelRooms);
            params.addValue(ColumnName.HOTEL_ROOM_PRICE.getColumnName(), hotel.hotelRoomPrice);

            namedParameterJdbcTemplate.update(sql("insert_hotel"), params);
        }

        @Transactional(propagation = Propagation.REQUIRED)
        public void updateHotel(String hotelId, Hotel hotel) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(ColumnName.HOTEL_ID.getColumnName(), hotelId);
            params.addValue(ColumnName.HOTEL_NAME.getColumnName(), hotel.hotelName);
            params.addValue(ColumnName.HOTEL_CITY.getColumnName(), hotel.hotelCity);
            params.addValue(ColumnName.HOTEL_COUNTRY.getColumnName(), hotel.hotelCountry);
            params.addValue(ColumnName.HOTEL_ADDRESS.getColumnName(), hotel.hotelAddress);
            params.addValue(ColumnName.HOTEL_ROOMS.getColumnName(), hotel.hotelRooms);
            params.addValue(ColumnName.HOTEL_ROOM_PRICE.getColumnName(), hotel.hotelRoomPrice);

            namedParameterJdbcTemplate.update(sql("update_hotel"), params);
        }


        @Transactional(propagation = Propagation.REQUIRED)
        public List<Review> getReviews(String hotelId) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(ColumnName.HOTEL_ID.getColumnName(), hotelId);

            return namedParameterJdbcTemplate.query(sql("get_review_by_hotel_id"), params, new ReviewRecordMapper());
        }

        static class ReviewRecordMapper implements ResultSetExtractor<List<Review>> {

            @Override
            public List<Review> extractData(ResultSet rs) throws SQLException {

                List<Review> reviewList = new ArrayList<>();
                while (rs.next()) {
                    Review review = Review.builder()
                            .reviewId(rs.getString(ColumnName.REVIEW_ID.getColumnName()))
                            .hotelId(rs.getString(ColumnName.HOTEL_ID.getColumnName()))
                            .userId(rs.getString(ColumnName.USER_ID.getColumnName()))
                            .rating(rs.getInt(ColumnName.RATING.getColumnName()))
                            .comments(rs.getString(ColumnName.COMMENTS.getColumnName())).build();
                    reviewList.add(review);
                }
                return reviewList;
            }
        }

        @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
        public Review getReviewById(String reviewId) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(ColumnName.REVIEW_ID.getColumnName(), reviewId);

            SqlRowSet rs = namedParameterJdbcTemplate.queryForRowSet(sql("get_review_by_id"), source);
            if (rs.next()) {
                return Review.builder()
                        .reviewId(rs.getString(ColumnName.REVIEW_ID.getColumnName()))
                        .hotelId(rs.getString(ColumnName.HOTEL_ID.getColumnName()))
                        .userId(rs.getString(ColumnName.USER_ID.getColumnName()))
                        .rating(rs.getInt(ColumnName.RATING.getColumnName()))
                        .comments(rs.getString(ColumnName.COMMENTS.getColumnName())).build();

            }

            return null;
        }

        @Transactional(propagation = Propagation.REQUIRED)
        public void addReview(String hotelId, Review review) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(ColumnName.REVIEW_ID.getColumnName(), review.reviewId);
            params.addValue(ColumnName.HOTEL_ID.getColumnName(), hotelId);
            params.addValue(ColumnName.RATING.getColumnName(), review.rating);
            params.addValue(ColumnName.COMMENTS.getColumnName(), review.comments);
            params.addValue(ColumnName.USER_ID.getColumnName(), review.userId);

            namedParameterJdbcTemplate.update(sql("insert_review"), params);
        }

        @Transactional(propagation = Propagation.REQUIRED)
        public void updateReview(String reviewId, Review review) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(ColumnName.REVIEW_ID.getColumnName(), reviewId);
            params.addValue(ColumnName.RATING.getColumnName(), review.rating);
            params.addValue(ColumnName.COMMENTS.getColumnName(), review.comments);

            namedParameterJdbcTemplate.update(sql("update_review"), params);
        }


        @Transactional(propagation = Propagation.REQUIRED)
        public List<Reservation> getReservations(String hotelId) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(ColumnName.HOTEL_ID.getColumnName(), hotelId);

            return namedParameterJdbcTemplate.query(sql("get_reservation_by_hotel_id"), params, new ReservationRecordMapper());
        }

        static class ReservationRecordMapper implements ResultSetExtractor<List<Reservation>> {

            @Override
            public List<Reservation> extractData(ResultSet rs) throws SQLException {

                List<Reservation> reservationList = new ArrayList<>();
                while (rs.next()) {
                    Reservation reservation = Reservation.builder()
                            .hotelId(rs.getString(ColumnName.HOTEL_ID.getColumnName()))
                            .reservationDate(rs.getDate(ColumnName.RESERVATION_DATE.getColumnName()))
                            .roomsAvailable(rs.getInt(ColumnName.ROOMS_AVAILABLE.getColumnName())).build();
                    reservationList.add(reservation);
                }
                return reservationList;
            }
        }

        @Transactional(propagation = Propagation.REQUIRED)
        public void updateReservation(String hotelId, Reservation reservation) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(ColumnName.ROOMS_AVAILABLE.getColumnName(), reservation.roomsAvailable);
            params.addValue(ColumnName.HOTEL_ID.getColumnName(), hotelId);
            params.addValue(ColumnName.RESERVATION_DATE.getColumnName(), reservation.reservationDate);

            namedParameterJdbcTemplate.update(sql("update_reservation"), params);
        }

        @Transactional(propagation = Propagation.REQUIRED)
        public void addReservation(String hotelId, Reservation reservation) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(ColumnName.ROOMS_AVAILABLE.getColumnName(), reservation.roomsAvailable);
            params.addValue(ColumnName.HOTEL_ID.getColumnName(), hotelId);
            params.addValue(ColumnName.RESERVATION_DATE.getColumnName(), reservation.reservationDate);

            namedParameterJdbcTemplate.update(sql("insert_reservation"), params);
        }


        @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
        public User getUserById(String userId) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(ColumnName.USER_ID.getColumnName(), userId);

            SqlRowSet rs = namedParameterJdbcTemplate.queryForRowSet(sql("get_user_by_id"), source);
            if (rs.next()) {
                return User.builder()
                        .userId(rs.getString(ColumnName.USER_ID.getColumnName()))
                        .username(rs.getString(ColumnName.USERNAME.getColumnName())).build();

            }

            return null;
        }

        @Transactional(propagation = Propagation.REQUIRED)
        public void addUser(User user) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(ColumnName.USER_ID.getColumnName(), user.userId);
            params.addValue(ColumnName.USERNAME.getColumnName(), user.username);

            namedParameterJdbcTemplate.update(sql("insert_user"), params);
        }

        @Transactional(propagation = Propagation.REQUIRED)
        public List<HotelAverageRating> search(String city, Integer roomsRequired, LocalDate reservationStartDate,
                                               LocalDate reservationEndDate, Integer averageRating) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(ColumnName.HOTEL_CITY.getColumnName(), city);
            params.addValue(ColumnName.ROOMS_REQUIRED.getColumnName(), roomsRequired);
            params.addValue(ColumnName.START_DATE.getColumnName(), reservationStartDate);
            params.addValue(ColumnName.END_DATE.getColumnName(), reservationEndDate);
            params.addValue(ColumnName.AVERAGE_RATING.getColumnName(), averageRating);

            return namedParameterJdbcTemplate.query(sql("search_hotel"), params, new HotelAverageRatingRecordMapper());
        }

        static class HotelAverageRatingRecordMapper implements ResultSetExtractor<List<HotelAverageRating>> {

            @Override
            public List<HotelAverageRating> extractData(ResultSet rs) throws SQLException {

                List<HotelAverageRating> hotelList = new ArrayList<>();
                while (rs.next()) {
                    HotelAverageRating hotelAverageRating = HotelAverageRating.builder()
                            .hotelId(rs.getString(ColumnName.HOTEL_ID.getColumnName()))
                            .hotelName(rs.getString(ColumnName.HOTEL_NAME.getColumnName()))
                            .hotelCity(rs.getString(ColumnName.HOTEL_CITY.getColumnName()))
                            .hotelCountry(rs.getString(ColumnName.HOTEL_COUNTRY.getColumnName()))
                            .hotelAddress(rs.getString(ColumnName.HOTEL_ADDRESS.getColumnName()))
                            .hotelRoomPrice(rs.getDouble(ColumnName.HOTEL_ROOM_PRICE.getColumnName()))
                            .averageRating(rs.getDouble(ColumnName.AVERAGE_RATING.getColumnName())).build();
                    hotelList.add(hotelAverageRating);
                }
                return hotelList;
            }
        }
    }
