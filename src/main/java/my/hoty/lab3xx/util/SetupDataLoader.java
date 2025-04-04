package my.hoty.lab3xx.util;

import jakarta.transaction.Transactional;
import my.hoty.lab3xx.model.Client;
import my.hoty.lab3xx.model.Role;
import my.hoty.lab3xx.repository.ClientRepo;
import my.hoty.lab3xx.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private ClientRepo clientRepo;
    boolean alreadySetup = false;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(alreadySetup) {
            return;
        }
        createRoleIfNotFound("USER");
        createRoleIfNotFound("ADMIN");
        if(clientRepo.findByUsername("admin") == null) {
            Role adminRole = roleRepo.findByName("ADMIN");
            Client client = new Client();
            client.setUsername("admin");
            client.setPassword(passwordEncoder.encode("admin"));
            client.setRole(adminRole);
            clientRepo.save(client);
        }

        alreadySetup = true;
    }

    @Transactional
    void createRoleIfNotFound(String roleName) {
        Role role = roleRepo.findByName(roleName);
        if(role == null) {
            role = new Role(roleName);
            roleRepo.save(role);
        }
    }
}
