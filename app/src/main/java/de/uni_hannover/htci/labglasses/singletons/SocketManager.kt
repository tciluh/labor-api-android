package de.uni_hannover.htci.labglasses.singletons

import android.content.Context
import android.preference.PreferenceManager
import de.uni_hannover.htci.labglasses.activity.BaseActivity
import de.uni_hannover.htci.labglasses.model.Action
import io.reactivex.schedulers.Schedulers
import io.socket.client.Ack
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by sl33k on 17.04.18.
 * This is class is used for managing the socket.io connection to the labor-api server
 */
typealias ResultCallback = (Any) -> Unit
typealias ErrorCallback = (String) -> Unit

class SocketManager private constructor(context: Context): AnkoLogger {
    var connected: Boolean = false
    val socket: Socket
        get() {
            if(!connected) {
                socketHolder.connect()
                connected = true
            }
            return socketHolder
    }
    private val socketHolder: Socket

    // any callbacks that haven't been processed by the server yet.
    val awaitingResult: MutableMap<Int, Pair<ResultCallback, ErrorCallback>> = mutableMapOf()

    init {
        //get api url + port
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val host = sharedPrefs.getString(BaseActivity.API_HOST_PREFERENCE, "130.75.115.47")
        val port = sharedPrefs.getString(BaseActivity.API_PORT_PREFERENCE, "3000")

        val opts: IO.Options = IO.Options()
        opts.path = "/socket.io"
        opts.forceNew = true
        opts.secure = false
        opts.reconnection = true
        opts.reconnectionAttempts = 10
        opts.transports = arrayOf(WebSocket.NAME)

        socketHolder = IO.socket("http://$host:3000/", opts)
        socketHolder.on(Socket.EVENT_CONNECTING, { args ->
            debug("labor-api socket: connecting...")
            connected = false
        })

        socketHolder.on(Socket.EVENT_CONNECT_ERROR, { args ->
            debug("labor-api socket: connection error")
            debug("error: $args")
            connected = false
        })

        socketHolder.on(Socket.EVENT_CONNECT_TIMEOUT, { args ->
            debug("labor-api socket: connection timeout")
            debug("error: $args")
            connected = false
        })

        socketHolder.on(Socket.EVENT_CONNECT, { args ->
            debug("labor-api socket: connected")
            connected = true
        })

        socketHolder.on(Socket.EVENT_DISCONNECT, { args ->
            debug("labor-api socket: disconnected")
            connected = false
        })

        socketHolder.on(EVENT_ERROR, { args: Array<out Any> ->
            debug("labor-api socket: action error")
            val obj = args[0] as JSONObject
            try {
                val msg = obj.getString("error")
                val id = obj.getInt("resultId")

                debug("error msg: $msg for id: $id")

                val cbs = awaitingResult.remove(id) ?: error("no callback with id($id) found")
                cbs.second(msg ?: "unknown")
            }
            catch(e: JSONException) {
                debug("malformed action error: $obj")
                return@on
            }
            catch(e: IllegalStateException){
                debug("labor-api socket: $e")
            }
        })

        socketHolder.on(EVENT_RESULT, { args: Array<out Any> ->
            debug("socket.io got result")
            val obj = args[0] as JSONObject
            val callbacks: Pair<ResultCallback, ErrorCallback>
            try {
                val id = obj.getInt("id")
                callbacks = awaitingResult[id] ?: error("no callback with id ($id) found")
            }
            catch(e: JSONException) {
                debug("labor-api socket: malformed result id")
                return@on
            }
            catch(e: IllegalStateException) {
                debug("labor-api socket: no callback for the given id found.")
                return@on
            }
            val result = obj.optString("result", "api error")
            callbacks.first(result)
        })

        socketHolder.open()
    }


    fun sendAction(action: Action, resultCb: ResultCallback, errorCb: ErrorCallback) {
        socket.emit(EVENT_ACTION, action.id, Ack {
            args: Array<out Any> ->
            val id: Int = args[0] as Int
            debug("labor-api socket: server action ack'ed with resultid: $id")
            awaitingResult.put(id, Pair(resultCb, errorCb))
        })
    }


    companion object: SingletonHolder<SocketManager, Context>(::SocketManager) {
        const val EVENT_ACTION = "action"
        const val EVENT_RESULT = "result"
        const val EVENT_ERROR = "action error"
    }
}
