package com.android.intdv.core.platform

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.intdv.R
import com.android.intdv.R.layout
import com.android.intdv.core.extension.gone
import com.android.intdv.core.extension.inTransaction
import com.android.intdv.core.extension.visible
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Represents the base activity which will provide common features to all child activities
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layoutId())

        addInitialFragment()

        GlideApp.with(this).asGif()
            .load(R.drawable.loader)
            .into(ivProgress)

        init()
    }

    /**
     * Returns the instance of fragment to be loaded in activity
     */
    abstract fun fragment(): BaseFragment

    /**
     * Returns the layout resource id
     */
    open fun layoutId(): Int = layout.activity_main

    /**
     * Initializes the view and resources
     */
    abstract fun init()

    /**
     * Displays the progress dialog
     */
    fun showProgress() {
        ivProgress?.visible()
    }

    /**
     * Hides the progress dialog
     */
    fun hideProgress() {
        ivProgress?.gone()
    }

    private fun addInitialFragment() =
        supportFragmentManager.inTransaction {
            add(R.id.fragmentContainer, fragment())
        }

    fun addFragmentInFlow(fragment: BaseFragment) {
        supportFragmentManager.inTransaction {
            add(R.id.fragmentContainer, fragment)
            addToBackStack(null)
        }
    }
}