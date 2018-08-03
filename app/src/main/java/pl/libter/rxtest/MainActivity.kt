package pl.libter.rxtest

import android.app.Activity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import pl.libter.rxtest.api.RepositoriesDownloader
import pl.libter.rxtest.api.Repository
import java.util.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val repositories = ArrayList<Repository>()
        val repositoriesAdapter = RepositoriesAdapter(this, repositories)

        RxJavaPlugins.setErrorHandler {
            it.printStackTrace()
            Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show()
        }

        Observable.create(RepositoriesDownloader())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                repositories.add(it)
                repositoriesAdapter.notifyDataSetChanged()
            }

        findViewById<ListView>(R.id.repositories).adapter = repositoriesAdapter
    }
}
