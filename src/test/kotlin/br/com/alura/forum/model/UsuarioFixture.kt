package br.com.alura.forum.model

import kotlin.random.Random

object UsuarioFixture {
    fun build(): Usuario {
        return Usuario(
            id = Random.nextInt().toLong(),
            nome = "Marco Antonio",
            email = "marco@email.com",
            password = "123456789"
        )
    }
}