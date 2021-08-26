package br.com.ane.registrapix

import br.com.ane.*
import br.com.ane.handler.ErrorHandler

import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

//@ErrorHandler
@Singleton
class RegistraChaveEndpoint(@Inject private val service: NovaChavePixService) :
    DesafioPixServiceGrpc.DesafioPixServiceImplBase() {

    override fun registra(
        request: ChavePixRequest,
        responseObserver: StreamObserver<ChavePixResponse>
    ) {
        val novaChave = request.toModel()
        val chaveCriada = service.registra(novaChave)

        responseObserver.onNext(
            ChavePixResponse.newBuilder()
                .setClienteId(chaveCriada.clienteId.toString())
                .setPixId(chaveCriada.id.toString())
                .build()
        )
        responseObserver.onCompleted()
    }
}



