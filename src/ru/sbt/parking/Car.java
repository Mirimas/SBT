package ru.sbt.parking;

/**
 * Car object.
 *
 * @author Anton Kildishev
 */
public class Car {
	
	private int id;
	private int timeIn;
	private int timeOut;

	/**
	 * Create car.
	 * 
	 * @param id the car id
	 */
	public Car(int id) {
		this.id = id;
	}

	/**
	 * Get car id.
	 * 
	 * @return the car id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set car id.
	 * 
	 * @param id the car id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Get entry time in parking.
	 * 
	 * @return the time in
	 */
	public int getTimeIn() {
		return timeIn;
	}

	/**
	 * Set entry time in parking.
	 * 
	 * @param timeIn the time in
	 */
	public void setTimeIn(int timeIn) {
		this.timeIn = timeIn;
	}

	/**
	 * Get exit time form parking.
	 * 
	 * @return the time out
	 */
	public int getTimeOut() {
		return timeOut;
	}

	/**
	 * Set exit time form parking.
	 * 
	 * @param timeOut the time out
	 */
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 59 * hash + this.id;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Car other = (Car) obj;
		return this.id == other.id;
	}

	@Override
	public String toString() {
		return "Car{" + "id=" + id + ", timeIn=" + timeIn + ", timeOut=" + timeOut + '}';
	}
	
}
