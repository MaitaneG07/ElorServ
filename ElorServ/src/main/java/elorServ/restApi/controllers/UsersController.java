package elorServ.restApi.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import elorServ.modelo.entities.Users;
import elorServ.restApi.repositoryRest.UsersRepository;

@RestController
@RequestMapping("/api")
public class UsersController {

    private final UsersRepository usersRepository;

    public UsersController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping("/users")
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }
    
    @GetMapping("/user/find/{name}")
    public ResponseEntity<Users> getByName(@PathVariable String name) {

        Users ret = null;

        List<Users> users = usersRepository.findAll();

        for (Users user : users) {
            if (user.getNombre() != null && user.getNombre().equals(name)) {
                ret = user;
                break;
            }
        }

        return ret != null
                ? ResponseEntity.ok(ret)
                : ResponseEntity.notFound().build();
    }

    
    @PostMapping("/user/new")
    public void addUser(@RequestBody Users user) {
        usersRepository.save(user);
    }
}
