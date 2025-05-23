package com.revolgenx.anilib.setting.ui.viewmodel

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.ConnectionState
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.revolgenx.anilib.common.ext.get
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.setting.data.store.BillingDataStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlin.math.min

class BillingViewModel(
    private val billingDataStore: BillingDataStore
) : ViewModel() {
    private lateinit var billingClient: BillingClient
    private lateinit var startConnectionJob: Job

    private var retryBillingServiceCount = 0
    private val maxRetries = 3
    private val baseDelay = 1000L
    private val maxDelay = 1000L * 60L * 15L

    val billingConnectionState = mutableIntStateOf(ConnectionState.DISCONNECTED)
    private var launchingBillingFlow = false

    val isAppDevSupported = billingDataStore.isAppDevSupported

    val hasPendingPurchase = mutableStateOf(false)

    val failedToPurchase = mutableStateOf(false)

    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->

            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                handlePurchases(purchases)
            } else {
                failedToPurchase.value = true
            }
        }

    fun setupBillingClient(context: Context) {
        if(billingConnectionState.intValue == ConnectionState.CONNECTED) return

        billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder().enableOneTimeProducts().build()
            )
            .build()

        if (!billingDataStore.isAppDevSupported.get()) {
            startBillingConnection()
        }
    }


    private fun startBillingConnection() {
        billingConnectionState.intValue = ConnectionState.CONNECTING
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    retryBillingServiceCount = 0
                    queryPurchases()
                    billingConnectionState.intValue = ConnectionState.CONNECTED
                }else{
                    billingConnectionState.intValue = ConnectionState.DISCONNECTED
                }
            }

            override fun onBillingServiceDisconnected() {
                billingConnectionState.intValue = ConnectionState.DISCONNECTED
                handleConnectionError()
            }
        })
    }

    private fun handleConnectionError() {
        startConnectionJob = launch {
            retryBillingServiceCount++
            if (retryBillingServiceCount <= maxRetries) {
                val delayMillis = calculateExponentialDelay(retryBillingServiceCount)
                delay(delayMillis)
                startBillingConnection()
            }
        }
    }

    fun queryPurchases() {
        hasPendingPurchase.value = false
        failedToPurchase.value = false
        val params = QueryPurchasesParams
            .newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        billingClient.queryPurchasesAsync(params) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                handlePurchases(purchases)
            }
        }
    }

    private fun handlePurchases(purchases: List<Purchase>) {
        for (purchase in purchases) {
            when (purchase.purchaseState) {
                Purchase.PurchaseState.PENDING -> {
                    hasPendingPurchase.value = true
                }

                Purchase.PurchaseState.PURCHASED -> {
                    hasPendingPurchase.value = false
                    if (!purchase.isAcknowledged) {
                        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                            .setPurchaseToken(purchase.purchaseToken)
                            .build()

                        billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                                storePurchaseInfo(purchase)
                            }
                        }
                    } else {
                        storePurchaseInfo(purchase)
                    }
                }
            }
        }
    }

    fun startPurchase(activity: Activity, purchaseQuantity: Int) {
        failedToPurchase.value = false
        launchBillingFlow(activity, "remove_ads_${purchaseQuantity}")
    }

    fun restartBillingConnection(){
        if (::startConnectionJob.isInitialized) {
            startConnectionJob.cancel()
            retryBillingServiceCount = 0
        }
        startBillingConnection()
    }

    private fun launchBillingFlow(activity: Activity, productId: String) {
        if(launchingBillingFlow) return

        launchingBillingFlow = true
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(productId)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            launchingBillingFlow = false
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                productDetailsList.firstOrNull()?.let { productDetails ->
                    val billingFlowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(
                            listOf(
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                    .setProductDetails(productDetails)
                                    .build()
                            )
                        )
                        .build()

                    billingClient.launchBillingFlow(activity, billingFlowParams)
                }
            }
        }
    }

    private fun storePurchaseInfo(purchase: Purchase) {
        launch {
            billingDataStore.savePurchase(purchase)
        }
    }


    private fun calculateExponentialDelay(retryCount: Int): Long {
        return min(maxDelay, baseDelay * (1L shl (retryCount - 1)))
    }

    override fun onCleared() {
        billingClient.endConnection()
        super.onCleared()
    }


}