package br.com.pdv.smartpos.controller;

import br.com.pdv.smartpos.dto.AuthRequest;
import br.com.pdv.smartpos.dto.AuthResponse;
import br.com.pdv.smartpos.dto.UsuarioCadastroRequest;
import br.com.pdv.smartpos.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody UsuarioCadastroRequest request) {
        return authService.cadastrar(request);
    }
}
