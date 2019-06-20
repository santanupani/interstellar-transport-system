package za.co.transport.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((planetId == null) ? 0 : planetId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Planet other = (Planet) obj;
        if (planetId == null) {
            if (other.planetId != null)
                return false;
        } else if (!planetId.equals(other.planetId))
            return false;
        return true;
    }
	
	
	
	
}
