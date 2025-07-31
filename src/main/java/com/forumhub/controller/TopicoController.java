package com.forumhub.controller;

import com.forumhub.domain.curso.Curso;
import com.forumhub.domain.curso.ValidacoesCurso;
import com.forumhub.domain.topico.*;
import com.forumhub.domain.usuario.Usuario;
import com.forumhub.domain.usuario.ValidacoesAutor;
import com.forumhub.validacoes.ValidacaoException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public ResponseEntity cadastrarTopico(@RequestBody @Valid DtoTopicoRequest dto, UriComponentsBuilder uriComponentsBuilder){

        Curso curso = validacoesCurso.validarCurso(dto.idCurso());

        Usuario autor = validacoesAutor.validarAutor(dto.idAutor());

        var topico = new Topico();
        topico.setTitulo(dto.titulo());
        topico.setMensagem(dto.mensagem());
        topico.setCurso(curso);
        topico.setAutor(autor);

        validacoesTopico.validarTituloTopicoEMensagem(topico.getMensagem(), topico.getTitulo());
;
        topicoRepository.save(topico);
        var uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DtoTopicoResponse(topico.getId(), topico.getTitulo(),
                topico.getMensagem(), topico.getDataCriacao()));
    }

    @GetMapping
    public ResponseEntity<Page<DtoListagemTopico>> listar(@PageableDefault(size = 10, sort = {"dataCriacao"}) Pageable paginacao) {
        var page = topicoRepository.findAllByOrderByDataCriacaoAsc(paginacao)
                .map(topico -> new DtoListagemTopico(
                        topico.getId(),
                        topico.getTitulo(),
                        topico.getMensagem(),
                        topico.getStatus().name(),
                        topico.getAutor().getNome(),
                        topico.getCurso().getNome()
                ));
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody DtoAtualizacaoTopico DtoAtualizacaoTopico) {

        //Validação do tópico
        var topico = validacoesTopico.validarIdTopico(id);

        validacoesTopico.validarTituloTopicoEMensagem(DtoAtualizacaoTopico.mensagem(), DtoAtualizacaoTopico.titulo());

        if (DtoAtualizacaoTopico.titulo() != null && !DtoAtualizacaoTopico.titulo().isBlank()) {
            topico.setTitulo(DtoAtualizacaoTopico.titulo());
        }

        if (DtoAtualizacaoTopico.mensagem() != null && !DtoAtualizacaoTopico.mensagem().isBlank()) {
            topico.setMensagem(DtoAtualizacaoTopico.mensagem());
        }

        return ResponseEntity.ok(new DtoDetalheTopico(topico.getId(),
                topico.getTitulo(), topico.getMensagem(), topico.getStatus().name(),
                topico.getAutor().getNome(), topico.getCurso().getNome()));
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

        return ResponseEntity.ok(new DtoDetalheTopico(topico.getId(),
                topico.getTitulo(), topico.getMensagem(), topico.getStatus().name(),
                topico.getAutor().getNome(), topico.getCurso().getNome()));
    }

}
