package br.com.ane.registra

import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional

@Validated
@Singleton
class NovaChavePixService(
    @Inject val repository: ChaveRepository,
    @Inject val itauClient: ContasDeClientesNoItauClient,
){
    private val Logger = LoggerFactory.getLogger(this::class.java)

//    @Transactional
    fun registra(novaChave: NovaChavePix): ChavePix {


//         verificando se já existe chave
//        if(repository.existsByChave(novaChave.chave)) throw ChavePixExistenteException("Chave Pix '${novaChave.chave}' existente")

        // busca dados na conta ERP do itau
        val response = itauClient.buscaContaPorTipo(novaChave.clienteId!!, novaChave.tipoConta!!.name)
        val conta = response.body()?.toModel() ?: throw  IllegalStateException("Cliente não encontrado no Itau")

        //salva no banco
        val chave = novaChave.toModel(conta)
        repository.save(chave)
        return chave
    }
}

