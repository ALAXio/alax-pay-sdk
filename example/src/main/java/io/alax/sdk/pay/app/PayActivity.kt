package io.alax.sdk.pay.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import io.alax.sdk.pay.AlaxPay
import io.alax.sdk.pay.model.Asset
import io.alax.sdk.pay.model.ObjectNotFoundException
import io.alax.sdk.pay.model.TransferInput
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_pay.*

class PayActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_pay)

    AlaxPay.init()

    btnTransfer.setOnClickListener {
      if (inputReceiver.text.isEmpty()) {
        Toast.makeText(this@PayActivity, "Receiver not allow empty", Toast.LENGTH_SHORT).show()
      }
      if (inputAmount.text.isEmpty()) {
        Toast.makeText(this@PayActivity, "Input amount not allow empty", Toast.LENGTH_SHORT).show()
      }

      if (inputReceiver.text.isNotEmpty() && inputAmount.text.isNotEmpty()) {
        try {
          AlaxPay.Ui.requestTransferActivity(TransferInput(inputReceiver.text.toString(), inputAmount.text.toString().toBigDecimal(), Asset.ALX), this)
        } catch (e: Exception) {
          Toast.makeText(this@PayActivity, e.message, Toast.LENGTH_SHORT).show()
        }
        btnVerify.isEnabled = false
      }
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (resultCode == Activity.RESULT_OK && data != null) {
      val result = AlaxPay.Ui.parseActivityResult(data)
      textResultBlockNum.text = "Result Ok, Block num: ${result.blockNum}"
      textResultTrxInBlock.text = "Result Ok, Block TrxInBloc: ${result.trxInBlock}"
      btnVerify.isEnabled = true
      btnVerify.setOnClickListener {
        AlaxPay.Api.verifyTransfer(result)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ op -> textResultBlockNum.text = op.toString() }, { if (it is ObjectNotFoundException) textResultBlockNum.text = "Object not found" })
      }
    } else {
      textResultBlockNum.text = "Result Canceled: ${data?.let { AlaxPay.Ui.parseActivityError(it) }}"
    }
  }
}