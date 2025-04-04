package my.hoty.lab3xx.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import my.hoty.lab3xx.entity.OperationStatus;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class ImportOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;
    @Column(columnDefinition = "TEXT")
    private String filename;
    private OperationStatus status;
    private int importedCount;
    @Column(columnDefinition = "TEXT")
    private String fileUrl;

    @ManyToOne
    private Client importedBy;
}
