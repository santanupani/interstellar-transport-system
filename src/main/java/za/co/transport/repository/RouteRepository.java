package za.co.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import za.co.transport.entity.Route;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

}
