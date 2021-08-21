package br.com.ane.registra

import javax.persistence.Embeddable

@Embeddable
data class ContaAssociada(
    var instituicao: String? = null,
    var nomeDoTitular: String? = null,
    var cpfDoTitular: String? = null,
    var agencia: String? = null,
    var numeroDaConta: String? = null

)