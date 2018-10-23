package io.alax.sdk.pay.model

import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName

data class ProcessedTransaction(
    @SerializedName("signatures") val signatures: List<String>,
    @SerializedName("extensions") val extensions: List<Any>,
    @SerializedName("operations") val operations: List<TransferOperation>,
    @SerializedName("expiration") val expiration: String,
    @SerializedName("ref_block_num") val refBlockNum: Int,
    @SerializedName("ref_block_prefix") val refBlockPrefix: Long,
    @SerializedName("operation_results") val opResults: List<JsonArray>
)