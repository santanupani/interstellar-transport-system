package za.co.discovery.transport.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import za.co.discovery.transport.entity.Planet;

@Repository
public interface PlanetRepository extends JpaRepository<Planet, String> {

}
