package br.com.ane.registra

import java.lang.RuntimeException

fun ChavePixExistenteException(s: String) : RuntimeException{
    throw RuntimeException(s)
}
