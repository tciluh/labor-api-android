package de.uni_hannover.htci.labglasses.api

import com.serjltt.moshi.adapters.Wrapped
import de.uni_hannover.htci.labglasses.model.Protocol
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by sl33k on 11/12/17.
 */
interface ILaborApi {
    @GET("protocol/")
    @Wrapped(path = arrayOf("payload"))
    fun getProtocols(): Single<List<Protocol>>

    @GET("protocol/{id}")
    @Wrapped(path = arrayOf("payload"))
    fun getProtocol(@Path("id") id: Int): Single<Protocol>
}