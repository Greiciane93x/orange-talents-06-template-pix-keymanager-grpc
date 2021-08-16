package br.com.ane.validacao

import javax.validation.Payload
import kotlin.reflect.KClass


annotation class ValidaUUID(
    val message: String = "não é um formato válido de UUID",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)
