package com.forumhub.controller;


import com.forumhub.domain.perfil.PerfilRepository;
import com.forumhub.domain.usuario.DadosCadastroUsuario;
import com.forumhub.domain.usuario.DadosDetalhamentoUsuario;
import com.forumhub.domain.usuario.Usuario;
import com.forumhub.domain.usuario.UsuarioRepository;
import com.forumhub.validacoes.ValidacaoException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    public UsuarioController(UsuarioRepository usuarioRepository, PerfilRepository perfilRepository){
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity cadastrarUsuario(@RequestBody @Valid DadosCadastroUsuario dados, UriComponentsBuilder uriBuilder){

        //Validação do perfil
        if (!perfilRepository.existsById(dados.idPerfil())) {
            throw new ValidacaoException("Id do perfil informado não existe...!");
        }

        //Validação da existencia de usuário Cadastrado
        if (usuarioRepository.existsByEmail(dados.email())) {
            throw new ValidacaoException("Usuário já cadastrado....!");
        }

        var usuario = new Usuario(dados);

        usuarioRepository.save(usuario);

        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoUsuario(usuario));
    }

}
