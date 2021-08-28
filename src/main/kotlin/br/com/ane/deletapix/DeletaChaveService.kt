package br.com.ane.deletapix

import br.com.ane.exceptions.ChavePixNaoEncontradaException
import br.com.ane.registra.ContasDeClientesNoItauClient
import br.com.ane.registrapix.ChaveRepository
import br.com.ane.validacao.ValidaUUID
import io.micronaut.validation.Validated
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional

@Validated
@Singleton
class DeletaChaveService(@Inject val repository: ChaveRepository){

    @Transactional
    fun remove(
        clienteId: String?, pixId: String?
    ) {
        @ValidaUUID val uuidPixId = UUID.fromString(pixId)
        @ValidaUUID val uuidClientId = UUID.fromString(clienteId)
        try {
            repository.deleteById(uuidPixId)

        } catch (e: ChavePixNaoEncontradaException) {
            e.printStackTrace()
        }
    }
}

