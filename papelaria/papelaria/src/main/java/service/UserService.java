package service;

import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import repository.UserRepository;
import util.CPFValidator;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(String email, String cpf, String password, String confirmPassword) throws Exception {
        if (!password.equals(confirmPassword)) {
            throw new Exception("As senhas não correspondem!");
        }

        if (!CPFValidator.isValidCPF(cpf)) {
            throw new Exception("CPF inválido!");
        }

        User user = new User();
        user.setEmail(email);
        user.setCpf(cpf);
        user.setPassword(passwordEncoder.encode(password));

        return userRepository.save(user);
    }
}

