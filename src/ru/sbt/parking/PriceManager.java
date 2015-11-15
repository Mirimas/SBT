package ru.sbt.parking;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * PriceManager for price calculations.
 *
 * @author Anton Kildishev
 */
public class PriceManager {

	/**
	 * Price per hour.
	 */
	private final BigDecimal hourPrice;
	
	/**
	 * Full day price.
	 */
	private final BigDecimal fullDayPrice;

	/**
	 * PriceManager.
	 * 
	 * @param hourPrice the price for hour per one car
	 */
	public PriceManager(BigDecimal hourPrice) {
		this.hourPrice = hourPrice;
		this.fullDayPrice = calculateDay(0, 24);
	}

	/**
	 * Calculate price for a car.
	 *
	 * @param car the car
	 * @return the price
	 */
	public BigDecimal calculatePrice(Car car) {
		int timeIn = car.getTimeIn();
		int timeOut = car.getTimeOut();
		
		// Count days, e.g. in=0 and out=25 is 1 day
		int days = (timeOut/24 - timeIn/24);
		
		// Get hours for entry and exit days. It's between 0 and 24
		int enterHour = timeIn%24;
		int exitHour = timeOut%24;

		// Calculate price based on days count
		switch(days) {
			case 0: {
				// Same day case, e.g. in=0 and out=5
				return calculateDay(enterHour, exitHour);
			}
			case 1: {
				// Two days case, e.g. in=22 and out=26
				BigDecimal calculateFirstDay = calculateDay(enterHour, 24);
				BigDecimal calculateLastDay = calculateDay(0, exitHour);
				
				return calculateFirstDay.add(calculateLastDay);
			}
			default: {
				// More then two days case, e.g. in=5 and out=74
				BigDecimal totalPrice = new BigDecimal(BigInteger.ZERO);
				
				// First day price
				totalPrice = totalPrice.add(calculateDay(enterHour, 24));
				
				// Last day price
				totalPrice = totalPrice.add(calculateDay(0, exitHour));
				
				// Other days price
				totalPrice = totalPrice.add(fullDayPrice.multiply(new BigDecimal(days - 1)));
				
				return totalPrice;
			}
		}
	}

	/**
	 * Calculate day price.
	 * 
	 * @param start the start time in hours
	 * @param end the end time in hours
	 * @return the price per day
	 */
	private BigDecimal calculateDay(int start, int end) {
		// How many hours has double price
		int doublePriceHours = getDoublePriceHours(start, end);
		
		// How many hours has standart price
		int standardPriceHours = getStandardPriceHours(start, end);
		
		BigDecimal totalHours = new BigDecimal((doublePriceHours * 2) + standardPriceHours);
		
		return hourPrice.multiply(totalHours);
	}
	
	/**
	 * Calculate hours of day with double price. 
	 * It's 0-6 and 23-24 hours of a day.
	 * 
	 * @param start the start time in hours
	 * @param end the end time in hours
	 * @return the hours count
	 */
	private int getDoublePriceHours(int start, int end) {
		int hours = 0;
		
		// 0 - 6 hours case
		if (start <= 6) {
			// We need hours till 6
			int stop = (end <= 6) ? end : 6;
			hours += (stop - start);
		}
		
		// 23-24 hours case
		if (end == 24) {
			hours++;
		}
		
		return hours;
	}
	
	/**
	 * Calculate hours of day with standard price.
	 * It's 6-23 hours of a day.
	 * 
	 * @param start the start time in hours
	 * @param end the end time in hours
	 * @return the hours count
	 */
	private int getStandardPriceHours(int start, int end) {
		int hours = 0;
		
		// Ignore 0-6 and 23-24 hours
		if(end < 6 || start >= 23) {
			return hours;
		}

		// We need hours starts with 6
		int startLocal = start;
		if(start < 6) {
			startLocal = 6;
		}

		// We need hours till 23
		int stop = (end > 23) ? 23 : end;
		hours += (stop - startLocal);
		
		return hours;
	}

}
