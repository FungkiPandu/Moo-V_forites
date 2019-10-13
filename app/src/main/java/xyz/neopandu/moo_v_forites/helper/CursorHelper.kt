package xyz.neopandu.moo_v_forites.helper

import android.database.Cursor
import xyz.neopandu.moo_v_forites.model.Movie


fun Cursor.toListOfMovies(): List<Movie> {
    val movies = mutableListOf<Movie>()
    while (this.moveToNext()) {
        movies.add(Movie.fromCursor(this))
    }
    return movies
}