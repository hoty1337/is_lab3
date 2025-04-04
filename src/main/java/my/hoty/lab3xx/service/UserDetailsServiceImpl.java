package my.hoty.lab3xx.service;

import my.hoty.lab3xx.model.Client;
import my.hoty.lab3xx.repository.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private ClientRepo clientRepo;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientRepo.findByUsername(username);
        System.out.println(client);
        if (client == null) {
            throw new UsernameNotFoundException(username);
        }

        return client;
    }
}
