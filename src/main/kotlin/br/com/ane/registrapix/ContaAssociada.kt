package br.com.ane.registrapix

import javax.persistence.Embeddable

@Embeddable
data class ContaAssociada(
    var instituicao: String?,
    var nomeDoTitular: String?,
    var cpfDoTitular: String?,
    var agencia: String?,
    var numeroDaConta: String?

){
    companion object{
        public val ITAU_UNIBANCO_ISPB: String = "60701190"
    }
}