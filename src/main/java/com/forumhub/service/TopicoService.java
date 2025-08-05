package com.forumhub.service;

import com.forumhub.domain.topico.DtoDetalheTopico;
import com.forumhub.domain.topico.Topico;
import com.forumhub.domain.topico.TopicoRepository;
import com.forumhub.validacoes.ValidacaoException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
            return new DtoDetalheTopico(t);
        }
        throw new ValidacaoException("Tópico não existe no banco de dados...!");
    }

    public Page<DtoDetalheTopico> obterListaTopicos(Pageable paginacao) {

        Page<Topico> page = topicoRepository.findAll(paginacao);

        return page.map(topico -> new DtoDetalheTopico(topico));
    }

    public void validarTituloTopicoEMensagem(String mensagem, String titulo) {

        Integer existe = topicoRepository.existeTituloEMensagem(mensagem, titulo);

        if (existe == 1) {
            throw new ValidacaoException("Já existe tópico tratando do assunto...!");
        }
    }

    public Topico validarIdTopico(Long id) {

        Optional<Topico> topico = topicoRepository.findById(id);

        if (topico.isPresent()){
            return topico.get();
        }
        throw new ValidacaoException("Tópico não existe...!");
    }
}
