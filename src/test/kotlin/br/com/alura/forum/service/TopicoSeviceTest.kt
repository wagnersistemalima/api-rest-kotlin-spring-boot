package br.com.alura.forum.service

import br.com.alura.forum.exception.NotFoundException
import br.com.alura.forum.mapper.TopicoFormMapper
import br.com.alura.forum.mapper.TopicoViewMapper
import br.com.alura.forum.model.TopicoFixture
import br.com.alura.forum.model.TopicoViewFixture
import br.com.alura.forum.repository.TopicoRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.*

@ExtendWith(MockKExtension::class)
class TopicoSeviceTest {

    @MockK
    private lateinit var topicoRepository: TopicoRepository

    @MockK
    private lateinit var topicoViewMapper: TopicoViewMapper

    @MockK
    private lateinit var topicoFormMapper: TopicoFormMapper

    @MockK
    private lateinit var paginacao: Pageable

    @InjectMockKs
    private lateinit var topicoService: TopicoService


    @Test
    fun `listar , deve listar topicos a partir do nome do curso`() {

        val topicos = PageImpl(listOf(TopicoFixture.build()))

        val topicoView = TopicoViewFixture.build()

        every { topicoRepository.findByCursoNome(any(), any()) } returns topicos

        every { topicoViewMapper.map(any()) } returns topicoView

        topicoService.listar("Kotlin avançado", paginacao)

        verify(exactly = 1) {topicoRepository.findByCursoNome(any(), any())}
        verify(exactly = 0) {topicoRepository.findAll()}
        verify (exactly = 1) {topicoViewMapper.map(any()) }

    }

    @Test
    fun `listar, deve listar todos os topicos quando o nome do curso for nulo`() {

        val topicos = PageImpl(listOf(TopicoFixture.build()))

        val topicoView = TopicoViewFixture.build()

        every { topicoRepository.findAll(paginacao) } returns topicos

        every { topicoViewMapper.map(any()) } returns topicoView

        topicoService.listar(null, paginacao)

        verify(exactly = 0) {topicoRepository.findByCursoNome(any(), any())}
        verify(exactly = 1) {topicoRepository.findAll(paginacao)}
        verify (exactly = 1) {topicoViewMapper.map(any()) }

    }

    @Test
    fun `deve buscar por id um topico`() {

        val topico = TopicoFixture.build()
        val topicoView = TopicoViewFixture.build()

        every { topicoRepository.findById(topico.id!!) } returns Optional.of(topico)
        every { topicoViewMapper.map(any()) } returns topicoView

        Assertions.assertDoesNotThrow { topicoService.buscarPorId(topico.id!!) }

        verify(exactly = 1) {topicoViewMapper.map(any())}
        verify(exactly = 1) {topicoRepository.findById(topico.id!!)}
    }

    @Test
    fun `deve lancar exception not found exception quando nao encontar id do topico`() {

        val idNotExist = 500000L

        every { topicoRepository.findById(idNotExist) } returns Optional.empty()

        val atual = assertThrows<NotFoundException> { topicoService.buscarPorId(idNotExist)  }

        assertThat(atual.message).isEqualTo("Topico nao encontrado!")

        verify(exactly = 0) {topicoViewMapper.map(any())}
        verify(exactly = 1) {topicoRepository.findById(idNotExist)}
    }
}