package io.alax.sdk.pay.model

import java.math.BigDecimal

enum class Asset(val symbol: String, val minAmountToPay: BigDecimal) {
  AIA("AIA", BigDecimal("0.02")),
  ALX("ALX", BigDecimal("0.000002"));

  companion object {
    val DEFAULT = ALX
  }
}