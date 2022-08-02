package br.com.alura.forum.model

import kotlin.random.Random

object TopicoFixture {

    fun build(): Topico {
        return Topico(
            id = Random.nextInt().toLong(),
            titulo = "Kotlin basico",
            mensagem = "Aprendendo Kotlin basico",
            curso = CursoFixture.build(),
            autor = UsuarioFixture.build()
        )
    }
}