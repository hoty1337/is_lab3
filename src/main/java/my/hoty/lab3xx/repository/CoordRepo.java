package my.hoty.lab3xx.repository;

import my.hoty.lab3xx.entity.Coordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordRepo extends JpaRepository<Coordinates, Integer> {
    Coordinates findById(Long id);
}
