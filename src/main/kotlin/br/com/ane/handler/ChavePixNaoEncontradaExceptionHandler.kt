package br.com.ane.handler

import br.com.ane.exceptions.ChavePixNaoEncontradaException
import io.grpc.Status
import javax.inject.Singleton

@Singleton
class ChavePixNaoEncontradaExceptionHandler : ExceptionHandler<ChavePixNaoEncontradaException>{
    override fun handle(e: ChavePixNaoEncontradaException): ExceptionHandler.StatusWithDetails {

        return ExceptionHandler.StatusWithDetails(
            Status.NOT_FOUND
                .withDescription(e.message)
                .withCause(e))
    }

    override fun supports(e: Exception): Boolean {
        return e is ChavePixNaoEncontradaException
    }


}