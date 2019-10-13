package xyz.neopandu.moo_v_forites.helper

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import androidx.lifecycle.MutableLiveData

/**
 * Abstract [MutableLiveData] to observe Android's Content Provider changes.
 * Provide a [uri] to observe changes and implement [getContentProviderValue]
 * to provide data to post when content provider notifies a change.
 */
abstract class ContentProviderLiveData<T>(
    private val context: Context,
    private val uri: Uri
) : MutableLiveData<T>() {
    private lateinit var observer: ContentObserver

    override fun onActive() {
        observer = object : ContentObserver(null) {
            override fun onChange(self: Boolean) {
                // Notify LiveData listeners an event has happened
                postValue(getContentProviderValue())
            }
        }
        try {
            context.contentResolver.registerContentObserver(uri, true, observer)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onInactive() {
        context.contentResolver.unregisterContentObserver(observer)
    }

    /**
     * Implement if you need to provide [T] value to be posted
     * when observed content is changed.
     */
    abstract fun getContentProviderValue(): T
}