package com.forumhub.domain.resposta;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RespostaRepository extends JpaRepository<Resposta, Long> {
    long countByTopicoId(Long id);
}
