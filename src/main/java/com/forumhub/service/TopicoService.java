package com.forumhub.service;

import com.forumhub.domain.resposta.DtoResposta;
import com.forumhub.domain.topico.DtoDetalheTopico;
import com.forumhub.domain.topico.Topico;
import com.forumhub.domain.topico.TopicoRepository;
import com.forumhub.validacoes.ValidacaoException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TopicoService {

    private final TopicoRepository topicoRepository;

    public TopicoService(TopicoRepository topicoRepository) {
        this.topicoRepository = topicoRepository;
    }

    public DtoDetalheTopico obterDetalheTopico(Long id) {
        Optional<Topico> topico = topicoRepository.findById(id);
        if (topico.isPresent()) {
            Topico t = topico.get();
            List<DtoResposta> respostas = t.getRespostas().stream()
                    .map(resposta -> new DtoResposta(
                            resposta.getId(),
                            resposta.getMensagem(),
                            resposta.getDataCriacao().toString(),
                            resposta.getAutor().getNome(),
                            resposta.getSolucao()
                    ))
                    .collect(Collectors.toList());

            return new DtoDetalheTopico(t.getId(), t.getTitulo(), t.getMensagem(), t.getDataCriacao().toString(),
                    t.getStatus().name(), t.getAutor().getNome(), t.getCurso().getNome(), respostas);

        }
        return null;
    }

    public Page<DtoDetalheTopico> obterListaTopicos(Pageable paginacao) {

        Page<Topico> page = topicoRepository.findAll(paginacao);

        return page.map(topico -> new DtoDetalheTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getDataCriacao().toString(),
                topico.getStatus().name(),
                topico.getAutor().getNome(),
                topico.getCurso().getNome(),
                topico.getRespostas().stream()
                        .map(resposta -> new DtoResposta(
                                resposta.getId(),
                                resposta.getMensagem(),
                                resposta.getDataCriacao().toString(),
                                resposta.getAutor().getNome(),
                                resposta.getSolucao()
                        ))
                        .toList()
        ));
    }

    public void validarTituloTopicoEMensagem(String mensagem, String titulo) {

        Integer existe = topicoRepository.existeTituloEMensagem(mensagem, titulo);

        if (existe == 1) {
            throw new ValidacaoException("Já existe tópico tratando do assunto...!");
        }
    }

    public Topico validarIdTopico(@Valid Long id) {

        Optional<Topico> topico = topicoRepository.findById(id);

        if (topico.isPresent()){
            return topico.get();
        }
        return new Topico();
    }
}
