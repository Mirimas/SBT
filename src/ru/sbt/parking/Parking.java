package ru.sbt.parking;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sbt.parking.exceptions.PriceArithmeticException;
import ru.sbt.parking.exceptions.CarAbsentException;

/**
 * Parking.
 * 
 * @author Anton Kildishev
 */
public class Parking {

	/**
	 * Capacity of the parking is max places for a cars.
	 * Must be more then 1.
	 */
	private static final int CAPACITY = 5;
	
	/**
	 * Hourly cost for a one car to stay at the parking place.
	 */
	private static final BigDecimal HOUR_PRICE = BigDecimal.ONE;
	
	/**
	 * Parking places. 
	 * Stores map carId - car object.
	 */
	private final Map<Integer, Car> parking;
	
	/**
	 * PriceManager for price calculations.
	 */
	private final PriceManager priceManager;
	
	private static final Logger LOG = LogManager.getLogger(Parking.class.getName());

	/**
	 * Parking.
	 */
	public Parking() {
		parking = new HashMap<Integer, Car>(CAPACITY);
		priceManager = new PriceManager(HOUR_PRICE);
	}
	
	/**
	 * Entry to the parking.
	 * 
	 * @param carId the car id
	 * @param time the entry time in hours 
	 * @return true if car can entry
	 */
	public boolean in(int carId, int time) {
		// Check is parking has place for a car
		if(parking.size() == CAPACITY) {
			LOG.debug("Parking is full. Car id={} can't enter to the parking", carId);
			return false;
		}
		
		// Check is carId already in parking
		boolean containsKey = parking.containsKey(carId);
		if(containsKey) {
			LOG.error("Car id={} already in parking", carId);
			return false;
		}

		Car car = new Car(carId);
		car.setTimeIn(time);
		parking.put(carId, car);
		
		LOG.debug("IN: {} successfully added", car);
		
		return true;
	}

	/**
	 * Exit from the parking.
	 * 
	 * @param carId the car id
	 * @param time the exit time in hours 
	 * @return the parking cost
	 * @throws CarAbsentException if car not in the parking while out
	 * @throws PriceArithmeticException if price calculation goes wrong
	 */
	public BigDecimal out(int carId, int time) throws CarAbsentException, PriceArithmeticException {
		Car car = parking.get(carId);
		if(car == null) {
			throw new CarAbsentException("There are no car with id=" + carId);
		}
		
		// Check time
		int timeIn = car.getTimeIn();
		if(timeIn >= time) {
			throw new IllegalArgumentException("Exit time must be more then entry time");
		}
		
		car.setTimeOut(time);

		BigDecimal price = priceManager.calculatePrice(car);
		if(price.intValue() < 0) {
			throw new PriceArithmeticException("Price can't be less then 0");
		}
		
		parking.remove(carId);
		
		LOG.debug("OUT: {} successfully removed, price={}", car, price);
		
		return price;
	}

	/**
	 * Main method.
	 * 
	 * @param args the arguments
	 * @throws CarAbsentException the car absent exception
	 * @throws PriceArithmeticException the price arithmetic exception
	 */
	public static void main(String[] args) throws CarAbsentException, PriceArithmeticException {
		Parking parking = new Parking();

		// Test one car 
		parking.in(1, 0);
		parking.out(1, 96);

		// Test full capacity. Add cars in parking
		for (int i = 1; i <= CAPACITY; i++) {
			int timeIn = (i-1) * 25;
			parking.in(i, timeIn);
		}
		
		// Try add new car in full parking
		parking.in(1, 25);
		
		// Remove cars from parking
		for (int i = 1; i <= CAPACITY; i++) {
			parking.out(i, i*50);
		}
	}

}
