package com.example.hms.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

public class RoomPricing {

    /**
     * Calculates the room price based on the duration of stay.
     *
     * @param price    The price for the first hour.
     * @param rate     The rate (percentage of price) for additional hours.
     * @param checkIn  The start time.
     * @param checkOut The end time.
     * @return The total amount to be charged.
     */
    public static BigDecimal calc(double price, double rate, LocalDateTime checkIn, LocalDateTime checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("checkIn and checkOut cannot be null");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("price must be greater than zero");
        }
        if (rate < 0) {
            throw new IllegalArgumentException("rate must be non-negative");
        }

        double hours = calculateHoursBetween(checkIn, checkOut);
        double additionalHourPrice = (rate / 100) * price; // rate percentage of the price for additional hours
        double amount;

        if (hours <= 1) {
            amount = price;
        } else {
            amount = price + (Math.ceil(hours - 1) * additionalHourPrice);
        }

        // Create a BigDecimal for precise rounding
        BigDecimal amountBD = new BigDecimal(amount);
        // Round to 2 decimal places
        return amountBD.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculates the difference in hours between two LocalDateTime instances.
     *
     * @param checkIn  The start time.
     * @param checkOut The end time.
     * @return The difference in hours.
     */
    private static double calculateHoursBetween(LocalDateTime checkIn, LocalDateTime checkOut) {
        Duration duration = Duration.between(checkIn, checkOut);
        double hours = duration.toHours();
        double minutes = duration.toMinutesPart();
        double seconds = duration.toSecondsPart();
        double minutesInHours = minutes / 60.0;
        double secondsInHours = seconds / 3600.0;

        return hours + minutesInHours + secondsInHours;
    }
}

