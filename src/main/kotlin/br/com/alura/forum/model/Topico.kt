package br.com.alura.forum.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Topico(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        var titulo: String,
        var mensagem: String,
        val dataCriacao: LocalDateTime = LocalDateTime.now(),

        @ManyToOne                        // Topico pertence a um curso -> Curso tem varios topicos -> chave estrangeira para tabela curso
        val curso: Curso,

        @ManyToOne                       // Topico pertence a um usuario -> Usuario tem varios topicos
        val autor: Usuario,

        @Enumerated(value = EnumType.STRING)  // vai salvar na coluna status no banco o nome da contante
        val status: StatusTopico = StatusTopico.NAO_RESPONDIDO,

        @OneToMany(mappedBy = "topico")      // Um Topico tem varias respostas -> OneToMany bidirecional mappedBy
        val respostas: List<Resposta> = ArrayList()
)