package io.alax.sdk.pay.model

import io.alax.sdk.pay.rest.TrustAllCerts
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

internal object DefaultConfig {
  internal val HTTP_CLIENT = TrustAllCerts.wrap(
      OkHttpClient.Builder()
          .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
  ).build()
}