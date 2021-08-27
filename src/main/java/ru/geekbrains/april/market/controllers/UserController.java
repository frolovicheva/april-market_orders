package ru.geekbrains.april.market.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.april.market.dtos.JwtResponse;
import ru.geekbrains.april.market.dtos.RegisterUserDto;
import ru.geekbrains.april.market.dtos.UserDto;
import ru.geekbrains.april.market.error_handling.ResourceNotFoundException;
import ru.geekbrains.april.market.models.Role;
import ru.geekbrains.april.market.models.User;
import ru.geekbrains.april.market.repositories.RoleRepository;
import ru.geekbrains.april.market.repositories.UserRepository;
import ru.geekbrains.april.market.services.RoleService;
import ru.geekbrains.april.market.services.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @GetMapping("/me")
    public UserDto getCurrentUsername(Principal principal) {
        User currentUser = userService.findByUsername(principal.getName()).orElseThrow(() -> new ResourceNotFoundException("User doesn't exist"));
        return new UserDto(currentUser.getUsername(), currentUser.getEmail());
    }

    @PostMapping("/register") // todo заменить при решении домашнего задания
    public void register(@RequestBody RegisterUserDto registerUserDto) {
        User newUser = new User ();
        newUser.setUsername (registerUserDto.getUsername ());
        newUser.setPassword (passwordEncoder.encode (registerUserDto.getPassword ()));
        newUser.setEmail (registerUserDto.getEmail ());
        Collection<Role> roles = new ArrayList<> ();
        roles.add(roleService.findById (1L).get ());
        newUser.setRoles (roles);
        userRepository.save (newUser);
    }
}
