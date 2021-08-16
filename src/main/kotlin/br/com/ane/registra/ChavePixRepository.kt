package br.com.ane.registra

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import java.util.*

@Repository
interface ChavePixRepository : CrudRepository<ChavePix, UUID> {
     fun existsByChave(chave: String?): Boolean
}
