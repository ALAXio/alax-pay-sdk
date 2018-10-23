package io.alax.sdk.pay.model

import com.google.gson.annotations.SerializedName

/**
 * Transfer operation constructor
 *
 * @param from account object id of the sender
 * @param to account object id or content object id of the receiver
 * @param amount an amount and asset type to transfer
 */
data class TransferOperation(
    @SerializedName("from") val from: String,
    @SerializedName("to") val to: String,
    @SerializedName("amount") val amount: AssetAmount
)
