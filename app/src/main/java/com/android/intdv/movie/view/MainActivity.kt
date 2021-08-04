package com.android.intdv.movie.view

import com.android.intdv.core.platform.BaseActivity

class MainActivity : BaseActivity() {

    override fun fragment() = MovieListFragment()

    override fun init() { }
}
