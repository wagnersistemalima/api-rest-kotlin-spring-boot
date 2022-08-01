package br.com.alura.forum.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
data class Usuario(

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        val nome: String,
        val email: String,
        val password: String,


        // relacionamento muitos para muitos, criacao de uma terceira tabela
        // carregar todas as roles do usuario usando EAGER
        @JsonIgnore
        @ManyToMany(fetch = FetchType.EAGER)
        @JoinColumn(name = "usuario_role")
        val role: List<Role> = mutableListOf()
)
