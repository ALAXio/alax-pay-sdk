package io.alax.sdk.pay

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.google.gson.GsonBuilder
import io.alax.sdk.pay.model.*
import io.alax.sdk.pay.rest.Endpoints
import io.alax.sdk.pay.rest.GetTransaction
import io.alax.sdk.pay.rest.OperationTypeFactory
import io.reactivex.Single
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object AlaxPay {

  const val PARAM_SDK_VERSION = "param_sdk_version"
  const val PARAM_RECEIVER = "param_receiver"
  const val PARAM_AMOUNT = "param_amount"
  const val PARAM_ASSET_SYMBOL = "param_asset_symbol"
  const val PARAM_RESULT_TRX_BLOCK_NUM = "param_result_trx_block_num"
  const val PARAM_RESULT_TRX_IN_BLOCK = "param_result_trx_in_block"
  const val PARAM_RESULT_ERROR = "param_result_error"
  const val SDK_VERSION: String = BuildConfig.VERSION_NAME
  const val PARAM_API_KEY = "param_api_key"
  const val PARAM_APP_BUNDLE_ID = "param_app_bundle_id"

  private const val ACTION_PAY = "io.alax.wallet.app.action.PAY"
  private const val DCORE_URL = "https://socket.decentgo.com:8090/"

  private val gson = GsonBuilder()
      .disableHtmlEscaping()
      .registerTypeAdapterFactory(OperationTypeFactory)
      .create()

  private lateinit var service: Endpoints

  private fun isTransferInputValid(input: TransferInput): Boolean =
    input.amount >= input.asset.minAmountToPay

  /**
   * Init the AlaxStore SDK
   *
   * @param dcoreUrl DCore node url serving rpc API
   * @param client an OkHttpClient performing net requests
   */
  @JvmStatic @JvmOverloads
  fun init(dcoreUrl: HttpUrl = HttpUrl.parse(DCORE_URL)!!, client: OkHttpClient = DefaultConfig.HTTP_CLIENT) {
    service = Retrofit.Builder()
        .baseUrl(dcoreUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()
        .create(Endpoints::class.java)
  }

  @JvmField
  val Ui = object : UiContract {
    override fun requestTransferActivity(input: TransferInput, activity: Activity, requestCode: Int) {
      if (!isTransferInputValid(input)) {
        throw InvalidAmountException(input.amount, input.asset)
      }

      val intent = Intent(ACTION_PAY).apply {
        putExtra(PARAM_SDK_VERSION, SDK_VERSION)
        putExtra(PARAM_RECEIVER, input.receiver)
        putExtra(PARAM_AMOUNT, input.amount.toString())
        putExtra(PARAM_ASSET_SYMBOL, input.asset.symbol)
        putExtra(PARAM_API_KEY, input.apiKey)
        putExtra(PARAM_APP_BUNDLE_ID, input.appBundleId)
      }
      if (intent.resolveActivity(activity.packageManager) != null) {
        activity.startActivityForResult(intent, requestCode)
      } else {
        Toast.makeText(activity, activity.getString(R.string.error_no_app), Toast.LENGTH_SHORT).show()
      }
    }

    override fun parseActivityResult(intent: Intent): TransactionConfirmation = TransactionConfirmation(
        intent.getLongExtra(PARAM_RESULT_TRX_BLOCK_NUM, -1),
        intent.getLongExtra(PARAM_RESULT_TRX_IN_BLOCK, -1)
    )
    override fun parseActivityError(intent: Intent): String = intent.getStringExtra(PARAM_RESULT_ERROR)
  }

  @JvmField
  val Api = object : ApiContract {
    override fun verifyTransfer(transactionConfirmation: TransactionConfirmation): Single<ProcessedTransaction> =
        service.getTransaction(GetTransaction(transactionConfirmation)).map { it.result() }

  }
}