package com.hotel.HotelReview.Controller;

import com.hotel.HotelReview.Entity.Hotel;
import com.hotel.HotelReview.Service.IHotelService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ApiControllerTest {

    @Mock
    IHotelService hotelService;

    @InjectMocks
    ApiController apiController;


// Have written a sample test case using junit and mocikto
    @Test
    public void getHotelsTest(){
        when(hotelService.getHotels()).thenReturn(mockHotelResponse());
        ResponseEntity<List<Hotel>> getHotels = apiController.getHotels();
        assertEquals(getHotels.getBody().size(),1);
        assertEquals(HttpStatus.OK, getHotels.getStatusCode());
        assertEquals(getHotels.getBody().get(0).hotelRoomPrice, 233 );
    }

    private List<Hotel> mockHotelResponse() {
        Hotel hotel =  Hotel.builder().hotelId("temp")
                .hotelRooms(1)
                .hotelAddress("ddd")
                .hotelCity("Pune")
                .hotelCountry("India")
                .hotelRoomPrice(233)
                .build();
        List<Hotel> hotelList = new ArrayList<>();
        hotelList.add(hotel);
        return hotelList;
    }
}
