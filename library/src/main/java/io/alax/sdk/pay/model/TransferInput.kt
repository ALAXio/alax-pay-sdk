package io.alax.sdk.pay.model

import java.math.BigDecimal

data class TransferInput @JvmOverloads constructor(
    val receiver: String,
    val amount: BigDecimal,
    val asset: Asset = Asset.DEFAULT,
    val xApiKey: String? = null
) {
  companion object {
    fun build(receiver: String, amount: BigDecimal, assetString: String, xApiKey: String) =
      TransferInput(receiver, amount, Asset.getAsset(assetString), xApiKey)
  }
}