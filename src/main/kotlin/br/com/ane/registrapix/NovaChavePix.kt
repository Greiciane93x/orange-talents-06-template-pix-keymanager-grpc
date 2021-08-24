package br.com.ane.registrapix

import br.com.ane.TipoConta
import br.com.ane.validacao.ValidaChavePix
import br.com.ane.validacao.ValidaUUID
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import br.com.ane.enums.TipoChave as validaTipoChave

@Introspected
@ValidaChavePix
data class NovaChavePix(

    @ValidaUUID
    @field:NotBlank
    val clienteId: String?,
    @field:NotNull
    val tipo: validaTipoChave,
    @field:Size(max = 77)
    val chave: String?,
    @field:NotNull
    var tipoConta: TipoConta,
    ){
    fun toModel(conta: ContaAssociada): ChavePix {
        return ChavePix (
            clienteId = UUID.fromString(this.clienteId),
            tipo = validaTipoChave.valueOf(this.tipo!!.name),
            chave = if(this.tipo == validaTipoChave.ALEATORIA) UUID.randomUUID().toString() else this.chave!!,
            tipoDeConta = TipoConta.valueOf(this.tipoConta!!.name),
            conta = conta
        )
    }
}

