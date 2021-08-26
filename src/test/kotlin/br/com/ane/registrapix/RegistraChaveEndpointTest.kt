package br.com.ane.registrapix

import br.com.ane.ChavePixRequest
import br.com.ane.DesafioPixServiceGrpc
import br.com.ane.TipoChave
import br.com.ane.TipoConta
import br.com.ane.registra.ContasDeClientesNoItauClient
import br.com.ane.registra.DadosDaContaResponse
import br.com.ane.registra.InstituicaoResponse
import br.com.ane.registra.TitularResponse
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.http.HttpResponse
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.hibernate.validator.internal.util.Contracts.assertTrue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.*

@MicronautTest(transactional = false)
internal class RegistraChaveEndpointTest(
    val repository: ChaveRepository,
    val grpcClient: DesafioPixServiceGrpc.DesafioPixServiceBlockingStub,
    val itauClient: ContasDeClientesNoItauClient
) {
    companion object {
        val Client_ID = UUID.randomUUID()
    }
    @BeforeEach
    fun setup() {
        repository.deleteAll()
    }

    @Test
    fun `deve registrar chave pix`() {
        `when`(itauClient.buscaContaPorTipo(clienteId = UUID.randomUUID().toString(), tipo = "CONTA_CORRENTE"))
            .thenReturn(HttpResponse.ok())
        repository.save(
            ChavePix(
                tipo = br.com.ane.enums.TipoChave.CPF,
                chave = "02467781054",
                clienteId = UUID.randomUUID(),
                tipoDeConta = TipoConta.CONTA_CORRENTE,
                conta = dadosDaContaResponse().toModel()
            )
        )
        assertEquals(1, repository.count())
    }
    @Test
    fun `nao deve registrar chave pix existente`() {
       repository.save(
            ChavePix(
                tipo = br.com.ane.enums.TipoChave.CPF,
                chave = "41531952860",
                clienteId = Client_ID,
                tipoDeConta = TipoConta.CONTA_CORRENTE,
                conta = dadosDaContaResponse().toModel()
            )
        )
        val testKey = repository.existsByChave("41531952860")
        assertTrue(testKey, Status.ALREADY_EXISTS.toString())
    }

    @Test
    fun `nao deve registrar chave pix que nao encontrar dados de conta no client`() {

        `when`(itauClient.buscaContaPorTipo(clienteId = Client_ID.toString(), tipo = "CONTA_CORRENTE"))
            .thenReturn(HttpResponse.notFound())

        val thrown = assertThrows<StatusRuntimeException> {
            grpcClient.registra(
                ChavePixRequest.newBuilder()
                    .setClienteId(Client_ID.toString())
                    .setTipoChave(TipoChave.EMAIL)
                    .setChave("ane@teste.com")
                    .setTipoConta(TipoConta.CONTA_CORRENTE)
                    .build()
            )
        }
        with(thrown) {
            assertEquals(Status.FAILED_PRECONDITION.code, status.code)
            assertEquals("Cliente não encontrado no Itau", status.description)
        }
    }

    @MockBean(ContasDeClientesNoItauClient::class)
    fun itauClient(): ContasDeClientesNoItauClient? {
        return Mockito.mock(ContasDeClientesNoItauClient::class.java)
    }

    @Factory
    class Clients {
        @Bean
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): DesafioPixServiceGrpc.DesafioPixServiceBlockingStub {
            return DesafioPixServiceGrpc.newBlockingStub(channel)
        }
    }



    private fun dadosDaContaResponse(): DadosDaContaResponse {
        return DadosDaContaResponse(
            tipo = "CONTA_CORRENTE",
            instituicao = InstituicaoResponse("UNIBANCO ITAU SA", "ITAU_UNIBANCO_ISBP"),
            agencia = "1218",
            numero = "291900",
            titular = TitularResponse("RAFAEL M C PONTE", "41531952860")
        )
    }
}






