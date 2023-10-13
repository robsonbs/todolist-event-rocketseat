package br.com.olgari.todolist.filter;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.olgari.todolist.user.IUserRepository;
import br.com.olgari.todolist.user.UserModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class FilterTaskAuth extends OncePerRequestFilter {

  private IUserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String authorization = request.getHeader("Authorization");
    if (!StringUtils.hasText(authorization))
      return;
    try {
      String authEncoded = authorization.split(" ")[1];
      String authDecoded = new String(Base64.getDecoder().decode(authEncoded));
      String[] credentials = authDecoded.split(":");

      if (credentials.length != 2) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }

      String username = credentials[0];
      String password = credentials[1];
      Optional<UserModel> user = this.userRepository.findByUsername(username);
      if (!user.isPresent() || !BCrypt.verifyer().verify(password.toCharArray(), user.get().getPassword()).verified) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
      } else {
        // Autenticação bem-sucedida, continue com a cadeia de filtros.
        request.setAttribute("userId", user.get().getId());
        filterChain.doFilter(request, response);
      }
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();
    return !path.startsWith("/tasks");
  }
}
