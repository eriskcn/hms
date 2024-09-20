package com.example.hms.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

public class RoomPricing {
    public static BigDecimal calc(BigDecimal price, BigDecimal rate, LocalDateTime checkIn, LocalDateTime checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("checkIn and checkOut cannot be null");
        }
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("price must be greater than zero");
        }
        if (rate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("rate must be non-negative");
        }

        BigDecimal hours = BigDecimal.valueOf(calculateHoursBetween(checkIn, checkOut));

        // If hours is less than or equal to 1, charge the base price.
        if (hours.compareTo(BigDecimal.ONE) <= 0) {
            return price.setScale(2, RoundingMode.HALF_UP);
        }

        // Calculate additional hour cost based on the rate.
        BigDecimal additionalHourPrice = price.multiply(rate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal additionalHours = hours.subtract(BigDecimal.ONE).setScale(0, RoundingMode.UP); // Round up additional hours
        BigDecimal additionalCost = additionalHourPrice.multiply(additionalHours);

        // Total amount = price for the first hour + additional costs for extra hours
        BigDecimal totalAmount = price.add(additionalCost);

        // Return rounded result
        return totalAmount.setScale(2, RoundingMode.HALF_UP);
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
