package de.uni_hannover.htci.labglasses.api

import com.serjltt.moshi.adapters.Wrapped
import de.uni_hannover.htci.labglasses.model.IOResult
import de.uni_hannover.htci.labglasses.model.Instruction
import de.uni_hannover.htci.labglasses.model.Protocol
import de.uni_hannover.htci.labglasses.model.Result
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Created by sl33k on 11/12/17.
 * This is the interface which is then constructed by retrofit.
 * It contains most of the methods described in the laborAPI Specification v1.0.0 without the
 * image update/delete/create methods as these are not neccessary for this app.
 */
interface ILaborApi {

    companion object {
        const val protocol_path = "protocol"
        const val instruction_path = "instruction"
        const val result_path = "result"
        const val image_path = "image"
        const val ioresult_path = "ioresult"
    }

    // protocols

    @GET(protocol_path)
    @Wrapped(path = arrayOf("payload"))
    fun getProtocols(): Single<List<Protocol>>

    @GET("$protocol_path/{id}")
    @Wrapped(path = arrayOf("payload"))
    fun getProtocol(@Path("id") id: Int): Single<Protocol>

    @PUT("$protocol_path/{id}")
    @Wrapped(path = arrayOf("payload"))
    fun updateProtocol(@Body protocol: Protocol, @Path("id") id: Int): Single<Protocol>

    @DELETE("$protocol_path/{id}")
    @Wrapped(path = arrayOf("success")) // only return whether or not the operation was scucessful
    fun deleteProtocol(@Path("id") id: Int): Single<Boolean>

    //protocol-steps

    @GET("$instruction_path/{id}")
    @Wrapped(path = arrayOf("payload"))
    fun getInstruction(@Path("id") id: Int): Single<Instruction>

    @PUT("$instruction_path/{id}")
    @Wrapped(path = arrayOf("payload"))
    fun updateInstruction(@Body instruction: Instruction): Single<Instruction>

    @GET("$result_path/{id}")
    @Wrapped(path = arrayOf("payload"))
    fun getResult(@Path("id") id: Int): Single<Result>

    @PUT("$result_path/{id}")
    @Wrapped(path = arrayOf("payload"))
    fun updateResult(@Body result: Result): Single<Result>

    //images

    @GET("$image_path/{id}")
    fun getImage(@Path("id") id: Int): Single<ResponseBody>

    //actions

    @GET("$ioresult_path/{id}")
    @Wrapped(path = arrayOf("payload"))
    fun getIOResult(@Path("id") id: Int): Single<IOResult>

}