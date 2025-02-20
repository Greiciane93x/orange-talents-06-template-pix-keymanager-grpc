package br.com.ane.registrapix

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface ChaveRepository : JpaRepository<ChavePix, UUID> {
    fun existsByChave(chave: String?): Boolean
    fun existsByIdAndClienteId(fromString: UUID?, clienteId: UUID): Boolean
//    fun findAllByClienteId(clienteId: UUID): List<ChavePix>

}
