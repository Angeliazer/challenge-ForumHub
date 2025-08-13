package com.forumhub.controller;

import com.forumhub.domain.curso.Curso;
import com.forumhub.domain.resposta.DtoRespostaRequest;
import com.forumhub.domain.resposta.Resposta;
import com.forumhub.domain.resposta.RespostaRepository;
import com.forumhub.domain.topico.DtoTopicoRequest;
import com.forumhub.domain.topico.DtoTopicoResponse;
import com.forumhub.domain.topico.Status;
import com.forumhub.domain.topico.Topico;
import com.forumhub.domain.usuario.Usuario;
import com.forumhub.service.AutorService;
import com.forumhub.service.CursoService;
import com.forumhub.service.RespostaService;
import com.forumhub.service.TopicoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/respostas")
@SecurityRequirement(name = "bearer-key")
public class RespostaController {

    private final AutorService autorService;
    private final TopicoService topicoService;
    private final RespostaService respostaService;

    public RespostaController(AutorService autorService, TopicoService topicoService, RespostaService respostaService){
        this.autorService = autorService;
        this.topicoService = topicoService;
        this.respostaService = respostaService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity cadastrarResposta(@RequestBody @Valid DtoRespostaRequest dtoRespostaRequest, UriComponentsBuilder uriComponentsBuilder){

        Usuario autor = autorService.validarAutor(dtoRespostaRequest.usuario_id());

        Topico topico = topicoService.validarIdTopico(dtoRespostaRequest.topico_id());

        var resposta = new Resposta();
        resposta.setMensagem(dtoRespostaRequest.mensagem());
        resposta.setTopico(topico);
        resposta.setAutor(autor);
        resposta.setSolucao(dtoRespostaRequest.solucao());

        respostaService.gravarResposta(resposta);

        if (topico.getStatus() == Status.NAO_RESPONDIDO) {
            long quantidade = respostaService.verificarRespostas(topico.getId());
            if (quantidade == 1) { // se for a primeira resposta
                topico.setStatus(Status.RESPONDIDO);
            }
        }

        var uri = uriComponentsBuilder.path("/respostas/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DtoTopicoResponse(topico));
    }
}
