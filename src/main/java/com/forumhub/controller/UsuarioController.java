package com.forumhub.controller;


import com.forumhub.domain.perfil.PerfilRepository;
import com.forumhub.domain.usuario.DtoCadastroUsuario;
import com.forumhub.domain.usuario.DtoDetalheUsuario;
import com.forumhub.domain.usuario.Usuario;
import com.forumhub.domain.usuario.UsuarioRepository;
import com.forumhub.validacoes.ValidacaoException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;
    public UsuarioController(UsuarioRepository usuarioRepository, PerfilRepository perfilRepository, PasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    @Transactional
    public ResponseEntity cadastrarUsuario(@RequestBody @Valid DtoCadastroUsuario dados, UriComponentsBuilder uriBuilder){

        //Validação do perfil
        if (!perfilRepository.existsById(dados.idPerfil())) {
            throw new ValidacaoException("Id do perfil informado não existe...!");
        }

        //Validação da existencia de usuário Cadastrado
        if (usuarioRepository.existsByEmail(dados.email())) {
            throw new ValidacaoException("Usuário já cadastrado....!");
        }

        var senhaCripto = passwordEncoder.encode(dados.senha());

        var usuario = new Usuario(dados, senhaCripto);

        usuarioRepository.save(usuario);

        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();

        return ResponseEntity.created(uri).body(new DtoDetalheUsuario(usuario));
    }

}
