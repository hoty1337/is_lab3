package my.hoty.lab3xx.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Set;

@Getter @Setter
@Entity
@Table(name="role")
public class Role implements Serializable, GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @ManyToMany(mappedBy = "roles")
    private Set<Client> clients;

    public Role(String name) {
        this.name = name;
    }

    public Role() {}

    @Override
    public String getAuthority() {
        return getName();
    }
}
