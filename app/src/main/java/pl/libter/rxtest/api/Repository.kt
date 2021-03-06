package pl.libter.rxtest.api

import pl.libter.rxtest.api.source.RepositorySource

data class Repository(val id: Int, val source: RepositorySource, val name: String, val description: String, val owner: User)