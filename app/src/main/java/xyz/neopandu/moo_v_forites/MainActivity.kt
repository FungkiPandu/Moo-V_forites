package xyz.neopandu.moo_v_forites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import xyz.neopandu.moo_v_forites.ui.main.PageViewModel
import xyz.neopandu.moo_v_forites.ui.main.PageViewModelFactory
import xyz.neopandu.moo_v_forites.ui.main.SectionsPagerAdapter

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this, PageViewModelFactory(this.applicationContext))
            .get(PageViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadMovies()
        viewModel.loadTVs()
    }
}