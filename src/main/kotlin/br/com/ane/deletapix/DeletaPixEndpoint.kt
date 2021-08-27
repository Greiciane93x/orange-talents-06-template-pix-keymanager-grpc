package br.com.ane.deletapix

import br.com.ane.DeletaChavePixRequest
import br.com.ane.DeletaChavePixResponse
import br.com.ane.DesafioDeletaPixServiceGrpc
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeletaPixEndpoint(@Inject private val service: DeletaChaveService,):
    DesafioDeletaPixServiceGrpc.DesafioDeletaPixServiceImplBase()  {

    override fun deleta(request: DeletaChavePixRequest, responseObserver: StreamObserver<DeletaChavePixResponse>) {

        service.remove(clienteId = request.clienteId, pixId = request.pixId)

        responseObserver.onNext(DeletaChavePixResponse.newBuilder()
            .setClienteId(request.clienteId)
            .setPixId(request.pixId)
            .build())
        responseObserver.onCompleted()
    }
}