package br.com.ane.registra

import br.com.ane.ChavePixRequest
import br.com.ane.ChavePixResponse
import br.com.ane.PixServiceGrpc
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RegistraChaveEndpoint(@Inject private val service: NovaChavePixService,) :
    PixServiceGrpc.PixServiceImplBase(){

    override fun criaPix(
        request: ChavePixRequest,
        responseObserver: StreamObserver<ChavePixResponse>
    ) {
        // transformando em um dto
        val novaChave = request.toModel()
        val chaveCriada = service.registra(novaChave)

        responseObserver.onNext(ChavePixResponse.newBuilder()
            .setClienteId(chaveCriada.clienteId.toString())
            .setPixId(chaveCriada.id.toString())
            .build())
        responseObserver.onCompleted()
    }
}

