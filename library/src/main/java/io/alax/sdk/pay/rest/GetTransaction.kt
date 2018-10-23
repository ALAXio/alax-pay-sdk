package io.alax.sdk.pay.rest

import io.alax.sdk.pay.model.TransactionConfirmation

class GetTransaction(transactionConfirmation: TransactionConfirmation) {
  val method: String = "get_transaction"
  val params: List<Any> = listOf(transactionConfirmation.blockNum, transactionConfirmation.trxInBlock)
  val jsonrpc: String = "2.0"
  val id: Int = 1
}