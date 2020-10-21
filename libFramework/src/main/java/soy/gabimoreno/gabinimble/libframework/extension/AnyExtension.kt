package soy.gabimoreno.gabinimble.libframework.extension

import com.google.gson.Gson
import org.json.JSONArray

val Any?.exhaustive
    get() = Unit

inline fun <reified T : Any> String.toList(): List<T> =
    Gson().fromJson(this, Array<T>::class.java).asList()

fun Any.toJSONArray(): JSONArray {
    return JSONArray(Gson().toJson(this, Any::class.java))
}
