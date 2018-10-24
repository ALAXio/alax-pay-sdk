package io.alax.sdk.pay.model

class InvalidPrecisionException(val selectedAsset: Asset) : Exception(
  "${selectedAsset.symbol} only supports amounts with precision of ${selectedAsset.precision} decimal places"
)