package com.forumhub.controller;

import com.forumhub.domain.curso.Curso;
import com.forumhub.domain.curso.ValidacoesCurso;
import com.forumhub.domain.topico.*;
import com.forumhub.domain.usuario.Usuario;
import com.forumhub.domain.usuario.ValidacoesAutor;
import com.forumhub.validacoes.ValidacaoException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    private final TopicoRepository topicoRepository;
    private final ValidacoesCurso validacoesCurso;
    private final ValidacoesAutor validacoesAutor;
    private final ValidacoesTopico validacoesTopico;

    public TopicoController(TopicoRepository topicoRepository,
                            ValidacoesCurso validacoesCurso,
                            ValidacoesAutor validacoesAutor,
                            ValidacoesTopico validacoesTopico){
        this.topicoRepository = topicoRepository;
        this.validacoesCurso = validacoesCurso;
        this.validacoesAutor = validacoesAutor;
        this.validacoesTopico = validacoesTopico;
    }

    @PostMapping
    @Transactional
    public ResponseEntity cadastrarTopico(@RequestBody @Valid DtoTopicoRequest dtoTopicoRequest, UriComponentsBuilder uriComponentsBuilder){

        Curso curso = validacoesCurso.validarCurso(dtoTopicoRequest.idCurso());

        Usuario autor = validacoesAutor.validarAutor(dtoTopicoRequest.idAutor());

        validacoesTopico.validarTituloTopicoEMensagem(dtoTopicoRequest.mensagem(), dtoTopicoRequest.titulo());

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
                        topico.getDataCriacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                )
        );

    }

    @GetMapping
    public ResponseEntity<Page<DtoDetalheTopico>> listar(@PageableDefault(size = 10, sort = {"dataCriacao"}) Pageable paginacao) {
        var page = topicoRepository.findAllByOrderByDataCriacaoAsc(paginacao)
                .map(topico -> new DtoDetalheTopico(
                        topico.getId(),
                        topico.getTitulo(),
                        topico.getMensagem(),
                        topico.getDataCriacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        topico.getStatus().name(),
                        topico.getAutor().getNome(),
                        topico.getCurso().getNome()
                ));
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody DtoAtualizacaoTopico dtoAtualizacaoTopico) {

        //Validação do tópico
        var topico = validacoesTopico.validarIdTopico(id);

        validacoesTopico.validarTituloTopicoEMensagem(dtoAtualizacaoTopico.mensagem(), dtoAtualizacaoTopico.titulo());

        if (dtoAtualizacaoTopico.titulo() != null && !dtoAtualizacaoTopico.titulo().isBlank()) {
            topico.setTitulo(dtoAtualizacaoTopico.titulo());
        }

        if (dtoAtualizacaoTopico.mensagem() != null && !dtoAtualizacaoTopico.mensagem().isBlank()) {
            topico.setMensagem(dtoAtualizacaoTopico.mensagem());
        }

        return ResponseEntity.ok(new DtoDetalheTopico(topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getDataCriacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                topico.getStatus().name(),
                topico.getAutor().getNome(),
                topico.getCurso().getNome()));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {

        //Validação se id to tópico existe
        var topico = validacoesTopico.validarIdTopico(id);

        if (topico == null) {
            throw  new ValidacaoException("Tópico não existe....!");
        }

        topicoRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {

        var topico = validacoesTopico.validarIdTopico(id);

        return ResponseEntity.ok(new DtoDetalheTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getDataCriacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                topico.getStatus().name(),
                topico.getAutor().getNome(),
                topico.getCurso().getNome()));
    }

}
