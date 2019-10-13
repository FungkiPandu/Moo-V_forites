package xyz.neopandu.moo_v_forites.adapter


import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.row_item_movie.view.*
import kotlinx.android.synthetic.main.view_empty_list.view.*
import xyz.neopandu.moo_v_forites.R
import xyz.neopandu.moo_v_forites.model.ListItem
import xyz.neopandu.moo_v_forites.model.Movie
import kotlin.random.Random


class MyMovieRecyclerViewAdapter(
    private val context: Context,
    private val screenWidth: Int,
    private val clickListener: (Movie) -> Unit,
    private val deleteListener: (Movie) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mValues: MutableList<ListItem> = mutableListOf()

    var emptyDrawable: Drawable? = null
    var emptyMessage: String? = null

    companion object {
        const val EMPTY_LIST_VIEWTYPE = 0
        const val POSTER_VIEWTYPE = 1
        const val BANNER_VIEWTYPE = 2
        const val WIDE_BANNER_VIEWTYPE = 3
    }

    fun updateValues(items: List<Movie>) {
        mValues.clear()
        mValues.addAll(transformToItemList(items))
        notifyDataSetChanged()
    }

    private fun transformToItemList(items: List<Movie>): List<ListItem> {
        val newItems = mutableListOf<ListItem>()
        var pos = 0
        for ((count, movie) in items.withIndex()) {
            pos %= 3

            if (count == items.size - 1) {
                when (pos) {
                    2 -> newItems.add(ListItem.Poster(movie))
                    1 -> newItems.add(ListItem.Banner(movie))
                    else -> newItems.add(ListItem.WideBanner(movie))
                }
                return newItems
            }

            when (pos) {
                2 -> {
                    val item = ListItem.Poster(movie)
                    newItems.add(item)
                    pos++
                }
                1 -> {
                    val type = Random.nextInt(POSTER_VIEWTYPE, BANNER_VIEWTYPE + 1)
                    pos += type
                    val item: ListItem = when (type) {
                        2 -> ListItem.Banner(movie)
                        else -> ListItem.Poster(movie)
                    }
                    newItems.add(item)
                }
                else -> {
                    val type = Random.nextInt(POSTER_VIEWTYPE, WIDE_BANNER_VIEWTYPE + 1)
                    pos += type
                    val item = when (type) {
                        3 -> ListItem.WideBanner(movie)
                        2 -> ListItem.Banner(movie)
                        else -> ListItem.Poster(movie)
                    }
                    newItems.add(item)
                }
            }
        }

        return newItems
    }

    override fun getItemViewType(position: Int): Int {
        return if (mValues.isEmpty()) EMPTY_LIST_VIEWTYPE else {
            mValues[position].run {
                return when (this) {
                    is ListItem.Poster -> POSTER_VIEWTYPE
                    is ListItem.Banner -> BANNER_VIEWTYPE
                    is ListItem.WideBanner -> WIDE_BANNER_VIEWTYPE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            EMPTY_LIST_VIEWTYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_empty_list, parent, false)
                EmptyViewHolder(view)
            }
            POSTER_VIEWTYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_item_movie, parent, false)
                ViewHolder(view)
            }
            BANNER_VIEWTYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_item_movie, parent, false)
                LandscapeViewHolder(view)
            }
            WIDE_BANNER_VIEWTYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_item_movie, parent, false)
                WideLandscapeViewHolder(view)
            }
            else -> throw RuntimeException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is EmptyViewHolder) {
            holder.bind()
            return
        }
        val item = mValues[position]
        if (holder is ViewHolder && item is ListItem.Poster) holder.bind(item.movie)
        else if (holder is LandscapeViewHolder && item is ListItem.Banner) holder.bind(item.movie)
        else if (holder is WideLandscapeViewHolder && item is ListItem.WideBanner) holder.bind(item.movie)
    }

    inner class MovieSpanSizeLookup(private val spanCount: Int) :
        GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return if (mValues.isEmpty()) spanCount else {
                when (mValues[position]) {
                    is ListItem.Poster -> 1
                    is ListItem.Banner -> 2
                    is ListItem.WideBanner -> 3
                }
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = MovieSpanSizeLookup(layoutManager.spanCount)
        }
    }

    override fun getItemCount(): Int = if (mValues.isEmpty()) 1 else mValues.size

    inner class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView = view.error_image
        private val textView = view.error_view_text

        fun bind() {
            emptyDrawable?.let {
                imageView.setImageDrawable(it)
            }
            emptyMessage?.let {
                textView.text = it
            }
        }
    }

    inner class ViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {
        private val mBanner: ImageView = mView.img_banner
        private val mFavoriteButton: ImageButton = mView.btn_favorite

        fun bind(item: Movie) {
            val w = screenWidth / 3
            mView.layoutParams.height = w * 3 / 2


            val posterUrl = "https://image.tmdb.org/t/p/w780" + item.posterPath
            Glide.with(context).load(posterUrl).into(mBanner)

            mView.setOnClickListener {
                clickListener.invoke(item)
            }

            mFavoriteButton.setOnClickListener {
                deleteListener.invoke(item)
            }
        }
    }

    inner class LandscapeViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {
        private val mBanner: ImageView = mView.img_banner
        private val mFavoriteButton: ImageButton = mView.btn_favorite

        fun bind(item: Movie) {
            val w = screenWidth / 3
            mView.layoutParams.height = w * 3 / 2

            mBanner.scaleType = ImageView.ScaleType.CENTER_CROP

            val bannerUrl = "https://image.tmdb.org/t/p/w780" + item.bannerPath
            Glide.with(context).load(bannerUrl).into(mBanner)

            mView.setOnClickListener {
                clickListener.invoke(item)
            }

            mFavoriteButton.setOnClickListener {
                deleteListener.invoke(item)
            }
        }
    }

    inner class WideLandscapeViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {
        private val mBanner: ImageView = mView.img_banner
        private val mFavoriteButton: ImageButton = mView.btn_favorite

        fun bind(item: Movie) {
            mView.layoutParams.height = screenWidth / 2

            val bannerUrl = "https://image.tmdb.org/t/p/w780" + item.bannerPath
            Glide.with(context).load(bannerUrl).into(mBanner)

            mView.setOnClickListener {
                clickListener.invoke(item)
            }

            mFavoriteButton.setOnClickListener {
                deleteListener.invoke(item)
            }
        }
    }

}
