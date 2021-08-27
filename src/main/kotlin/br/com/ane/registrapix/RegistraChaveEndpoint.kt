package br.com.ane.registrapix

import br.com.ane.*
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

//@ErrorHandler
@Singleton
class RegistraChaveEndpoint(@Inject private val service: NovaChavePixService) :
    DesafioRegistraPixServiceGrpc.DesafioRegistraPixServiceImplBase() {


    override fun registra(
        request: RegistraChavePixRequest,
        responseObserver: StreamObserver<RegistraChavePixResponse>
    ) {
        val novaChave = request.toModel()
        val chaveCriada = service.registra(novaChave)

        responseObserver.onNext(
            RegistraChavePixResponse.newBuilder()
                .setClienteId(chaveCriada.clienteId.toString())
                .setPixId(chaveCriada.id.toString())
                .build()
        )
        responseObserver.onCompleted()
    }
}


fun RegistraChavePixRequest.toModel(): NovaChavePix {
    return NovaChavePix(
        clienteId = clienteId,
        tipo = when (tipoChave) {
            TipoChave.UNKNOWN_TIPO_CHAVE -> throw IllegalArgumentException("Tipo de Chave desconhecida")
            else -> br.com.ane.enums.TipoChave.valueOf(tipoChave.name)
        },
        chave = chave,
        tipoConta = when (tipoConta) {
            TipoConta.UNKNOWN_TIPO_CONTA -> throw IllegalArgumentException("Tipo de Conta desconhecida")
            else -> TipoConta.valueOf(tipoConta.name)
        }
    )
}




