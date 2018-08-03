package pl.libter.rxtest.api.source

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.github.kittinunf.fuel.Fuel
import io.reactivex.ObservableEmitter
import org.json.JSONArray
import org.json.JSONObject
import pl.libter.rxtest.R
import pl.libter.rxtest.api.Repository
import pl.libter.rxtest.api.User

sealed class RepositorySource {
    companion object {
        @JvmStatic
        protected var repositoryId = 0
            get() { field++; return field }
            private set
    }

    abstract val icon: Int
    protected abstract val url: String
    protected abstract fun getRepositories(response: String, emitter: ObservableEmitter<Repository>)

    protected fun getAvatar(url: String): Bitmap {
        try {
            val (request, response, result) = Fuel.get(url).responseString()
            val data = response.data
            return BitmapFactory.decodeByteArray(data, 0, data.size)
        } catch (throwable: Throwable) {
            throw RuntimeException("Can't download avatar: $url", throwable)
        }
    }

    fun getRepositories(emitter: ObservableEmitter<Repository>) {
        val (request, response, result) = Fuel.get(url).responseString()
        getRepositories(result.component1()!!, emitter)
    }
}

class GithubRepositorySource: RepositorySource() {
    override val url = "https://api.github.com/repositories"
    override val icon = R.drawable.github

    override fun getRepositories(response: String, emitter: ObservableEmitter<Repository>) {
        val array = JSONArray(response)
        for (i in 0 until array.length()) {
            val repository = array.getJSONObject(i)
            val owner = repository.getJSONObject("owner")
            emitter.onNext(Repository(
                repositoryId, this, repository.getString("name"), repository.getString("description"),
                User(
                    owner.getString("login"),
                    getAvatar(owner.getString("avatar_url"))
                )
            ))
        }
    }
}

class BitbucketRepositorySource: RepositorySource() {
    override val url = "https://api.bitbucket.org/2.0/repositories?fields=values.name,values.owner,values.description"
    override val icon = R.drawable.bitbucket

    override fun getRepositories(response: String, emitter: ObservableEmitter<Repository>) {
        val array = JSONObject(response).getJSONArray("values")
        for (i in 0 until array.length()) {
            val repository = array.getJSONObject(i)
            val owner = repository.getJSONObject("owner")
            emitter.onNext(Repository(
                repositoryId, this, repository.getString("name"), repository.getString("description"),
                User(
                    owner.getString("username"),
                    getAvatar(
                        owner.getJSONObject("links").getJSONObject("avatar").getString("href")
                    )
                )
            ))
        }
    }
}