package my.hoty.lab3xx.util;

import my.hoty.lab3xx.model.Client;
import my.hoty.lab3xx.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    @Autowired
    private ClientService clientService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Client.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Client client = (Client) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
        if (client.getUsername().length() < 2 || client.getUsername().length() > 32) {
            errors.rejectValue("username", "Size.userForm.username");
        }
        if (clientService.findByUsername(client.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.userForm.username");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (client.getPassword().length() < 2 || client.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password");
        }
    }
}
