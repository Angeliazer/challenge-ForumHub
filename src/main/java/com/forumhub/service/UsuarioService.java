package com.forumhub.service;

import com.forumhub.domain.usuario.Usuario;
import com.forumhub.domain.usuario.UsuarioRepository;
import com.forumhub.validacoes.ValidacaoException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    public UsuarioService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario validarAutor(@Valid Long id) {

        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException("Autor não encontrado...!"));
    }

    public boolean existsByEmail(@NotNull String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public void gravarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }
}
