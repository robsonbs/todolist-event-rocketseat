package br.com.olgari.todolist.user;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.olgari.todolist.errors.RequestErrorException;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

  private IUserRepository userRepository;

  @PostMapping("/")
  public ResponseEntity<UserModel> create(@RequestBody UserModel user) throws RequestErrorException {
    Optional<UserModel> findUser = this.userRepository.findByUsername(user.getUsername());
    if (findUser.isPresent()) {
      throw new RequestErrorException("Usuário já existente");
    }

    String passwordHashed = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
    user.setPassword(passwordHashed);

    return ResponseEntity.ok().body(this.userRepository.saveAndFlush(user));
  }
}
