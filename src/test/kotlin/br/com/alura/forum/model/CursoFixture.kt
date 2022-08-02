package br.com.alura.forum.model

import kotlin.random.Random

object CursoFixture {

    fun build(): Curso {
        return Curso(
            id = Random.nextInt().toLong(),
            nome = "Kotlin test",
            categoria = "Progamação"
        )
    }
}