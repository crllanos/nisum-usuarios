package cl.nisum.usuarios.filter;
import cl.nisum.usuarios.config.CredentialsConfig;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class AuthorizationFilter extends OncePerRequestFilter {
    private final static String bearer = "Bearer ";

    @Autowired
    private CredentialsConfig credentialsConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("Pasa por AuthorizationFilter");
        String secret = credentialsConfig.getSecret();
        if (request.getServletPath().equals("/login")){
            filterChain.doFilter(request, response);
        }else{
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            log.info("Pasa por AuthorizationFilter, authHeader: {}", authHeader);
            if(!ObjectUtils.isEmpty(authHeader) && authHeader.startsWith(bearer)){
                try {
                    String token = authHeader.substring(bearer.length());
                    Algorithm alg  = Algorithm.HMAC256(secret.getBytes());
                    JWTVerifier verificator = JWT.require(alg).build();
                    DecodedJWT decoder = verificator.verify(token);
                    String username = decoder.getSubject();

                    Collection<SimpleGrantedAuthority> roles = new ArrayList<>();
                    decoder.getClaim("roles").asList(String.class).forEach(rol -> {
                        roles.add(new SimpleGrantedAuthority(rol));
                    });

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(username, null, roles);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    filterChain.doFilter(request, response);

                } catch (Exception e) {
                    log.error("error en AuthorizationFilter {}", e.getMessage());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpStatus.NOT_FOUND.value());
                    Map<String , String > error = new HashMap<>();
                    error.put("message-error", e.getMessage());
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
            }else {
                filterChain.doFilter(request, response);
            }
        }
    }
}