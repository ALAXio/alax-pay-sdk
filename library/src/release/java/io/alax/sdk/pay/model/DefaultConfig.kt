package io.alax.sdk.pay.model

import okhttp3.OkHttpClient

internal object DefaultConfig {
  internal val HTTP_CLIENT = OkHttpClient.Builder().build()
}