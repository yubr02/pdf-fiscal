package br.com.pdv.smartpos.service;

import br.com.pdv.smartpos.dto.AuthRequest;
import br.com.pdv.smartpos.dto.AuthResponse;
import br.com.pdv.smartpos.dto.UsuarioCadastroRequest;

public interface AuthService {

    AuthResponse login(AuthRequest request);

    AuthResponse cadastrar(UsuarioCadastroRequest request);
}
