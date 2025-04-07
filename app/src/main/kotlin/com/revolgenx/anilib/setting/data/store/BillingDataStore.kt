package com.revolgenx.anilib.setting.data.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.android.billingclient.api.Purchase
import kotlinx.coroutines.flow.map

class BillingDataStore(val dataStore: DataStore<Preferences>) {
    companion object{
        val isAppDevSupportedKey = booleanPreferencesKey("is_app_dev_supported_key")
        val purchaseTokenKey = stringPreferencesKey("purchase_token_key")
        val purchaseTimeKey = stringPreferencesKey("purchase_time_key")
        val purchaseOrderIdKey = stringPreferencesKey("purchase_order_id_key")
    }

    val isAppDevSupported get() = dataStore.data.map { it[isAppDevSupportedKey] ?: false }

    suspend fun savePurchase(purchase: Purchase){
        dataStore.edit {
            it[isAppDevSupportedKey] = true
            it[purchaseTokenKey] = purchase.purchaseToken
            it[purchaseTimeKey] = purchase.purchaseTime.toString()
            it[purchaseOrderIdKey] = purchase.orderId ?: ""
        }
    }
}