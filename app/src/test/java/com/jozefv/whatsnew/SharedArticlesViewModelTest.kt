@file:OptIn(ExperimentalCoroutinesApi::class)

package com.jozefv.whatsnew

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.jozefv.whatsnew.core.domain.ArticleDaoFake
import com.jozefv.whatsnew.core.domain.SessionStorage
import com.jozefv.whatsnew.core.domain.TestCoroutineDispatchers
import com.jozefv.whatsnew.feat_articles.data.remote.articles.ApiDefaults.BASE_URL
import com.jozefv.whatsnew.feat_articles.data.remote.articles.ApiDefaults.DEFAULT_URL
import com.jozefv.whatsnew.feat_articles.data.remote.articles.ApiDefaults.OPTIONAL_NEXT_PAGE_PARAM
import com.jozefv.whatsnew.feat_articles.data.remote.articles.ArticlesRepositoryKtorImpl
import com.jozefv.whatsnew.feat_articles.data.remote.articles.HttpClientFactory
import com.jozefv.whatsnew.feat_articles.data.remote.articles.dto.ArticleDataDto
import com.jozefv.whatsnew.feat_articles.data.remote.articles.dto.toArticle
import com.jozefv.whatsnew.feat_articles.domain.filter.FiltersStorage
import com.jozefv.whatsnew.feat_articles.domain.search.SuggestionsStorage
import com.jozefv.whatsnew.feat_articles.presentation.ArticlesAction
import com.jozefv.whatsnew.feat_articles.presentation.SharedArticlesEvent
import com.jozefv.whatsnew.feat_articles.presentation.SharedArticlesViewModel
import com.jozefv.whatsnew.feat_articles.presentation.articles.model.toArticleLocal
import com.jozefv.whatsnew.feat_articles.presentation.articles.model.toArticleUi
import com.jozefv.whatsnew.feat_profile.data.RoomLocalArticlesRepository
import com.jozefv.whatsnew.stubs.ArticlesDataDtoStub
import com.jozefv.whatsnew.stubs.FilterStorageStub
import com.jozefv.whatsnew.stubs.SessionStorageStub
import com.jozefv.whatsnew.stubs.SuggestionStorageStub
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.headers
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
// Integration test
class SharedArticlesViewModelTest {
    private lateinit var sharedArticlesViewModel: SharedArticlesViewModel
    private lateinit var articleRepository: ArticlesRepositoryKtorImpl
    private lateinit var localArticlesRepository: RoomLocalArticlesRepository
    private lateinit var articleDaoFake: ArticleDaoFake
    private lateinit var sessionStorage: SessionStorage
    private lateinit var filterStorage: FiltersStorage
    private lateinit var suggestionsStorage: SuggestionsStorage
    private lateinit var testCoroutineDispatchers: TestCoroutineDispatchers
    private lateinit var testKtorEngine: TestKtorEngine
    private lateinit var articlesDataDtoStub: ArticlesDataDtoStub
    private lateinit var articlesDataDtoStubDefault: ArticleDataDto
    private lateinit var articlesDataDtoStubMore: ArticleDataDto

    @BeforeEach
    fun setUp() {
        // We are not interested in testing those - so just create stubs for that
        sessionStorage = SessionStorageStub
        filterStorage = FilterStorageStub
        suggestionsStorage = SuggestionStorageStub
        articlesDataDtoStub = ArticlesDataDtoStub
        articlesDataDtoStubDefault = articlesDataDtoStub.articleDataDaoStub(
            sizeOfResults = articlesDataDtoStub.SIZE_OF_RESULTS,
            nextPage = articlesDataDtoStub.NEXT_PAGE
        )
        // After getMore articles - we expect
        // twice as much results as what we had when articlesDataDtoStubDefault is called
        articlesDataDtoStubMore = articlesDataDtoStub.articleDataDaoStub(
            sizeOfResults = articlesDataDtoStub.SIZE_OF_RESULTS * 2,
            nextPage = "3"
        )
        testCoroutineDispatchers = TestCoroutineDispatchers(StandardTestDispatcher())
        val mockEngineConfig = MockEngineConfig().apply {
            requestHandlers.add { incomingRequest ->
                when (incomingRequest.url) {
                    Url(BASE_URL + DEFAULT_URL) -> {
                        // what mockClient response with when selected url is executed
                        respond(
                            content = ByteReadChannel(
                                text = Json.encodeToString(
                                    articlesDataDtoStubDefault
                                )
                            ),
                            status = HttpStatusCode.OK,
                            headers = headers {
                                // set - if there is already a content-type header - replace it
                                set("Content-Type", "application/json")
                            }
                        )
                    }

                    Url("$BASE_URL$DEFAULT_URL$OPTIONAL_NEXT_PAGE_PARAM${articlesDataDtoStub.NEXT_PAGE}") -> {
                        // what mockClient response with when selected url is executed
                        respond(
                            content = ByteReadChannel(
                                text = Json.encodeToString(
                                    articlesDataDtoStubDefault
                                )
                            ),
                            status = HttpStatusCode.OK,
                            headers = headers {
                                // set - if there is already a content-type header - replace it
                                set("Content-Type", "application/json")
                            }
                        )
                    }

                    else -> {
                        println("Incoming url is: ${incomingRequest.url}")
                        respond(
                            // we don't have a data to respond with
                            content = byteArrayOf(),
                            // Respond with error - this will be caught with ArticlesRepositoryKtorImpl
                            status = HttpStatusCode.InternalServerError
                        )
                    }
                }
            }
        }
        testKtorEngine = TestKtorEngine(
            mockEngineConfig = mockEngineConfig,
            dispatcher = testCoroutineDispatchers.testDispatcher
        )
        val httpClient = HttpClientFactory.build(testKtorEngine)
        articleRepository = ArticlesRepositoryKtorImpl(
            httpClient = httpClient,
            suggestionsStorage = suggestionsStorage,
            filtersStorage = filterStorage
        )
        articleDaoFake = ArticleDaoFake
        localArticlesRepository = RoomLocalArticlesRepository(articleDaoFake)
        sharedArticlesViewModel = SharedArticlesViewModel(
            articlesRepository = articleRepository,
            localArticlesRepository = localArticlesRepository,
            sessionStorage = sessionStorage,
            filtersStorage = filterStorage,
            suggestionsStorage = suggestionsStorage,
            coroutineDispatchers = testCoroutineDispatchers

        )
    }

