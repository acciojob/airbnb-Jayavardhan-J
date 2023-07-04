package com.driver.controllers;

import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import com.driver.model.Booking;

import java.util.*;

public class HotelManagementRepository {


    private Map<String,Hotel> hotelDB = new HashMap<>();
    private Map<Integer,User> userDB = new HashMap<>();

    private HashMap<String,Booking> bookingDb = new HashMap<>();

    private HashMap<Integer,Integer> countOfBookings = new HashMap<>();
    public String addHotel(Hotel hotel) {
        if(hotelDB.containsKey(hotel.getHotelName()) || hotel==null || hotel.getHotelName()==null)return "FAILURE";
        hotelDB.put(hotel.getHotelName(),hotel);
        return "SUCCESS";
    }

    public Integer addUser(User user) {
        userDB.put(user.getaadharCardNo(), user);
        return user.getaadharCardNo();
    }

    public String getHotelWithMostFacilities() {
        int facilities= 0;

        String hotelName = "";

        for(Hotel hotel:hotelDB.values()){

            if(hotel.getFacilities().size()>facilities){
                facilities = hotel.getFacilities().size();
                hotelName = hotel.getHotelName();
            }
            else if(hotel.getFacilities().size()==facilities){
                if(hotel.getHotelName().compareTo(hotelName)<0){
                    hotelName = hotel.getHotelName();
                }
            }
        }
        return hotelName;
    }
    public int bookARoom(Booking booking){
        String key = UUID.randomUUID().toString();

        booking.setBookingId(key);

        String hotelName = booking.getHotelName();

        Hotel hotel = hotelDB.get(hotelName);

        int availableRooms = hotel.getAvailableRooms();

        if(availableRooms<booking.getNoOfRooms()){
            return -1;
        }

        int amountToBePaid = hotel.getPricePerNight()*booking.getNoOfRooms();
        booking.setAmountToBePaid(amountToBePaid);

        //Make sure we check this part of code as well
        hotel.setAvailableRooms(hotel.getAvailableRooms()-booking.getNoOfRooms());

        bookingDb.put(key,booking);

        hotelDB.put(hotelName,hotel);

        int aadharCard = booking.getBookingAadharCard();
        Integer currentBookings = countOfBookings.get(aadharCard);
        countOfBookings.put(aadharCard, Objects.nonNull(currentBookings)?1+currentBookings:1);
        return amountToBePaid;
    }
    public int getBooking(Integer adharCard){
        return countOfBookings.get(adharCard);
    }

    public Hotel updateFacility(List<Facility> newFacilities, String hotelName){
        List<Facility> oldFacilities = hotelDB.get(hotelName).getFacilities();

        for(Facility facility: newFacilities){

            if(oldFacilities.contains(facility)){
                continue;
            }else{
                oldFacilities.add(facility);
            }
        }

        Hotel hotel = hotelDB.get(hotelName);
        hotel.setFacilities(oldFacilities);

        hotelDB.put(hotelName,hotel);

        return hotel;
    }
}
