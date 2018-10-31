package io.alax.sdk.pay.model

import java.math.BigDecimal

class InvalidAmountException(amount: BigDecimal, selectedAsset: Asset) : Exception(
  "Trying to send $amount ${selectedAsset.symbol}. ${selectedAsset.symbol} only supports amounts >= ${selectedAsset.minAmountToPay}"
)