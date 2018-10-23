package io.alax.sdk.pay.rest

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.internal.Streams
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.alax.sdk.pay.model.TransferOperation

@Suppress("UNCHECKED_CAST")
object OperationTypeFactory : TypeAdapterFactory {
  override fun <T : Any?> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
    if (type.rawType == TransferOperation::class.javaObjectType) {
      return object : TypeAdapter<T>() {
        override fun write(out: JsonWriter, value: T?) {
        }

        override fun read(reader: JsonReader): T? {
          val el = Streams.parse(reader)
          val obj = el.asJsonArray[1].asJsonObject
          val delegate = gson.getDelegateAdapter(this@OperationTypeFactory, TypeToken.get(TransferOperation::class.java))
          return delegate.fromJsonTree(obj) as T?
        }
      }
    }
    return null
  }
}
