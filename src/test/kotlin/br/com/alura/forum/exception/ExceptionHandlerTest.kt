package br.com.alura.forum.exception

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletRequest


private const val testMessage = "Test message"

@ExtendWith(MockKExtension::class)
class ExceptionHandlerTest {

    @InjectMockKs
    private lateinit var exceptionHandler: ExceptionHandler

    private val request = MockHttpServletRequest()

    @Test
    fun `deve garantir que uma NotFoundException de entrada retornara 404`() {

        val response = exceptionHandler.handleNotFound(NotFoundException(testMessage), request)

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.status)
        Assertions.assertEquals(testMessage, response.message)
        Assertions.assertEquals(HttpStatus.NOT_FOUND.name, response.error)
    }

    @Test
    fun `deve garantir que um InternalServerError de entrada retornara 500`() {

        val response = exceptionHandler.handleServerError(Exception(testMessage), request)

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.status)
        Assertions.assertEquals(testMessage, response.message)
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.name, response.error)

    }

}