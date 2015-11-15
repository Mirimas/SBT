package ru.sbt.parking.exceptions;

/**
 * Car absent exception.
 *
 * @author Anton Kildishev
 */
public class CarAbsentException extends Exception {

	/**
	 * Exception car not in parking.
	 * 
	 * @param message the message
	 */
	public CarAbsentException(String message) {
		super(message);
	}

}
