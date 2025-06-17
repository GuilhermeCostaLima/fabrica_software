package br.com.trabalhofinal.fabrica_software.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import jakarta.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            System.out.println("Autenticação bem-sucedida!");
            System.out.println("Principal class: " + authentication.getPrincipal().getClass().getName());
            
            HttpSession session = request.getSession();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            session.setAttribute("loggedUser", userDetails.getUser());
            
            System.out.println("Usuário autenticado: " + userDetails.getUsername());
            System.out.println("Redirecionando para página inicial");
            
            response.sendRedirect("/");
        };
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            System.out.println("Falha na autenticação!");
            System.out.println("Erro: " + exception.getMessage());
            System.out.println("Causa: " + exception.getCause());
            response.sendRedirect("/login?error");
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/debug-password").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/promocoes/**").authenticated()
                .requestMatchers("/", "/login", "/cadastro", "/sobre", "/contato", "/css/**", "/js/**", "/images/**", "/hoteis/**", "/webjars/**", "/error").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler())
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );
        
        return http.build();
    }
}
