package br.com.ane.registra

import br.com.ane.TipoChave
import br.com.ane.TipoConta
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

//@ValidPixKey
@Introspected
data class NovaChavePix(
//    @ValidUUID
    @field:NotBlank
    val clienteId: String?,
    @field:NotNull
    val tipo: TipoChave,
    @field:Size(max = 77)
    val chave: String?,
    @field:NotNull
    var tipoConta: TipoConta,

    ){

    fun toModel(conta: ContaAssociada): ChavePix {
        return ChavePix (
          clienteId = UUID.fromString(this.clienteId),
            tipo = TipoChave.valueOf(this.tipo!!.name),
            chave = if(this.tipo == TipoChave.ALEATORIA) UUID.randomUUID().toString() else this.chave!!,
            tipoDeConta = TipoConta.valueOf(this.tipoConta!!.name),
            conta = conta
        )
    }
}