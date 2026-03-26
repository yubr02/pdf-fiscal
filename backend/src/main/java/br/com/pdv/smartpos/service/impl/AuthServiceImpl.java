package br.com.pdv.smartpos.service.impl;

import br.com.pdv.smartpos.dto.AuthRequest;
import br.com.pdv.smartpos.dto.AuthResponse;
import br.com.pdv.smartpos.dto.UsuarioCadastroRequest;
import br.com.pdv.smartpos.exception.BusinessException;
import br.com.pdv.smartpos.model.Usuario;
import br.com.pdv.smartpos.repository.UsuarioRepository;
import br.com.pdv.smartpos.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        Usuario usuario = usuarioRepository.findByUsuarioIgnoreCase(request.usuario())
            .orElseThrow(() -> new BusinessException("Usuário ou senha inválidos."));

        boolean senhaValida = usuario.getSenhaHash() != null && usuario.getSenhaHash().startsWith("$2")
            ? passwordEncoder.matches(request.senha(), usuario.getSenhaHash())
            : request.senha().equals(usuario.getSenhaHash());

        if (!senhaValida) {
            throw new BusinessException("Usuário ou senha inválidos.");
        }

        return new AuthResponse(usuario.getId(), usuario.getNome(), usuario.getUsuario(), usuario.getPerfil(), UUID.randomUUID().toString());
    }

    @Override
    public AuthResponse cadastrar(UsuarioCadastroRequest request) {
        if (!request.senha().equals(request.confirmarSenha())) {
            throw new BusinessException("A confirmação da senha não confere.");
        }

        usuarioRepository.findByUsuarioIgnoreCase(request.usuario()).ifPresent(existing -> {
            throw new BusinessException("Já existe um usuário com este login.");
        });

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setUsuario(request.usuario());
        usuario.setSenhaHash(passwordEncoder.encode(request.senha()));
        usuario.setPerfil("OPERADOR");

        Usuario saved = usuarioRepository.save(usuario);
        return new AuthResponse(saved.getId(), saved.getNome(), saved.getUsuario(), saved.getPerfil(), UUID.randomUUID().toString());
    }
}
