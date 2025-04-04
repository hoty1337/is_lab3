package my.hoty.lab3xx.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "coordinates")
public class Coordinates {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long x; //Поле не может быть null
    @Column(nullable = false)
    private Long y; //Значение поля должно быть больше -431, Поле не может быть null
}
