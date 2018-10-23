package io.alax.sdk.pay.rest

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface Endpoints {

  @POST("rpc")
  fun getTransaction(@Body body: GetTransaction): Single<RpcResponse>

}