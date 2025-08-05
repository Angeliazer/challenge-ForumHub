package com.forumhub.service;

import com.forumhub.domain.usuario.Usuario;
import com.forumhub.domain.usuario.UsuarioRepository;
import com.forumhub.validacoes.ValidacaoException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class AutorService {
    private final UsuarioRepository usuarioRepository;
    public AutorService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario validarAutor(@Valid Long id) {

        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException("Autor não encontrado...!"));
    }
}
