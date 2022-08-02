package br.com.alura.forum.model

import br.com.alura.forum.dto.TopicoView
import java.time.LocalDate
import java.time.LocalDateTime

object TopicoViewFixture {

    fun build(): TopicoView {
        return TopicoView(
            id = 1,
            titulo = "Kotlin Basico",
            mensagem = "Aprendendo Kotlin basico",
            status = StatusTopico.NAO_RESPONDIDO,
            dataCriacao = LocalDateTime.now(),
            dataAlteracao = LocalDate.now()
        )
    }
}