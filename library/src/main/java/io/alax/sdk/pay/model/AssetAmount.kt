package io.alax.sdk.pay.model

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class AssetAmount constructor(
    @SerializedName("amount") val amount: BigInteger,
    @SerializedName("asset_id") val assetId: String
)