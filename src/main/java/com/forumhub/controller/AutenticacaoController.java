package com.forumhub.controller;

import com.forumhub.domain.usuario.DtoLoginUsuario;
import com.forumhub.infra.security.DtoToken;
import com.forumhub.domain.usuario.Usuario;
import com.forumhub.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AutenticacaoController(AuthenticationManager authenticationManager, TokenService tokenService){
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity login (@RequestBody @Valid DtoLoginUsuario dtoLoginUsuario){

        var token = new UsernamePasswordAuthenticationToken(dtoLoginUsuario.email(), dtoLoginUsuario.senha());
        var authentication = authenticationManager.authenticate(token);
        return ResponseEntity.ok(new DtoToken(tokenService.tokenCreate((Usuario) authentication.getPrincipal())));
    }

}
