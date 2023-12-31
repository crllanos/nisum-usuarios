package cl.nisum.usuarios.config;

import cl.nisum.usuarios.filter.AuthenticationFilter;
import cl.nisum.usuarios.filter.AuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final static String roleAdmin = "ROLE_ADMIN";
    private final static String roleSuperAdmin = "ROLE_SUPERADMIN";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationFilter filter = new AuthenticationFilter(authenticationManagerBean());
        filter.setFilterProcessesUrl("/user-registry/login/**");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/login").permitAll();

        http.authorizeRequests().antMatchers(HttpMethod.GET, "/user-registry/**")
                .hasAnyAuthority(roleAdmin, roleSuperAdmin);
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/user-registry/**")
                .hasAnyAuthority(roleAdmin, roleSuperAdmin);

        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/user-registry/**")
                .hasAnyAuthority(roleSuperAdmin);
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/user-registry/**")
                .hasAnyAuthority(roleSuperAdmin);

        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(filter);
        http.addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}