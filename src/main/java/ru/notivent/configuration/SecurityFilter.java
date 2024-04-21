package ru.notivent.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.notivent.service.PasswordService;
import ru.notivent.service.UserService;
import ru.notivent.util.HttpUtil;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

  final UserService userService;
  final PasswordService passwordService;
  private final List<String> notSecure =
          List.of("/notivent/auth", "/notivent/user/password/reset", "/actuator/health");

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    val userId = getUserId(request);
    if (userId != null) {
      var userOpt = userService.findById(userId);
      if (userOpt.isPresent()) {
        filterChain.doFilter(request, response);
        return;
      }
    }
    response.sendError(HttpStatus.UNAUTHORIZED.value());
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    val path = request.getRequestURI();
    return notSecure.contains(path);
  }

  private UUID getUserId(HttpServletRequest request) {
    val bearer = request.getHeader(HttpUtil.X_AUTH);
    if (StringUtils.hasText(bearer)) {
      val userId = passwordService.decodeBase64(bearer);
      return UUID.fromString(userId);
    }
    return null;
  }
}
