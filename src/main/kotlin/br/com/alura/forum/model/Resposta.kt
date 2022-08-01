package br.com.alura.forum.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Resposta(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        val mensagem: String,
        val dataCriacao: LocalDateTime = LocalDateTime.now(),

        @ManyToOne       // Resposta pertence a um usuario -> usuario pode ter varias respostas
        val autor: Usuario,

        @ManyToOne       // Resposta pertence a um topico -> topico pode ter varias respostas
        val topico: Topico,

        val solucao: Boolean
)
