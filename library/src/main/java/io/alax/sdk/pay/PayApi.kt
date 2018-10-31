package io.alax.sdk.pay

import android.app.Activity
import android.content.Intent
import io.alax.sdk.pay.model.*
import io.reactivex.Single

/**
 * Ui operations
 */
interface UiContract {
  /**
   * Request an Activity to make a transfer, calls [Activity.startActivityForResult].
   * On successful transfer, result is set to [Activity.RESULT_OK] with fields [AlaxPay.PARAM_RESULT_TRX_BLOCK_NUM] and [AlaxPay.PARAM_RESULT_TRX_IN_BLOCK]
   * as Long values in extra.
   * use [parseActivityResult] method to retrieve the TransactionConfirmation model
   *
   * @param input transfer input object
   * @param activity calling Activity on which the [Activity.onActivityResult] gets called
   * @throws [io.alax.sdk.pay.model.InvalidAmountException] when precision of amount exceeds supported precision of token
   */
  @Throws(InvalidAmountException::class)
  fun requestTransferActivity(input: TransferInput, activity: Activity) = requestTransferActivity(input, activity, 0)

  /**
   * Request an Activity to make a transfer, calls [Activity.startActivityForResult].
   * On successful transfer, result is set to [Activity.RESULT_OK] with fields [AlaxPay.PARAM_RESULT_TRX_BLOCK_NUM] and [AlaxPay.PARAM_RESULT_TRX_IN_BLOCK]
   * as Long values in extra.
   * use [parseActivityResult] method to retrieve the TransactionConfirmation model
   *
   * @param input transfer input object
   * @param activity calling Activity on which the [Activity.onActivityResult] gets called
   * @param requestCode request identifier
   * @throws [io.alax.sdk.pay.model.InvalidAmountException] when precision of amount exceeds supported precision of token
   */
  @Throws(InvalidAmountException::class)
  fun requestTransferActivity(input: TransferInput, activity: Activity, requestCode: Int)

  /**
   * Parses the block number [AlaxPay.PARAM_RESULT_TRX_BLOCK_NUM] and transaction number [AlaxPay.PARAM_RESULT_TRX_IN_BLOCK] extras
   * from [Activity.onActivityResult] data intent
   */
  fun parseActivityResult(intent: Intent): TransactionConfirmation

  /**
   * Parses the error [AlaxPay.PARAM_RESULT_ERROR] string extra from [Activity.onActivityResult] data intent
   */
  fun parseActivityError(intent: Intent): String
}

/**
 * Async data operations
 */
interface ApiContract {

  /**
   * Checks the presence of transfer on the DCore blockchain. Emits [ObjectNotFoundException] if purchase is not present.
   *
   * @param transactionConfirmation DCore transfer confirmation containing block number and transaction number in block,
   * retrievable from [AlaxPay.PARAM_RESULT_TRX_BLOCK_NUM] and [AlaxPay.PARAM_RESULT_TRX_IN_BLOCK]
   */
  fun verifyTransfer(transactionConfirmation: TransactionConfirmation): Single<ProcessedTransaction>
}