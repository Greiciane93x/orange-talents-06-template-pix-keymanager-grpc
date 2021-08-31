package br.com.ane.deletapix

import br.com.ane.DesafioRegistraPixServiceGrpc
import br.com.ane.client.ContasDeClientesNoItauClient
import br.com.ane.client.IntegracaoBCBClient
import br.com.ane.registrapix.ChaveRepository
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito
import java.util.*

@MicronautTest(transactional = false)
internal class DeletaPixEndpointTest(
    val repository: ChaveRepository,
    val grpcClient: DesafioRegistraPixServiceGrpc.DesafioRegistraPixServiceBlockingStub,
    val itauClient: ContasDeClientesNoItauClient
){
    companion object {
        val client_ID = UUID.randomUUID()
    }
    @BeforeEach
    fun setup(){
        repository.deleteAll()
    }

    @MockBean(ContasDeClientesNoItauClient::class)
    fun itauClient(): ContasDeClientesNoItauClient? {
        return Mockito.mock(ContasDeClientesNoItauClient::class.java)
    }
    @MockBean(IntegracaoBCBClient::class)
    fun BCBClient(): IntegracaoBCBClient? {
        return Mockito.mock(IntegracaoBCBClient::class.java)
    }

    @Factory
    class Clients {
        @Bean
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): DesafioRegistraPixServiceGrpc.DesafioRegistraPixServiceBlockingStub {
            return DesafioRegistraPixServiceGrpc.newBlockingStub(channel)
        }
    }
}