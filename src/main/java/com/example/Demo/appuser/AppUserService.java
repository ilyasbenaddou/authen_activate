package com.example.Demo.appuser;

import com.example.Demo.registration.token.ConfirmationToken;
import com.example.Demo.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenservice;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElseThrow(()->
                new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG,email)));
    }
//////////////////////////////////
  
    public AppUser activateUser(Long idUser) {
    	AppUser user = appUserRepository.findById(idUser).get();
    	user.setEnabled(true);
    	appUserRepository.save(user);
		return user;
    }
//    public AppUser desactivateUser(Long idUser) {
//    	AppUser user = appUserRepository.findById(idUser).get();
//    	user.setEnabled(false);
//    	appUserRepository.save(user);
//		return user;
//    }
    //////////////////////////////////
    public String signUpUser(AppUser appUser) {
        boolean userExists = appUserRepository
                .findByEmail(appUser.getEmail())
                .isPresent();
        if(userExists){
            throw new IllegalStateException("email already Taken");
        }

        String encodedPassword = bCryptPasswordEncoder
                .encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        appUserRepository.save(appUser);

        String token = UUID.randomUUID().toString();


        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        confirmationTokenservice.saveConfirmationToken(confirmationToken);

        //TODO: send Email
        return token ;
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }
}
