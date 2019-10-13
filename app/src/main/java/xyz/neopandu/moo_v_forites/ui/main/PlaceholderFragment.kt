package xyz.neopandu.moo_v_forites.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import xyz.neopandu.moo_v_forites.DetailActivity
import xyz.neopandu.moo_v_forites.adapter.MyMovieRecyclerViewAdapter


/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyMovieRecyclerViewAdapter
    private var page: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel =
            ViewModelProviders.of(
                requireActivity(),
                PageViewModelFactory(requireActivity().applicationContext)
            ).get(PageViewModel::class.java)

        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels

        page = arguments?.getInt(ARG_SECTION_NUMBER, 0) ?: 0

        adapter = MyMovieRecyclerViewAdapter(requireContext(), width,

            clickListener = {
                val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_MOVIE, it)
                }
                startActivity(intent)
            },

            deleteListener = {
                if (page == 1) pageViewModel.removeMovie(it)
                else pageViewModel.removeTV(it)
            })

        if (page == 1) {
            pageViewModel.movies.observe(this, Observer {
                it?.let { adapter.updateValues(it) }
            })
        } else {
            pageViewModel.tvs.observe(this, Observer {
                it?.let { adapter.updateValues(it) }
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        swipeRefreshLayout = SwipeRefreshLayout(requireContext())
        recyclerView = RecyclerView(requireContext())
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.adapter = adapter

        swipeRefreshLayout.setOnRefreshListener {
            print("refreshing...")
            if (page == 1) pageViewModel.loadMovies()
            else pageViewModel.loadTVs()
        }

        val observer = Observer<Boolean> { isLoading ->
            swipeRefreshLayout.isRefreshing = isLoading
        }
        if (page == 1) pageViewModel.movieLoading.observe(requireActivity(), observer)
        else pageViewModel.tvLoading.observe(requireActivity(), observer)

        swipeRefreshLayout.addView(recyclerView)
        return swipeRefreshLayout
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}