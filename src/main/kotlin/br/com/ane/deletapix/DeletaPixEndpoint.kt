package br.com.ane.deletapix

import br.com.ane.DeletaChavePixRequest
import br.com.ane.DeletaChavePixResponse
import br.com.ane.DesafioDeletaPixServiceGrpc
import io.grpc.Status
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeletaPixEndpoint(@Inject private val service: DeletaChaveService,):
    DesafioDeletaPixServiceGrpc.DesafioDeletaPixServiceImplBase()  {

    override fun deleta(request: DeletaChavePixRequest, responseObserver: StreamObserver<DeletaChavePixResponse>) {


        val idClient = request!!.clienteId
        if(idClient == null || idClient.isBlank()){
            val error = Status.INVALID_ARGUMENT
                .withDescription("ID do cliente deve ser informado")
                .asRuntimeException()
            responseObserver?.onError(error)
            return
        }

        service.remove(clienteId = request.clienteId, pixId = request.pixId)

        responseObserver.onNext(DeletaChavePixResponse.newBuilder()
            .setClienteId(request.clienteId)
            .setPixId(request.pixId)
            .build())
        responseObserver.onCompleted()

    }
}