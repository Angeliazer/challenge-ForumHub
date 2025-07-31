package com.forumhub.domain.topico;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    @Query(value = """
            select exists (select * from topico where mensagem = :mensagem
            and
            titulo = :titulo)
            """
    , nativeQuery = true)
    Integer existeTituloEMensagem(@Param("mensagem") String mensagem, @Param("titulo") String titulo);

    Page<Topico> findAllByOrderByDataCriacaoAsc(Pageable paginacao);
    //boolean existsByMensagemAndTitulo(@NotNull String mensagem, @NotNull String titulo);

}
