package br.com.ane.handler

import br.com.ane.exceptions.ChavePixExistenteException
import com.google.rpc.BadRequest
import io.grpc.BindableService
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.protobuf.StatusProto
import io.grpc.stub.StreamObserver
import io.micronaut.aop.InterceptorBean
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import org.slf4j.LoggerFactory
import javax.inject.Singleton
import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException

@Singleton
@InterceptorBean(ErrorHandler::class)
class ExceptionHandlerInterceptor : MethodInterceptor<BindableService, Any?>{
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun intercept(context: MethodInvocationContext<BindableService, Any?>?): Any? {
        logger.info("Interceptando o método: ${context!!.targetMethod}")

        try {

        }catch (error: Exception){
            error.printStackTrace()

            val statusERROR = when(error){
                is IllegalArgumentException -> Status.INVALID_ARGUMENT.withDescription(error.message).asRuntimeException()
                is IllegalStateException -> Status.FAILED_PRECONDITION.withDescription(error.message).asRuntimeException()
                is ChavePixExistenteException -> Status.ALREADY_EXISTS.withDescription(error.message).asRuntimeException()
                is ConstraintViolation<*> -> handleConstraintViolationException(error as ConstraintViolationException)
                else -> Status.UNKNOWN.withDescription("error unexpected").asRuntimeException()
            }
            val responseObserver = (context.parameterValues[1] as StreamObserver<*>).apply {
                onError(statusERROR)
            }
            return null
        }
        return false
    }

    private fun handleConstraintViolationException(error: ConstraintViolationException): StatusRuntimeException {
        error.printStackTrace()

        val violations = error.constraintViolations.map {
            BadRequest.FieldViolation.newBuilder()
                .setField(it.propertyPath.last().name)
                .setDescription(it.message)
                .build()
        }
        val details = BadRequest.newBuilder()
            .addAllFieldViolations(violations)
            .build()

        val statusProto = com.google.rpc.Status.newBuilder()
            .setCode(Status.INVALID_ARGUMENT.code.value())
            .setMessage("parametros de entrada invalidos")
            .addDetails(com.google.protobuf.Any.pack(details))
            .build()

        logger.error("status Proto = ${statusProto}")

        val exception = StatusProto.toStatusRuntimeException(statusProto)
        return exception
    }

}
