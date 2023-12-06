package uz.ejavlon.demoapp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.ejavlon.demoapp.dto.UserDto;
import uz.ejavlon.demoapp.entity.User;
import uz.ejavlon.demoapp.repository.UserRepository;
import uz.ejavlon.demoapp.service.AuthenticationService;

import java.time.LocalDateTime;
import java.util.Optional;

import static uz.ejavlon.demoapp.enums.Role.ADMIN;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${spring.sql.init.mode}")
    private String initMode;

    @Bean
    public CommandLineRunner commandLineRunner(AuthenticationService service){
        return  args -> {
            if(initMode.equals("always")){
                System.out.println("init mode true");
                var admin = User.builder()
                        .username("ejavlon")
                        .password(passwordEncoder.encode("6nf4rd5j99"))
                        .firstName("Javlon")
                        .lastName("Ergashev")
                        .createdAt(LocalDateTime.now())
                        .role(ADMIN)
                        .build();

                Optional<User> optionalUser = userRepository.findByUsername(admin.getUsername());
                if (optionalUser.isEmpty()){
                    userRepository.save(admin);
                }
                service.signIn(UserDto.builder().username(admin.getUsername()).password("6nf4rd5j99").build());
            }

        };
    }

}
