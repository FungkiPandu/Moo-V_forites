package xyz.neopandu.moo_v_forites.model

import android.database.Cursor
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import xyz.neopandu.moo_v_forites.data.DBContract.MovieColumns.Companion.BANNER_PATH
import xyz.neopandu.moo_v_forites.data.DBContract.MovieColumns.Companion.DESCRIPTION
import xyz.neopandu.moo_v_forites.data.DBContract.MovieColumns.Companion.MOVIE_TYPE
import xyz.neopandu.moo_v_forites.data.DBContract.MovieColumns.Companion.ORI_LANG
import xyz.neopandu.moo_v_forites.data.DBContract.MovieColumns.Companion.ORI_TITLE
import xyz.neopandu.moo_v_forites.data.DBContract.MovieColumns.Companion.POPULARITY
import xyz.neopandu.moo_v_forites.data.DBContract.MovieColumns.Companion.POSTER_PATH
import xyz.neopandu.moo_v_forites.data.DBContract.MovieColumns.Companion.RELEASE_DATE
import xyz.neopandu.moo_v_forites.data.DBContract.MovieColumns.Companion.SCORE
import xyz.neopandu.moo_v_forites.data.DBContract.MovieColumns.Companion.TITLE
import xyz.neopandu.moo_v_forites.data.DBContract.MovieColumns.Companion._ID

@Parcelize
data class Movie(
    val id: Int,
    val title: String,
    val oriTitle: String,
    val oriLang: String,
    val description: String,
    val posterPath: String,
    val bannerPath: String,
    val score: Double,
    val popularity: Double,
    val releaseDate: String,
    val movieType: MovieType,
    var isFavorite: Boolean
) : Parcelable {
    enum class MovieType(val value: String) {
        MOVIE("MOVIE"), TV_SHOW("TV SHOW")
    }

    companion object {
        fun fromCursor(cursor: Cursor): Movie {

            val typeName = cursor.getString(cursor.getColumnIndex(MOVIE_TYPE))
            val movieType =
                MovieType.values().firstOrNull { it.name == typeName } ?: MovieType.MOVIE

            return Movie(
                cursor.getInt(cursor.getColumnIndex(_ID)),
                cursor.getString(cursor.getColumnIndex(TITLE)),
                cursor.getString(cursor.getColumnIndex(ORI_TITLE)),
                cursor.getString(cursor.getColumnIndex(ORI_LANG)),
                cursor.getString(cursor.getColumnIndex(DESCRIPTION)),
                cursor.getString(cursor.getColumnIndex(POSTER_PATH)),
                cursor.getString(cursor.getColumnIndex(BANNER_PATH)),
                cursor.getDouble(cursor.getColumnIndex(SCORE)),
                cursor.getDouble(cursor.getColumnIndex(POPULARITY)),
                cursor.getString(cursor.getColumnIndex(RELEASE_DATE)),
                movieType,
                true
            )
        }
    }
}