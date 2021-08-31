package br.com.ane.client

import br.com.ane.DeletaChavePixRequest
import br.com.ane.DeletaChavePixResponse
import br.com.ane.TipoConta
import br.com.ane.TipoConta.*
import br.com.ane.enums.TipoChave
import br.com.ane.registrapix.ChavePix
import br.com.ane.registrapix.ContaAssociada
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType.APPLICATION_XML
import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client
import java.time.LocalDateTime

@Client("\${integracao.bcb.contas}")
interface IntegracaoBCBClient {

    @Post("/api/v1/pix/keys")
    @Consumes(APPLICATION_XML)
    @Produces(APPLICATION_XML)
    fun criaChavePix(@Body request: CriaChavePixRequest):
                     HttpResponse<CriaChavePixRequest>


    @Delete("/api/v1/pix/keys/{key}")
    @Consumes(APPLICATION_XML)
    @Produces(APPLICATION_XML)
    fun deletaChavePix(@PathVariable("key") key: String,
                       @Body request: DeletaChavePixRequest):
                       HttpResponse<DeletaChavePixResponse>


    @Get("/api/v1/pix/keys/{key}")
    @Consumes(APPLICATION_XML)
    fun buscaPelaChave(@PathVariable("key") key: String):
            HttpResponse<PixKeyDetailsResponse>
}

    data class PixKeyDetailsResponse(
        val keyType: CriaChavePixRequest.PixKeyType,
        val key: String,
        val bankAccount: BankAccount,
        val owner: Owner,
        val createdAt: LocalDateTime
    ) {
        fun toModel(): ChavePixInfo {
            return ChavePixInfo(
                tipo = keyType.domainType!!,
                chave = this.key,
                tipoDeconta = when (this.bankAccount.accountType) {
                    BankAccount.AccountType.CACC -> CONTA_POUPANCA
                    BankAccount.AccountType.SVGS -> CONTA_CORRENTE
                },
                conta = ContaAssociada(
                    instituicao = bankAccount.participant,
                    nomeDoTitular = owner.name,
                    cpfDoTitular = owner.taxIdNumber,
                    agencia = bankAccount.branch,
                    numeroDaConta = bankAccount.accountNumber
                )
            )
        }
    }

    data class Owner(
        val type: OwnerType,
        val name: String?,
        val taxIdNumber: String?,
    ) {
        enum class OwnerType {
            NATURAL_PERSON,
            LEGAL_PERSON
        }
    }

    data class BankAccount(
        val participant: String,
        val branch: String?,
        val accountNumber: String?,
        val accountType: AccountType
    ) {
        enum class AccountType() {
            SVGS,
            CACC;

            companion object {
                fun by(domainType: TipoConta): AccountType {
                    return when (domainType) {
                        CONTA_POUPANCA     ->  CACC
                        CONTA_CORRENTE     ->  SVGS
                        UNKNOWN_TIPO_CONTA -> TODO()
                        UNRECOGNIZED       -> TODO()
                    }
                }
            }
        }
    }

    data class CriaChavePixRequest(
        val keyType: PixKeyType,
        val key: String,
        val bankAccount: BankAccount,
        val owner: Owner
    ) {
        companion object {
            fun of(chave: ChavePix): CriaChavePixRequest {
                return CriaChavePixRequest(
                    keyType = PixKeyType.by(chave.tipo),
                    key = chave.chave!!,
                    bankAccount = BankAccount(
                        participant = ContaAssociada.ITAU_UNIBANCO_ISPB,
                        branch = chave.conta?.agencia,
                        accountNumber = chave.conta?.numeroDaConta,
                        accountType = BankAccount.AccountType.by(chave.tipoDeConta!!),
                    ),
                    owner = Owner(
                        type = Owner.OwnerType.NATURAL_PERSON,
                        name = chave.conta?.nomeDoTitular,
                        taxIdNumber = chave.conta?.cpfDoTitular,
                    ),
                )
            }
        }

        data class CreatePixKeyResponse(
            val keyType: PixKeyType,
            val key: String,
            val bankAccount: BankAccount,
            val owner: Owner,
            val createdAt: LocalDateTime
        )

        enum class PixKeyType(val domainType: TipoChave?) {
            CPF(TipoChave.CPF),
            CNPJ(null),
            PHONE(TipoChave.CELULAR),
            EMAIL(TipoChave.EMAIL),
            RANDOM(TipoChave.ALEATORIA);

            companion object {
                private val mapping = PixKeyType.values().associateBy(PixKeyType::domainType)
                fun by(domainType: TipoChave?): PixKeyType {
                    return mapping[domainType] ?: throw IllegalArgumentException("Chave Pix Inválida")
                }
            }
        }
    }
    data class DeletePixKeyRequest(
        val key: String,
        val participant: String = ContaAssociada.ITAU_UNIBANCO_ISPB
    )
    data class DeletaPixKeyResponse(
        val key: String,
        val participant: String,
        val deletedAt: LocalDateTime
    )


