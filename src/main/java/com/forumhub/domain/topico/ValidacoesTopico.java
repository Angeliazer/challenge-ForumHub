package com.forumhub.domain.topico;

import com.forumhub.domain.curso.Curso;
import com.forumhub.validacoes.ValidacaoException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class ValidacoesTopico {

    private final TopicoRepository topicoRepository;
    public ValidacoesTopico(TopicoRepository topicoRepository){
        this.topicoRepository = topicoRepository;
    }

    public void validarTituloTopicoEMensagem(String mensagem, String titulo){

        //boolean existe = topicoRepository.existsByMensagemAndTitulo(dados.mensagem(), dados.titulo());

        Integer existe = topicoRepository.existeTituloEMensagem(mensagem, titulo);

        if (existe == 1) {
            throw new ValidacaoException("Já existe tópico tratando do assunto...!");
        }
    }

    public Topico validarIdTopico(@Valid Long id) {

        return topicoRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException("Tópico não encontrado no banco de dados...!"));
    }

}
