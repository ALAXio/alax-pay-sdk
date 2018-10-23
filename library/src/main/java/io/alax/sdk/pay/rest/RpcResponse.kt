package io.alax.sdk.pay.rest

import io.alax.sdk.pay.model.ObjectNotFoundException
import io.alax.sdk.pay.model.ProcessedTransaction

data class RpcResponse(
    val id: Int,
    val result: ProcessedTransaction?,
    val error: Error?
) {
  fun result(): ProcessedTransaction = result ?: throw ObjectNotFoundException()
}