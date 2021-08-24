package br.com.ane.handler

import com.google.rpc.Code

fun invalidArgumentHandler(message: String?): com.google.rpc.Status =
    com.google.rpc.Status.newBuilder()
        .setCode(Code.INVALID_ARGUMENT.number)
        .setMessage(message)
        .build()

fun failedPreconditionHandler(message: String?): com.google.rpc.Status =
    com.google.rpc.Status.newBuilder()
        .setCode(Code.FAILED_PRECONDITION.number)
        .setMessage(message)
        .build()

fun defaultHandler(message: String?): com.google.rpc.Status =
    com.google.rpc.Status.newBuilder()
        .setCode(Code.UNKNOWN.number)
        .setMessage(message)
        .build()

fun alreadyExistsHandler(message: String?): com.google.rpc.Status =
    com.google.rpc.Status.newBuilder()
        .setCode(Code.ALREADY_EXISTS.number)
        .setMessage(message)
        .build()