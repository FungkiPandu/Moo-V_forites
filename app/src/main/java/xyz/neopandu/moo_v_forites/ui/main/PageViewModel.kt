package xyz.neopandu.moo_v_forites.ui.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import xyz.neopandu.moo_v_forites.data.DBContract.MovieColumns.Companion.CONTENT_URI_MOVIE
import xyz.neopandu.moo_v_forites.data.DBContract.MovieColumns.Companion.CONTENT_URI_TV
import xyz.neopandu.moo_v_forites.data.DBContract.MovieColumns.Companion.createMovieURI
import xyz.neopandu.moo_v_forites.data.DBContract.MovieColumns.Companion.createTVURI
import xyz.neopandu.moo_v_forites.helper.MovieLiveData
import xyz.neopandu.moo_v_forites.helper.TVLiveData
import xyz.neopandu.moo_v_forites.helper.toListOfMovies
import xyz.neopandu.moo_v_forites.model.Movie

class PageViewModel(private val context: Context) : ViewModel() {

    private val _movies = MovieLiveData(context)
    val movies: LiveData<List<Movie>>
        get() = _movies

    private val _tvs = TVLiveData(context)
    val tvs: LiveData<List<Movie>>
        get() = _tvs

    private val _movieLoading = MutableLiveData<Boolean>()
    val movieLoading : LiveData<Boolean>
        get() = _movieLoading

    private val _tvLoading = MutableLiveData<Boolean>()
    val tvLoading : LiveData<Boolean>
        get() = _tvLoading

    init {
        loadMovies()
        loadTVs()
    }

    fun loadMovies() {
        try {
            _movieLoading.postValue(true)
            val cursor =
                context.contentResolver.query(CONTENT_URI_MOVIE, null, null, null, null)
            this._movies.postValue(cursor?.toListOfMovies())
            cursor?.close()
            _movieLoading.postValue(false)
        } catch (e: Exception) {
            e.printStackTrace()
            _movieLoading.postValue(false)
        }
    }

    fun loadTVs() {
        try {
            _tvLoading.postValue(true)
            val cursor =
                context.contentResolver.query(CONTENT_URI_TV, null, null, null, null)
            this._tvs.postValue(cursor?.toListOfMovies())
            cursor?.close()
            _tvLoading.postValue(false)
        } catch (e: Exception) {
            e.printStackTrace()
            _tvLoading.postValue(false)
        }
    }

    fun removeMovie(movie: Movie) {
        createMovieURI(movie.id)?.let {
            context.contentResolver.delete(it, null, null)
        }
    }

    fun removeTV(movie: Movie) {
        createTVURI(movie.id)?.let {
            context.contentResolver.delete(it, null, null)
        }
    }
}