package za.co.discovery.transport.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table
public class Planet {

	@Id
	private String planetId;
	@Column(unique=true)	
	private String planetName;
	
	public Planet() {}

	public Planet(String planetId, String planetName) {		
		this.planetId = planetId;
		this.planetName = planetName;
	}

	public String getPlanetId() {
		return planetId;
	}

	public void setPlanetId(String planetId) {
		this.planetId = planetId;
	}

	public String getPlanetName() {
		return planetName;
	}

	public void setPlanetName(String planetName) {
		this.planetName = planetName;
	}

	
	
	
	
}
