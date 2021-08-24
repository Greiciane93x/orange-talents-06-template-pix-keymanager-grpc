package br.com.ane.validacao

import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.ReportAsSingleViolation
import javax.validation.constraints.Pattern
import kotlin.reflect.KClass

@ReportAsSingleViolation
@Constraint(validatedBy = [])
@Pattern(regexp = "[a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8}$",
    flags = [Pattern.Flag.CASE_INSENSITIVE] )
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidaUUID(
    val message: String = "não é um formato válido de UUID",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)
