package br.com.ane.registrapix

import br.com.ane.client.ContasDeClientesNoItauClient
import br.com.ane.client.CriaChavePixRequest
import br.com.ane.client.IntegracaoBCBClient
import br.com.ane.exceptions.ChavePixExistenteException
import io.micronaut.http.HttpStatus
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NovaChavePixService(
    @Inject val repository: ChaveRepository,
    @Inject val itauClient: ContasDeClientesNoItauClient,
    @Inject val bcbClient: IntegracaoBCBClient
) {
    private val Logger = LoggerFactory.getLogger(this::class.java)

    fun registra(novaChave: NovaChavePix): ChavePix {

        if (repository.existsByChave(novaChave.chave)) {
            throw  ChavePixExistenteException("Chave pix já existente: `${novaChave.chave}`")
        }
        val response = itauClient.buscaContaPorTipo(novaChave.clienteId!!, novaChave.tipoConta.name)
        val conta = response.body()?.toModel() ?: throw IllegalStateException("Cliente não encontrado no Itau")

        val chave = novaChave.toModel(conta)
        repository.save(chave)


        val bcbRequest = CriaChavePixRequest.of(chave).also {
            Logger.info("Registrando chave Pix no Banco Central do Brasil (BCB): $it")
        }

        val bcbResponse = bcbClient.criaChavePix(bcbRequest)

        if(bcbResponse.status != HttpStatus.CREATED){
            throw IllegalArgumentException("Erro ao registrar chave Pix no Banco Central")
        }
        return chave
    }
}

