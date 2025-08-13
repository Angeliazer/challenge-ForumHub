package com.forumhub.controller;

import com.forumhub.domain.curso.Curso;
import com.forumhub.domain.topico.*;
import com.forumhub.domain.usuario.Usuario;
import com.forumhub.service.AutorService;
import com.forumhub.service.CursoService;
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


@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {

    private final CursoService cursoService;
    private final AutorService autorService;
    private final TopicoService topicoService;

    public TopicoController(CursoService cursoService,
                            AutorService autorService,
                            TopicoService topicoService){
        this.cursoService = cursoService;
        this.autorService = autorService;
        this.topicoService = topicoService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity cadastrarTopico(@RequestBody @Valid DtoTopicoRequest dtoTopicoRequest, UriComponentsBuilder uriComponentsBuilder){

        Curso curso = cursoService.validarCurso(dtoTopicoRequest.idCurso());

        Usuario autor = autorService.validarAutor(dtoTopicoRequest.idAutor());

        topicoService.validarTituloTopicoEMensagem(dtoTopicoRequest.mensagem(), dtoTopicoRequest.titulo());

        var topico = new Topico();
        topico.setTitulo(dtoTopicoRequest.titulo());
        topico.setMensagem(dtoTopicoRequest.mensagem());
        topico.setCurso(curso);
        topico.setAutor(autor);

        topicoService.gravarTopico(topico);
        var uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DtoTopicoResponse(topico));
    }

    @GetMapping
    public ResponseEntity<Page<DtoDetalheTopico>> listar(@Valid @PageableDefault(size = 10, sort = "dataCriacao") Pageable paginacao) {

            Page<DtoDetalheTopico> page = topicoService.obterListaTopicos(paginacao);

            if (page.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity atualizar(@Valid @PathVariable Long id, @RequestBody DtoAtualizacaoTopico dtoAtualizacaoTopico) {

        //Validação do tópico
        var topico = topicoService.validarIdTopico(id);

        topicoService.validarTituloTopicoEMensagem(dtoAtualizacaoTopico.mensagem(), dtoAtualizacaoTopico.titulo());

        if (dtoAtualizacaoTopico.titulo() != null && !dtoAtualizacaoTopico.titulo().isBlank()) {
            topico.setTitulo(dtoAtualizacaoTopico.titulo());
        }

        if (dtoAtualizacaoTopico.mensagem() != null && !dtoAtualizacaoTopico.mensagem().isBlank()) {
            topico.setMensagem(dtoAtualizacaoTopico.mensagem());
        }

        return ResponseEntity.ok(new DtoDetalheTopico(topico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    //@PreAuthorize("hasRole('ADMIN')")  //Só usuários logados com perfil ADMIN podem excluir...
    public ResponseEntity excluir(@Valid @PathVariable Long id) {

        //Validação se id to tópico existe
        var topico = topicoService.validarIdTopico(id);

        if (topico.getId() == null) {
            throw new ValidacaoException("Tópico não existe....!");
        }

        topicoService.deletarTopico(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@Valid @PathVariable Long id) {

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        UserDetails userDetails = (Usuario) authentication.getPrincipal();
//        var subject = userDetails.getUsername();
//        var idUsuario = userDetails.getAuthorities();
//
//        System.out.println(idUsuario);

        return ResponseEntity.ok(topicoService.obterDetalheTopico(id));

    }

}
