package xyz.neopandu.moo_v_forites.helper

import android.content.Context
import xyz.neopandu.moo_v_forites.data.DBContract
import xyz.neopandu.moo_v_forites.model.Movie

class MovieLiveData(
    private val context: Context
) : ContentProviderLiveData<List<Movie>>(context, uri) {

    override fun getContentProviderValue(): List<Movie> {
        val cursor = context.contentResolver.query(uri, null, null, null, null, null)
        val result = cursor?.toListOfMovies() ?: emptyList()
        cursor?.close()
        return result
    }

    companion object {
        /**
         * Uri used for this [ContentProviderLiveData].
         */
        private val uri = DBContract.MovieColumns.CONTENT_URI_MOVIE
    }
}