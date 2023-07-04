package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class HotelManagementRepository {
    Map<String,Hotel> hotelMap = new HashMap<>();
    Map<Integer,User> userMap = new HashMap<>();
    Map<String,Booking> bookingMap= new HashMap<>();
    Map<Integer,Integer> countMap = new HashMap<>();

    public String addHotel(Hotel hotel) {
        if(hotel==null || hotel.getHotelName()==null || hotelMap.containsKey(hotel.getHotelName()))return "FAILURE";
        hotelMap.put(hotel.getHotelName(),hotel);
        return "SUCCESS";
    }

    public Integer addUser(User user) {
        userMap.put(user.getaadharCardNo(),user);
        return user.getaadharCardNo();
    }

    public String getHotelWithMostFacilities() {
        int facility=0;
        String hotelName="";
        for(Hotel hotel : hotelMap.values()){
            if(hotel.getFacilities().size()>facility){
                facility=hotel.getFacilities().size();
                hotelName=hotel.getHotelName();
            }
            else if(hotel.getFacilities().size()==facility){
                if(hotel.getHotelName().compareTo(hotelName)<0){
                    hotelName=hotel.getHotelName();
                }
            }
        }
        return hotelName;
    }

    public int bookARoom(Booking booking) {
        String bookingID= UUID.randomUUID().toString();
        booking.setBookingId(bookingID);
        String hotelName =booking.getHotelName();
        Hotel hotel = hotelMap.get(hotelName);
        if(booking.getNoOfRooms()>hotel.getAvailableRooms())return -1;

        int amount = booking.getNoOfRooms()*hotel.getPricePerNight();
        booking.setAmountToBePaid(amount);

        hotel.setAvailableRooms(hotel.getAvailableRooms()-booking.getNoOfRooms());
        bookingMap.put(bookingID,booking);
        hotelMap.put(hotelName,hotel);

        int aadhar = booking.getBookingAadharCard();
        Integer bookings = countMap.get(aadhar);
        countMap.put(aadhar,(bookings==null)?1:bookings+1);
        return amount;

    }

    public int getBookings(Integer aadharCard) {
        return countMap.get(aadharCard);
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        List<Facility> old = new ArrayList<>();
        for(Facility facility:newFacilities){
            if(old.contains(facility))continue;
            else old.add(facility);
        }
        Hotel hotel= hotelMap.get(hotelName);
        hotel.setFacilities(old);
        hotelMap.put(hotelName,hotel);
        return hotel;
    }
}
