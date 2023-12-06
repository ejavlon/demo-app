package uz.ejavlon.demoapp.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.ejavlon.demoapp.dto.ResponseApi;
import uz.ejavlon.demoapp.dto.UserDto;
import uz.ejavlon.demoapp.entity.User;
import uz.ejavlon.demoapp.enums.Role;
import uz.ejavlon.demoapp.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    public ResponseApi signUp(UserDto userDto) {
        if(userDto == null)
            return ResponseApi
                    .builder()
                    .message("Fields empty")
                    .success(false).build();

        User user = User.builder()
                .role(Role.USER)
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .createdAt(LocalDateTime.now())
                .build();

        user = userRepository.save(user);
        return ResponseApi.builder()
                .message("successfully!")
                .success(true)
                .data(jwtService.generetToken(user))
                .build();
    }

    public ResponseApi signIn(UserDto user) {
        if(user == null)
            return ResponseApi
                    .builder()
                    .message("User not found!")
                    .success(false).build();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );

        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        if (optionalUser.isEmpty()){
            return ResponseApi.builder()
                    .message("User not found!")
                    .success(false)
                    .build();
        }

        String token = jwtService.generetToken(optionalUser.get());
        return ResponseApi.builder()
                .message("Welcome to systems!")
                .success(true)
                .data(token)
                .build();
    }

    public ResponseApi isExpiredToken(HttpServletRequest request) {
        final String header = request.getHeader("Authorization");
        final  String token = header.split(" ")[1].trim();

        String username = jwtService.extractUsername(token);
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty())
            return ResponseApi.builder()
                    .message("User not found")
                    .success(false).build();

        return ResponseApi.builder()
                .data(jwtService.isTokenExpired(token))
                .message("successfully")
                .success(true)
                .data(false)
                .build();
    }

    public ResponseApi updateUser(UserDto userDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setUpdatedAt(LocalDateTime.now());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setUsername(userDto.getUsername());
        userRepository.save(user);

        return ResponseApi.builder()
                .message("User successfully updated")
                .success(true)
                .data(user)
                .build();
    }

    public ResponseApi updateUserAdmin(UserDto userDto,Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent())
            return ResponseApi.builder()
                    .message("User not found")
                    .success(true)
                    .build();

        User user = optionalUser.get();
        user.setUpdatedAt(LocalDateTime.now());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setUsername(userDto.getUsername());
        userRepository.save(user);

        return ResponseApi.builder()
                .message("User successfully updated")
                .success(true)
                .data(user)
                .build();
    }

    public ResponseApi deleteUserById(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty())
            return ResponseApi.builder()
                    .message("User not found ")
                    .success(false)
                    .build();

        return ResponseApi.builder()
                .message(optionalUser.isPresent() ? "User successfully deleted" : "User not found")
                .success(optionalUser.isPresent())
                .build();
    }

    public ResponseApi getAllUsers() {
        return ResponseApi.builder()
                .message("All users")
                .success(true)
                .data(userRepository.findAll())
                .build();
    }

    public ResponseApi giveAdminRole(Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent())
            return ResponseApi.builder()
                    .message("User not found")
                    .success(true)
                    .build();

        User user = optionalUser.get();
        user.setRole(Role.ADMIN);
        user = userRepository.save(user);
        return ResponseApi.builder()
                .message("User found")
                .success(true)
                .data(user)
                .build();
    }
}
