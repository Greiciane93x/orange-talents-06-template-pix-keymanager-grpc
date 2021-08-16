package br.com.ane.registra

import br.com.ane.ChavePixRequest
import br.com.ane.TipoChave
import br.com.ane.TipoConta


fun ChavePixRequest.toModel() : NovaChavePix {
    return NovaChavePix(
    clienteId = clienteId,
        tipo = when(tipoChave){
            TipoChave.UNKNOWN_TIPO_CHAVE -> null
            else -> TipoChave.valueOf(tipoChave.name)
        },
        chave = chave,
        tipoConta = when(tipoConta) {
            TipoConta.UNKNOWN_TIPO_CONTA -> null
            else -> TipoConta.valueOf(tipoConta.name)
        }
    )
}