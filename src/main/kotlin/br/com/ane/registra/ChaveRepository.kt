package br.com.ane.registra

import io.micronaut.context.annotation.Executable
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface ChaveRepository : JpaRepository<ChavePix, UUID> {

    @Executable
    fun existsByChave(chave: String?) : Boolean
}
