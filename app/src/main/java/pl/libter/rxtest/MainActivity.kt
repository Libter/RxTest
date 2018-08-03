package pl.libter.rxtest

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import pl.libter.rxtest.api.RepositoriesDownloader
import pl.libter.rxtest.api.Repository
import java.util.*

class MainActivity: AppCompatActivity() {

    private var sort = false
    private val repositories = ArrayList<Repository>()
    private lateinit var repositoriesAdapter: ArrayAdapter<Repository>

    private fun applySort() {
        repositories.sortWith(Comparator { r1, r2 ->
            if (sort) r1.name.toLowerCase().compareTo(r2.name.toLowerCase()) else r1.id.compareTo(r2.id)
        })
        repositoriesAdapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        repositoriesAdapter = RepositoriesAdapter(this, repositories)

        RxJavaPlugins.setErrorHandler {
            it.printStackTrace()
            Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show()
        }

        Observable.create(RepositoriesDownloader())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                repositories.add(it)
                applySort()
            }

        findViewById<ListView>(R.id.repositories).run {
            adapter = repositoriesAdapter
            setOnItemClickListener { adapter, view, position, id ->
                DescriptionActivity.repository = adapter.getItemAtPosition(position) as Repository
                startActivity(Intent(this@MainActivity, DescriptionActivity::class.java))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.actionbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuSort -> {
                sort = !sort
                item.icon = resources.getDrawable(if (sort) R.drawable.sort_id else R.drawable.sort_name)
                applySort()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