    @Test
    fun getInitialArticles() = runTest(testCoroutineDispatchers.testDispatcher) {
        // We will immediately jump into coroutine - so isLoadingArticles will be switch to true
        assertThat(sharedArticlesViewModel.state.isLoadingArticles).isTrue()
        advanceTimeBy(1005)
        assertThat(sharedArticlesViewModel.state.isLoggedIn).isTrue()
        assertThat(sharedArticlesViewModel.state.articles).isNull()

        advanceUntilIdle()
        assertThat(sharedArticlesViewModel.state.articles).isNotNull()
        assertThat(sharedArticlesViewModel.state.articles!!.size).isEqualTo(articlesDataDtoStub.SIZE_OF_RESULTS)
        assertThat(sharedArticlesViewModel.state.articles).isEqualTo(
            articlesDataDtoStubDefault.results!!.map { it.toArticle() }.map { it.toArticleUi() }
        )
        assertThat(sharedArticlesViewModel.state.isLoadingArticles).isFalse()
    }

    @Test
    fun getMoreArticles() = runTest(testCoroutineDispatchers.testDispatcher) {
        assertThat(sharedArticlesViewModel.state.isLoadingMoreArticles).isFalse()
        // Get fresh articles
        sharedArticlesViewModel.onAction(ArticlesAction.OnRefresh)
        advanceUntilIdle()
        assertThat(sharedArticlesViewModel.state.articles).isNotNull()
        assertThat(sharedArticlesViewModel.state.articles!!.size).isEqualTo(articlesDataDtoStub.SIZE_OF_RESULTS)
        assertThat(sharedArticlesViewModel.state.articles)
            .isEqualTo(articlesDataDtoStubDefault.results!!.map { it.toArticle() }
                .map { it.toArticleUi() })
        sharedArticlesViewModel.onAction(ArticlesAction.OnLoadMoreArticles)
        runCurrent()
        assertThat(sharedArticlesViewModel.state.isLoadingMoreArticles).isTrue()
        advanceUntilIdle()
        // We should have twice as much articles
        assertThat(sharedArticlesViewModel.state.articles!!.size)
            .isEqualTo(articlesDataDtoStub.SIZE_OF_RESULTS * 2)
        assertThat(sharedArticlesViewModel.state.articles)
            .isEqualTo(articlesDataDtoStubMore.results!!.map { it.toArticle() }
                .map { it.toArticleUi() }
            )
        assertThat(sharedArticlesViewModel.state.isLoadingMoreArticles).isFalse()
    }

    @Test
    fun getRefreshedArticles() = runTest(testCoroutineDispatchers.testDispatcher) {
        // articles are loading during the init
        advanceUntilIdle()
        assertThat(sharedArticlesViewModel.state.articles).isNotNull()
        assertThat(sharedArticlesViewModel.state.isLoadingArticles).isFalse()
        sharedArticlesViewModel.onAction(ArticlesAction.OnRefresh)
        runCurrent()
        assertThat(sharedArticlesViewModel.state.isLoadingArticles).isTrue()
        advanceUntilIdle()
        assertThat(sharedArticlesViewModel.state.articles).isEqualTo(
            articlesDataDtoStubDefault.results!!.map { it.toArticle() }.map { it.toArticleUi() }
        )
        sharedArticlesViewModel.channel.test { cancelAndIgnoreRemainingEvents() }
        assertThat(sharedArticlesViewModel.state.isLoadingArticles).isFalse()
    }

    @Test
    fun wasArticleSaved() = runTest(testCoroutineDispatchers.testDispatcher) {
        localArticlesRepository.getArticles().test {
            assertThat(awaitItem()).isEqualTo(emptyList())
            awaitComplete()
        }
        assertThat(sharedArticlesViewModel.state.isLoadingArticleSave).isFalse()
        sharedArticlesViewModel.onAction(
            ArticlesAction.OnSaveArticle(
                articlesDataDtoStub.articleDtoStub.toArticle().toArticleUi()
            )
        )
        runCurrent()
        assertThat(sharedArticlesViewModel.state.isLoadingArticleSave).isTrue()
        localArticlesRepository.getArticles().test {
            // we saved only one article
            val savedArticleLink = awaitItem().first().articleLink
            assertThat(savedArticleLink).isEqualTo(
                articlesDataDtoStub.articleDtoStub.toArticle().toArticleUi()
                    .toArticleLocal(coroutineDispatchers = testCoroutineDispatchers).articleLink
            )
            awaitComplete()
        }
        sharedArticlesViewModel.channel.test {
            assertThat(awaitItem()).isEqualTo(SharedArticlesEvent.SaveSuccess)
        }
        advanceUntilIdle()
        assertThat(sharedArticlesViewModel.state.isLoadingArticleSave).isFalse()
    }

    // TODO: Include more tests...

}