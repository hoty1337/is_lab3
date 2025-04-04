package my.hoty.lab3xx.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import my.hoty.lab3xx.model.Client;

@Getter
@Setter
@Entity
@Table(name = "vehicle")
public class Vehicle {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    @Column(nullable = false)
    private String name; //Поле не может быть null, Строка не может быть пустой
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "coordinates_id", nullable = false)
    private Coordinates coordinates; //Поле не может быть null
    @Column(updatable = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VehicleType type; //Поле может быть null
    @Column(nullable = false)
    private Float enginePower; //Поле не может быть null, Значение поля должно быть больше 0
    @Column(nullable = false)
    private int numberOfWheels; //Значение поля должно быть больше 0
    @Column(nullable = false)
    private double capacity; //Значение поля должно быть больше 0
    @Column(nullable = false)
    private double distanceTravelled; //Значение поля должно быть больше 0
    @Column(nullable = false)
    private Float fuelConsumption; //Поле может быть null, Значение поля должно быть больше 0
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FuelType fuelType; //Поле может быть null
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
