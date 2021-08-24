package br.com.ane.registrapix

import br.com.ane.ChavePixRequest
import br.com.ane.TipoChave
import br.com.ane.TipoConta

fun ChavePixRequest.toModel(): NovaChavePix {
    return NovaChavePix(
        clienteId = clienteId,
        tipo = when (tipoChave) {
            TipoChave.UNKNOWN_TIPO_CHAVE -> throw
                    IllegalArgumentException("Tipo de Chave desconhecida")
            else -> br.com.ane.enums.TipoChave.valueOf(tipoChave.name)
        }, chave = chave, tipoConta = when (tipoConta) {
            TipoConta.UNKNOWN_TIPO_CONTA-> throw
                    IllegalArgumentException("Tipo de Conta desconhecida")
            else -> TipoConta.valueOf(tipoConta.name)
        }
    )
}
