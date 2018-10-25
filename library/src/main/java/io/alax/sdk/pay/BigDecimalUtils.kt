package io.alax.sdk.pay

import java.math.BigDecimal

internal fun BigDecimal.decimalPlaces(): Int = Math.max(0, stripTrailingZeros().scale())