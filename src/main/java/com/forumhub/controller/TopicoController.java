package com.forumhub.controller;

import com.forumhub.domain.curso.Curso;
import com.forumhub.domain.curso.ValidacoesCurso;
import com.forumhub.domain.resposta.DtoResposta;
import com.forumhub.domain.topico.*;
import com.forumhub.domain.usuario.Usuario;
import com.forumhub.domain.usuario.ValidacoesAutor;
import com.forumhub.service.TopicoService;
import com.forumhub.validacoes.ValidacaoException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {

    private final TopicoRepository topicoRepository;
    private final ValidacoesCurso validacoesCurso;
    private final ValidacoesAutor validacoesAutor;
    private final TopicoService topicoService;

    public TopicoController(TopicoRepository topicoRepository,
                            ValidacoesCurso validacoesCurso,
                            ValidacoesAutor validacoesAutor,
                            TopicoService topicoService){
        this.topicoRepository = topicoRepository;
        this.validacoesCurso = validacoesCurso;
        this.validacoesAutor = validacoesAutor;
        this.topicoService = topicoService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity cadastrarTopico(@RequestBody @Valid DtoTopicoRequest dtoTopicoRequest, UriComponentsBuilder uriComponentsBuilder){

        Curso curso = validacoesCurso.validarCurso(dtoTopicoRequest.idCurso());

        Usuario autor = validacoesAutor.validarAutor(dtoTopicoRequest.idAutor());

        topicoService.validarTituloTopicoEMensagem(dtoTopicoRequest.mensagem(), dtoTopicoRequest.titulo());

        var topico = new Topico();
        topico.setTitulo(dtoTopicoRequest.titulo());
        topico.setMensagem(dtoTopicoRequest.mensagem());
        topico.setCurso(curso);
        topico.setAutor(autor);

        topicoRepository.save(topico);
        var uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(
                new DtoTopicoResponse(
                        topico.getId(),
                        topico.getTitulo(),
                        topico.getMensagem(),
                        topico.formataData()
                )
        );
    }

    @GetMapping
    public ResponseEntity<Page<DtoDetalheTopico>> listar(@PageableDefault(size = 10, sort = "dataCriacao") Pageable paginacao) {

            Page<DtoDetalheTopico> page = topicoService.obterListaTopicos(paginacao);

            if (page.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody DtoAtualizacaoTopico dtoAtualizacaoTopico) {

        //Validação do tópico
        var topico = topicoService.validarIdTopico(id);

        topicoService.validarTituloTopicoEMensagem(dtoAtualizacaoTopico.mensagem(), dtoAtualizacaoTopico.titulo());

        if (dtoAtualizacaoTopico.titulo() != null && !dtoAtualizacaoTopico.titulo().isBlank()) {
            topico.setTitulo(dtoAtualizacaoTopico.titulo());
        }

        if (dtoAtualizacaoTopico.mensagem() != null && !dtoAtualizacaoTopico.mensagem().isBlank()) {
            topico.setMensagem(dtoAtualizacaoTopico.mensagem());
        }

        List<DtoResposta> respostas = topico.getRespostas().stream()
                .map(resposta -> new DtoResposta(
                        resposta.getId(),
                        resposta.getMensagem(),
                        resposta.getDataCriacao().toString(),
                        resposta.getAutor().getNome(),
                        resposta.getSolucao()
                ))
                .collect(Collectors.toList());

        var dtoDetalheTopico = new DtoDetalheTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getDataCriacao().toString(),
                topico.getStatus().name(),
                topico.getAutor().getNome(),
                topico.getCurso().getNome(),
                respostas);

        return ResponseEntity.ok(dtoDetalheTopico);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {

        //Validação se id to tópico existe
        var topico = topicoService.validarIdTopico(id);

        if (topico == null) {
            throw new ValidacaoException("Tópico não existe....!");
        }

        topicoRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {

        return ResponseEntity.ok(topicoService.obterDetalheTopico(id));

    }

}
