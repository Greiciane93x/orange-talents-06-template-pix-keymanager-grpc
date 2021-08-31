package br.com.ane.deletapix


import br.com.ane.exceptions.ChavePixNaoEncontradaException
import br.com.ane.registrapix.ChaveRepository
import br.com.ane.validacao.ValidaUUID
import io.micronaut.validation.Validated
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional

@Validated
@Singleton
class DeletaChaveService(@Inject val repository: ChaveRepository,
                       ){

    @Transactional
    fun remove(
         @ValidaUUID(message = "Cliente ID com formato inválido") clienteId: String?,
         @ValidaUUID (message = "Pix ID com formato inválido") pixId: String?,
    ) {
        val uuidPixId = UUID.fromString(pixId)
        val uuidClienteId = UUID.fromString(clienteId)

        val chave = repository.existsByIdAndClienteId(uuidClienteId, uuidPixId)

        if(!chave){
            ChavePixNaoEncontradaException("Chave Pix não encontrada, ou não pertencente ao cliente")
        }
        repository.deleteById(uuidPixId)
    }
}


