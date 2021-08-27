package br.com.ane.deletapix

import br.com.ane.exceptions.ChavePixNaoEncontradaException
import br.com.ane.registrapix.ChaveRepository
import io.micronaut.validation.Validated
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional

@Validated
@Singleton
class DeletaChaveService(@Inject val repository: ChaveRepository) {

    @Transactional
    fun remove(
        clienteId: String?, pixId: String?
    ) {
        val uuidPixId = UUID.fromString(pixId)
        val uuidClientId = UUID.fromString(clienteId)

        repository.deleteById(uuidPixId)
        }

    }

