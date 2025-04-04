package my.hoty.lab3xx.repository;

import my.hoty.lab3xx.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepo extends JpaRepository<Client, Long> {

    Client findByUsername(String username);
}
