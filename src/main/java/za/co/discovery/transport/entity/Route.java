package za.co.discovery.transport.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table()
public class Route {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long routeId;
	
	@OneToOne()
	@JoinColumn(name="planet_origin")
	private Planet origin;
	
	@OneToOne()
	@JoinColumn(name="planet_destination")
	private Planet destination;
	
	@Column
	private double distance;
	
	public Route() {}

	public Route(long routeId, Planet origin, Planet destination, double distance) {
		this.routeId = routeId;
		this.origin = origin;
		this.destination = destination;
		this.distance = distance;
	}

	public long getRouteId() {
		return routeId;
	}

	public void setRouteId(long routeId) {
		this.routeId = routeId;
	}

	public Planet getOrigin() {
		return origin;
	}

	public void setOrigin(Planet origin) {
		this.origin = origin;
	}

	public Planet getDestination() {
		return destination;
	}

	public void setDestination(Planet destination) {
		this.destination = destination;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	

}
