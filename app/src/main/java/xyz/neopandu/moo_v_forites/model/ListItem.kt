package xyz.neopandu.moo_v_forites.model

sealed class ListItem {
    data class Poster(
        val movie: Movie
    ) : ListItem()
    data class Banner(
        val movie: Movie
    ) : ListItem()
    data class WideBanner(
        val movie: Movie
    ) : ListItem()
}