package io.alax.sdk.pay.rest

import okhttp3.OkHttpClient
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object TrustAllCerts {
  fun wrap(builder: OkHttpClient.Builder): OkHttpClient.Builder {
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
      override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()

      @Throws(CertificateException::class)
      override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>,
                                      authType: String) {
      }

      @Throws(CertificateException::class)
      override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>,
                                      authType: String) {
      }
    })

    // Install the all-trusting trust manager
    val sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, trustAllCerts, java.security.SecureRandom())
    // Create an ssl socket factory with our all-trusting manager
    val sslSocketFactory = sslContext.socketFactory

    return builder
        .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        .hostnameVerifier { _, _ -> true }
  }
}
