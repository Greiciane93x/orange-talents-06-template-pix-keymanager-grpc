package br.com.ane.registra

import br.com.ane.*
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RegistraChaveEndpoint(@Inject private val service: NovaChavePixService,) :
    DesafioPixServiceGrpc.DesafioPixServiceImplBase(){


    override fun registra(request: ChavePixRequest,
                          responseObserver: StreamObserver<ChavePixResponse>) {
        val novaChave = request.toModel()
        val chaveCriada = service.registra(novaChave)

        responseObserver.onNext(ChavePixResponse.newBuilder()
            .setClienteId(chaveCriada.clienteId.toString())
            .setPixId(chaveCriada.id.toString())
            .build())
        responseObserver.onCompleted()

    }
}

fun ChavePixRequest.toModel(): NovaChavePix {
    return NovaChavePix(
        clienteId = clienteId, tipo = when (tipoChave) {
            TipoChave.UNKNOWN_TIPO_CHAVE -> null
            else -> TipoChave.valueOf(tipoChave.name)
        }!!, chave = chave, tipoConta = when (tipoConta) {
            TipoConta.UNKNOWN_TIPO_CONTA-> null
            else -> TipoConta.valueOf(tipoConta.name)
        }!!
    )
}

