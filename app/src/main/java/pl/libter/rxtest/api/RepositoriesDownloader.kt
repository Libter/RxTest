package pl.libter.rxtest.api

import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import pl.libter.rxtest.api.source.BitbucketRepositorySource
import pl.libter.rxtest.api.source.GithubRepositorySource

class RepositoriesDownloader: ObservableOnSubscribe<Repository> {

    override fun subscribe(emitter: ObservableEmitter<Repository>) {
        try {
            process(emitter)
            emitter.onComplete()
        } catch (throwable: Throwable) {
            emitter.onError(throwable)
        }
    }

    private fun process(emitter: ObservableEmitter<Repository>) {
        arrayOf(BitbucketRepositorySource(), GithubRepositorySource()).forEach {
            it.getRepositories(emitter)
        }
    }

}