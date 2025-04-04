package my.hoty.lab3xx.repository;

import my.hoty.lab3xx.entity.FuelType;
import my.hoty.lab3xx.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepo extends JpaRepository<Vehicle, Integer> {
    Vehicle findById(int id);

    @Modifying
    @Query("DELETE FROM Vehicle v WHERE v.fuelType = :fuelType")
    void deleteByFuelType(@Param("fuelType") FuelType fuelType);

    @Query("SELECT ROUND(AVG(v.fuelConsumption),2) FROM Vehicle v")
    Double calculateAverageFuelConsumption();

    @Query("SELECT v.fuelType, COUNT(v) FROM Vehicle v GROUP BY v.fuelType")
    List<Object[]> countByFuelType();

    @Query("SELECT v FROM Vehicle v WHERE v.enginePower BETWEEN :minPower AND :maxPower")
    List<Vehicle> findByEnginePowerRange(
            @Param("minPower") Float minPower,
            @Param("maxPower") Float maxPower
    );

    @Modifying
    @Query("UPDATE Vehicle v SET v.numberOfWheels = v.numberOfWheels + :wheelsToAdd WHERE v.id = :id")
    void addWheels(@Param("id") Long id, @Param("wheelsToAdd") Integer wheelsToAdd);

}
