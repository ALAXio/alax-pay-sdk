# ALAX Pay SDK #

[![Release](https://jitpack.io/v/alaxio/alax-pay-sdk.svg)](https://jitpack.io/#alaxio/alax-pay-sdk)

## Introduction ##

ALAX Pay is a secure online platform for transferring and storing ALAX digital currency. Our mission is to create an open financial system for the gaming world by providing services for sending or receiving ALAX digital currency between online wallets, game players, or game publishers. ALAX Pay is a platform on which many applications are being built using our SDK.

### Installation guide ###
#### Minimum requirements ####
Minimum requirements for the SDK are:
Android SDK Version >= 4.1.
API >= 16.
Android Studio
ALAX Pay application installed on the tested device

Only Android Studio is supported as a development environment.

#### Guide for Android Studio ####

This SDK is distributed via `jitpack` maven repository and can only be used with Android SDK versions >= 4.1（API >= 16).

To import the ALAX Pay SDK into Android Studio, in project level `build.gradle` add jitpack repository to list of repositories:

```
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
        // Other repositories
    }
}
```



Then, add the SDK library as a dependency (`implementation com.github.alaxio:alax-pay-sdk:0.3.0`) and ensure that the following dependencies, required by ALAX Pay SDK are added to the build.gradle file of the application you are working on.

```
dependencies {
    // ...
    implementation 'com.github.alaxio:alax-pay-sdk:0.3.0'
    // ...
}
```

### Components ###

#### ALAX Pay Wallet App ####
The ALAX Pay Wallet is a precompiled application that provides the services that the ALAX Pay SDK utilises. Without it being installed and running, applications built on the ALAX Pay SDK will not function.

In order to test applications written with this SDK, developers will need to create at least two accounts through the wallet so that they can transfer assets between them. You’ll need to contact Alax Support Team via email address `developer@alax.io` to request some tokens for testing.

##### Determining a Wallet Address / Account Name #####

Start the ALAX Pay Wallet application.
Sign in using the  telephone number and password that the desired account was created with..
Tap the `Top Up` button. A QR code for ALXT Top Up will be displayed with the account’s ‘Wallet Address’ displayed in a box below it. This can then be copied to the clipboard using the supplied icon.

The process for a newly created account is slightly different:

Start the ALAX Pay Wallet application.
* If it is already ‘signed in’, you will need to ‘sign out’ by selecting the ‘sign out’ option from the menu accessible via the three vertical dot icon in the top right hand corner of the screen.
* With the Wallet in a ‘signed out’ state, select the `Don’t have an account? Sign Up!` link at the bottom of the display.
* Enter your telephone number, followed by the password, twice. Follow all the other instructions to create an account. After successful sign up you will then be signed in.
* Hit the `Top Up` button to display the QR Code and Wallet Address.

Selecting `Top Up`, as above, will display the Wallet Address / Account Name which, as before, can then be copied to the clipboard using the supplied icon.

#### ALAX Pay SDK ####
The SDK is published via `jitpack` maven repository and exposes a simple interface to transfer funds, and examine responses to those requests.

It needs to be installed according to the instructions outlined in the section, ‘Guide for Android Studio’, above.

#### ALAX Pay Example Application ####
An example application is provided in this repository, with the code illustrating the SDK use in, `app/java/io.alax.sdk.pay.app/PayActivity.kt`.

In the class `PayActivity`, the method `onCreate()` initialises the SDK with a call to `AlaxPay.init()` then sets a click-listener event to call `AlaxPay.Ui.requestTransferActivity()` with values from the GUI form.

`onActivityResult()`  handles the response, displaying the transfer’s block number and number in block, if the transaction was successful, and the message “Result Cancelled”, if it failed.

### Using the ALAX Pay SDK ###

#### Initialization ####
Before any of the SDK methods can be used, the SDK service needs to be registered with the operating system using the statement:
```
	AlaxPay.init()
```

This need only be done once during the duration of the application, and must be done before calling any other SDK methods.

See `fun onCreate()` in the provided example application for context.

#### Available Methods ####
ALAX Pay SDK provides two interfaces - `UI`, which interfaces to the Android GUI framework,  and `API`, which connects to the blockchain.

##### UI Operations (Ui) #####

The UI Interface exposes two methods to register a transfer activity event with the UI, and a callback to handle its result.

The transfer event is registered with:
```
fun requestTransferActivity( input: TransferInput, activity: Activity,
  requestCode: Int = 0)
```

Requests an Activity to make a transfer, calling the provided `Activity.startActivityForResult`.

On successful transfer, the result is set to `[Activity.RESULT_OK]` with two `Long` extras: `[AlaxPay.PARAM_RESULT_TRX_BLOCK_NUM]` representing number of block containing the transfer and `[AlaxPay.PARAM_RESULT_TRX_IN_BLOCK]`, representing number of the transaction within the block.

Use the `parseActivityResult()` method to retrieve the Transaction Confirmation model.

Parameters:

Name | Type | Description | Default
--- | --- | --- | ---
data | TransferInput | Transfer data object |
activity | Activity | The AppCompatActivity object whose onActivityResult method is called upon completion of the request.
requestCode | Int | request identifier | 0


See `fun onCreate` in the provided example application for context.

**Validation of input:**
Upon calling `requestTransferActivity()`, input is validated for minimum amount. Note, that payments in AIA tokens can only be made with amounts >= 0.02 AIA and payments in ALX token support amounts >= 0.000002 AIA. When lower amount is passed into `TransferInput`, `InvalidAmountException` is thrown.

The result of the transfer request can be examined using:

```
fun parseActivityResult(intent: Intent): TransactionConfirmation
```

This method parses the Intent typed parameter that the GUI framework passes to the `Activity.onActivityResult()` method call to obtain the transaction confirmation object

Using the parameter, `intent`, This is, effectively:
```
intent.getStringExtra(PARAM_RESULT_TRX_BLOCK_NUM)
intent.getStringExtra(PARAM_RESULT_TRX_IN_BLOCK)
```

Parameters:

Name | Type | Description | Default
--- | --- | --- | ---
intent | Intent | The Intent object passed to Activity.onActivityResult() by the GUI framework. |

See `fun onActivityResult()` in the provided example application for context.

##### API Operations (Api) #####

The `API` interface exposes a single method that communicates with the blockchain:

```
fun verifyTransfer(transactionConfirmation: TransactionConfirmation): Single<ProcessedTransaction>
```

This checks for the presence of a transfer given a `TransactionConfirmation` object on the blockchain. In the context of this SDK, this is mostly intended to be used to verify that a transfer was, in fact, successfully registered on the blockchain.

If the transfer is not present, `verifyTransfer()` emits an `ObjectNotFoundException` exception.

Parameters:

Name | Type | Description | Example
--- | --- | --- | ---
transactionConfirmation | TransactionConfirmation | Transaction confirmation information to be verified. (retrievable from combining results of `[AlaxPay.PARAM_RESULT_TRX_BLOCK_NUM]` and  `[AlaxPay.PARAM_RESULT_TRX_IN_BLOCK]`) | TransactionConfirmation(blockNum=2346462, trxInBlock=0)

In the provided example application, the parameter, `transactionConfirmation` is obtained as the result of the call to `AlaxPay.Ui.parseActivityResult()` in the method, `onActivityResult()`.

See `fun onActivityResult` in the provided example application for context.

##### Data Objects #####

The `TransferInput` object carries information on the recipient of the transfer, the amount of the transfer.

```
data class TransferInput (val receiver: String,
   val amount: BigDecimal,
   val asset: Asset = Asset.ALXT)
```

Parameters:

Name | Type | Description | Default
--- | --- | --- | ---
receiver | String | Account name of the intended recipient of the transfer. |
amount | BigDecimal | The amount to be transferred, formatted as a Decimal value. |

For example:
`TransferInput(“alx-customer-6bf38de3-01af-449b-9fef-2eab82c5ca5c”, “27.50”.toBigDecimal())`

Represents a transfer of `27.5 ALXT` (the default asset type) to the user `alx-customer-6bf38de3-01af-449b-9fef-2eab82c5ca5c`.

The `TransactionConfirmation` object carries information about processed transaction after it’s carried out and serves for purposes of verifying that the transfer is indeed registered on the blockchain.

```
data class TransactionConfirmation (val blockNum: Long,
   val trxInBlock: Long)
```

Parameters:

Name | Type | Description | Default
--- | --- | --- | ---
blockNum | Long | Number of block containing the transaction |
trxInBlock | Long | Number of the transaction within its block |

For example:
`TransactionConfirmation(2346462, 0)`

Represents a transfer that is registered in block #2346462 and is the first transaction within the block.

### Versioning and compatibility ###
ALAX Pay SDK follows semantic versioning MAJOR.MINOR.PATCH. It’s important to keep attention to what version of ALAX Pay SDK is supported by latest Alax Pay application. When performing payment via ALAX Pay SDK, version check is performed. In case of version mismatch, request to payment will finish activity and you’ll get error result in `onActivityResult` method
