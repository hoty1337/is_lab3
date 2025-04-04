package my.hoty.lab3xx.repository;

import my.hoty.lab3xx.model.Client;
import my.hoty.lab3xx.model.ImportOperation;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImportOperationRepo extends JpaRepository<ImportOperation, Integer> {
    List<ImportOperation> findByImportedBy(Client importedBy, Sort sort);
}
