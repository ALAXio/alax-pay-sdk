package io.alax.sdk.pay.model

import java.math.BigDecimal

data class TransferInput @JvmOverloads constructor(
    val receiver: String,
    val amount: BigDecimal,
    val asset: Asset = Asset.DEFAULT,
    val apiKey: String? = null,
    val appBundleId: String? = null
)