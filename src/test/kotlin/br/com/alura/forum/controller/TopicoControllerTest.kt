package br.com.alura.forum.controller

import br.com.alura.forum.config.JWTUtil
import br.com.alura.forum.model.Role
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

// teste controller utilizando spring security test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class TopicoControllerTest {

    @Autowired                    // contexto da nossa aplicação é um web application context
    private lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    private lateinit var jwtUtil: JWTUtil

    private lateinit var mockMvc: MockMvc

    private var token: String? = null

    companion object {
        private const val RECURSO = "/topicos/"
        private const val RECURSO_ID = RECURSO.plus("%s")
    }

    @BeforeEach
    internal fun setUp() {
        token = gerarToken()
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply<DefaultMockMvcBuilder>(
                SecurityMockMvcConfigurers.springSecurity()
            ).build()
    }

    @Test
    fun `deve retornar status 400 quando chamar topicos sem token `() {

        mockMvc.get(RECURSO)
            .andExpect { status { is4xxClientError() } }
    }

    @Test
    fun `deve retornar status 200 quando chamar topicos com token` () {

        mockMvc.get(RECURSO) {
            headers { token?.let { this.setBearerAuth(it) } }
        }.andExpect { status { is2xxSuccessful() } }

    }

    @Test
    fun `deve retornar 200 quando chamar topico por id com token`() {
        mockMvc.get(RECURSO_ID.format("1")) {
            headers { token?. let { this.setBearerAuth(it) } }
        }.andExpect { status { is2xxSuccessful() } }
    }

    private fun gerarToken(): String? {
        val authorities = mutableListOf(Role(id = 1, nome = "LEITURA_ESCRITA"))
        return jwtUtil.generateToken(username = "ana@email.com", authorities = authorities )
    }
}