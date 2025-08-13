package com.forumhub.service;

import com.forumhub.domain.resposta.Resposta;
import com.forumhub.domain.resposta.RespostaRepository;
import org.springframework.stereotype.Service;

@Service
public class RespostaService {

    private final RespostaRepository respostaRepository;

    public RespostaService(RespostaRepository respostaRepository){
        this.respostaRepository = respostaRepository;
    }

    public void gravarResposta(Resposta resposta) {
        respostaRepository.save(resposta);
    }

    public long verificarRespostas(Long id) {
        return respostaRepository.countByTopicoId(id);
    }
}
