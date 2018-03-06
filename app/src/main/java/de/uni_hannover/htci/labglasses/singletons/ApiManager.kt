package de.uni_hannover.htci.labglasses.singletons

import android.content.Context
import android.preference.PreferenceManager
import com.serjltt.moshi.adapters.Wrapped
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import de.uni_hannover.htci.labglasses.activity.BaseActivity
import de.uni_hannover.htci.labglasses.api.ILaborApi
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by sl33k on 06.03.18.
 */
class ApiManager private constructor(context: Context) {
    val api: ILaborApi

    init {
        val moshi: Moshi = Moshi.Builder()
                                .add(Wrapped.ADAPTER_FACTORY)
                                .add(KotlinJsonAdapterFactory())
                                .build()

        //we want the subscription to run on a background thread
        val rx2adapter = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
        //get api url + port
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val host = sharedPrefs.getString(BaseActivity.API_HOST_PREFERENCE, "130.75.115.47")
        val port = sharedPrefs.getString(BaseActivity.API_PORT_PREFERENCE, "3000")

        val retrofit: Retrofit = Retrofit.Builder()
                        .baseUrl("http://$host:$port/")
                        .addConverterFactory(MoshiConverterFactory.create(moshi))
                        .addCallAdapterFactory(rx2adapter)
                        .build()
        api = retrofit.create(ILaborApi::class.java)
    }
    companion object: SingletonHolder<ApiManager, Context>(::ApiManager) //pass private constructor
}