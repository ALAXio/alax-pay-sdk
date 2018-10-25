package io.alax.sdk.pay.model

enum class Asset(val symbol: String, val precision: Int) {
  AIA("AIA", 2),
  ALX("ALX", 6);

  companion object {
    val DEFAULT = ALX
  }
}