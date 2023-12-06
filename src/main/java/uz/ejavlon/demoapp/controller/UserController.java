package uz.ejavlon.demoapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import uz.ejavlon.demoapp.dto.ResponseApi;
import uz.ejavlon.demoapp.dto.UserDto;
import uz.ejavlon.demoapp.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<?> getAllUsers(){
        ResponseApi responseApi = authenticationService.getAllUsers();
        return ResponseEntity.status(responseApi.getSuccess() ? HttpStatus.OK : HttpStatus.FORBIDDEN).body(responseApi);
    }

    @PostMapping("/profile/expiredToken")
    public ResponseEntity<?> expiredToken(@NonNull HttpServletRequest request){
        ResponseApi responseApi = authenticationService.isExpiredToken(request);
        return ResponseEntity.status(responseApi.getSuccess() ? HttpStatus.OK : HttpStatus.FORBIDDEN).body(responseApi);
    }

    @PostMapping("/profile/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserDto user){
        ResponseApi responseApi = authenticationService.signUp(user);
        return ResponseEntity.status(responseApi.getSuccess() ? HttpStatus.CREATED : HttpStatus.FORBIDDEN).body(responseApi);
    }

    @PostMapping("/profile/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody UserDto user){
        ResponseApi responseApi = authenticationService.signIn(user);
        return ResponseEntity.status(responseApi.getSuccess() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED).body(responseApi);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserDto userDto){
        ResponseApi responseApi = authenticationService.updateUser(userDto);
        return ResponseEntity.status(responseApi.getSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(responseApi);
    }

    @PutMapping("/profile/{id}")
    public ResponseEntity<?> updateUserAdmin(@Valid @RequestBody UserDto userDto,@PathVariable Integer id){
        ResponseApi responseApi = authenticationService.updateUserAdmin(userDto,id);
        return ResponseEntity.status(responseApi.getSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(responseApi);
    }

    @DeleteMapping("/profile/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Integer id){
        ResponseApi responseApi = authenticationService.deleteUserById(id);
        return ResponseEntity.status(responseApi.getSuccess() ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND).body(responseApi);
    }

    @PostMapping("/giveAdminRole/{id}")
    public ResponseEntity<?> giveAdmin(@PathVariable Integer id){
        ResponseApi responseApi = authenticationService.giveAdminRole(id);
        return ResponseEntity.status(responseApi.getSuccess() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED).body(responseApi);
    }
}
