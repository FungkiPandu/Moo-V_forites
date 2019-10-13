package xyz.neopandu.moo_v_forites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import xyz.neopandu.moo_v_forites.model.Movie

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MOVIE = "movie_extra"
    }

    private val movie by lazy { intent.getParcelableExtra<Movie>(EXTRA_MOVIE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        movie?.let {
            supportActionBar?.title = it.title
            description.text = it.description
            val path = "https://image.tmdb.org/t/p/w780" + it.posterPath
            Glide.with(this).load(path).into(img_poster_detail)
        }
    }
}
