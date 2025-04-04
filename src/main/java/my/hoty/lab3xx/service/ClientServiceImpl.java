package my.hoty.lab3xx.service;

import my.hoty.lab3xx.model.Client;
import my.hoty.lab3xx.model.Role;
import my.hoty.lab3xx.repository.ClientRepo;
import my.hoty.lab3xx.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepo clientRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Client findByUsername(String name) {
        return clientRepo.findByUsername(name);
    }

    @Override
    public boolean save(Client client) {
        if(clientRepo.findByUsername(client.getUsername()) != null) {
            return false;
        }

        String encodedPassword = passwordEncoder.encode(client.getPassword());
        client.setPassword(encodedPassword);
        clientRepo.save(client);
        return true;
    }

    @Override
    @Transactional
    public void updateClientRole(String username, String roleName) {
        Client client = clientRepo.findByUsername(username);
        Role newRole = roleRepo.findByName(roleName);
        client.getRoles().clear();
        client.getRoles().add(newRole);
        clientRepo.save(client);
    }

    public boolean deleteClient(Client client) {
        if(clientRepo.existsById(client.getId())) {
            clientRepo.delete(client);
            return true;
        }
        return false;
    }

    @Override
    public List<Client> findAll() {
        return clientRepo.findAll();
    }

}
