package br.com.ane.registrapix

import br.com.ane.handler.ChavePixExistenteException
import br.com.ane.registra.ContasDeClientesNoItauClient
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
class NovaChavePixService(
    @Inject val repository: ChaveRepository,
    @Inject val itauClient: ContasDeClientesNoItauClient,
) {
    private val Logger = LoggerFactory.getLogger(this::class.java)
    fun registra(novaChave: NovaChavePix): ChavePix {

        if (repository.existsByChave(novaChave.chave)) {
            throw  ChavePixExistenteException("Chave pix já existente")
        }
        val response = itauClient.buscaContaPorTipo(novaChave.clienteId!!, novaChave.tipoConta.name)
        val conta = response.body()?.toModel() ?: throw IllegalStateException("Cliente não encontrado no Itau")
        val chave = novaChave.toModel(conta)
        repository.save(chave)
        return chave
    }
}

