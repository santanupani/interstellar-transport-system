package za.co.discovery.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import za.co.discovery.transport.entity.Route;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

}
