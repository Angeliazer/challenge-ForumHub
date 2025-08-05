package com.forumhub.domain.topico;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    @Query(value = """
            select exists (select * from topico where mensagem = :mensagem
            and
            titulo = :titulo)
            """
            , nativeQuery = true)
    Integer existeTituloEMensagem(@Param("mensagem") String mensagem, @Param("titulo") String titulo);

    @Query(
            value = """
                SELECT * FROM topico t
                JOIN resposta r
                where t.id = :id and r.topico_id = t.id
            """, nativeQuery = true
    )
    Topico findProcurarPorId(@Param("id") Long id);
    //boolean existsByMensagemAndTitulo(@NotNull String mensagem, @NotNull String titulo);

}
