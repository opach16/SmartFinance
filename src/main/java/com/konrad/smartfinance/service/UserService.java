package com.konrad.smartfinance.service;

import com.konrad.smartfinance.domain.model.Account;
import com.konrad.smartfinance.domain.model.Currency;
import com.konrad.smartfinance.domain.model.User;
import com.konrad.smartfinance.exception.CurrencyExeption;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AccountService accountService;
    private final CurrencyService currencyService;
    private final BCryptPasswordEncoder bCrypt;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) throws UserException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException(UserException.USER_NOT_FOUND));
    }

    public User getUserByUsername(String username) throws UserException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserException.USER_NOT_FOUND));
    }

    public User addUser(User user, String mainCurrencySymbol, BigDecimal mainBalance) throws CurrencyExeption, UserException {
        validateUser(user);
        Currency mainCurrency = currencyService.getCurrencyBySymbol(mainCurrencySymbol);
        user.setPassword(bCrypt.encode(user.getPassword()));
        User fetchedUser = userRepository.save(user);
        Account account = accountService.createAccount(new Account(fetchedUser, mainCurrency, mainBalance));
        fetchedUser.setAccount(account);
        return userRepository.save(fetchedUser);
    }

    public User updateUser(Long id, User user) throws UserException {
        User fetchedUser = userRepository.findById(id)
                .orElseThrow(() -> new UserException(UserException.USER_NOT_FOUND));
        fetchedUser.setUsername(user.getUsername());
        fetchedUser.setEmail(user.getEmail());
        fetchedUser.setPassword(user.getPassword());
        fetchedUser.setUpdatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        return userRepository.save(fetchedUser);
    }

    public void deleteUser(Long id) throws UserException {
        User fetchedUser = userRepository.findById(id).
                orElseThrow(() -> new UserException(UserException.USER_NOT_FOUND));
        fetchedUser.setUsername("deleted" + fetchedUser.getId());
        fetchedUser.setEmail("deleted" + fetchedUser.getId());
        fetchedUser.setPassword("deleted" + fetchedUser.getId());
        fetchedUser.setUpdatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        userRepository.save(fetchedUser);
    }

    private void validateUser(User user) throws UserException {
        if (userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new UserException("Username: " + user.getUsername() + " already exists");
        }
        if (user.getUsername().length() < 5) {
            throw new UserException("Username must contain at least 4 characters");
        }
        if (user.getPassword().length() < 8) {
            throw new UserException("Password must be at least 6 characters");
        }
        if (user.getEmail().length() < 5) {
            throw new UserException("Email too short");
        }
        if (!user.getEmail().contains("@") || !user.getEmail().contains(".")) {
            throw new UserException("Invalid email");
        }
    }
}
