package com.android.intdv.movie.usecase

import com.android.intdv.TestCoroutineRule
import com.android.intdv.core.exception.Failure
import com.android.intdv.core.functional.Either
import com.android.intdv.movie.data.MovieType
import com.android.intdv.movie.data.MoviesRepository
import com.android.intdv.movie.data.remote.response.Genres
import com.android.intdv.movie.data.remote.response.MovieDetails
import com.android.intdv.movie.data.remote.response.MovieDetailsResponse
import com.android.intdv.movie.data.remote.response.MovieListResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Unit test class for GetMoviesUseCase
 */
class GetMoviesUseCaseTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val testCoroutineRule =
        TestCoroutineRule()

    @Before
    fun init() {
        // Resources can be initialized here
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getCurrentlyPlayingMovieList_success() {

        val latch = CountDownLatch(1)

        val movieList: ArrayList<MovieDetails> = ArrayList()
        val title = "Cruella"
        val overview =
            "In 1970s London amidst the punk rock revolution, a young grifter named Estella is determined to make a name for herself with her designs. She befriends a pair of young thieves who appreciate her appetite for mischief, and together they are able to build a life for themselves on the London streets. One day, Estella’s flair for fashion catches the eye of the Baroness von Hellman, a fashion legend who is devastatingly chic and terrifyingly haute. But their relationship sets in motion a course of events and revelations that will cause Estella to embrace her wicked side and become the raucous, fashionable and revenge-bent Cruella."
        val postPath = "/rTh4K5uw9HypmpGslcKd4QfHl93.jpg"
        val releaseDate = "2021-05-26"
        val voteAverage = 8.6f

        movieList.add(MovieDetails(1, postPath, title, overview, voteAverage, releaseDate))

        val repository = mock(MoviesRepository::class.java)
        val movieListResponse = MovieListResponse(1, 1, 1, movieList)

        `when`(repository.getCurrentPlayingMovies("", 1, ""))
            .thenReturn(Either.Right(movieListResponse))

        val moviesUseCaseTest = GetMoviesUseCase(repository)

        var response: MovieListResponse? = null
        var failureResponse: Failure? = null

        moviesUseCaseTest(
            GetMoviesUseCase.Params(
                MovieType.CURRENTLY_PLAYING,
                "", 1, ""
            )
        ) {
            it.either(fun(failure: Failure) {
                failureResponse = failure
                latch.countDown()
            }, fun(movieListResponse: MovieListResponse?) {
                response = movieListResponse
                latch.countDown()
            })
        }
        try {
            latch.await(3000, TimeUnit.MILLISECONDS)
        } catch (e: InterruptedException) {
            e.printStackTrace()
            println("InterruptedException:" + e.printStackTrace())
        }

        //Verify that rows is not empty and has some valid row data
        Assert.assertTrue(response?.movieList?.isNotEmpty() == true)

        //Verify no of rows available in the response
        Assert.assertEquals(1, response?.movieList?.size)

        //Verify the details for the first row
        Assert.assertEquals("Title not matched", title, response?.movieList?.get(0)?.title)
        Assert.assertEquals(overview, response?.movieList?.get(0)?.overview)
        Assert.assertEquals(postPath, response?.movieList?.get(0)?.posterPath)
        Assert.assertEquals(releaseDate, response?.movieList?.get(0)?.releaseDate)

        //Verify that there is no failure
        Assert.assertNull(failureResponse)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getPopularMovies() {

        testCoroutineRule.runBlockingTest {
            val language = "en-US"
            val apiKey = "en-US"
            val pageNo = 1

            val repository = mock(MoviesRepository::class.java)

            val moviesUseCaseTest = GetMoviesUseCase(repository)
            moviesUseCaseTest.run(
                GetMoviesUseCase.Params(
                    MovieType.POPULAR,
                    language,
                    pageNo,
                    apiKey
                )
            )

            verify(repository, times(1)).getPopularMovies(language, pageNo, apiKey)
            verify(repository, times(0)).getCurrentPlayingMovies(language, pageNo, apiKey)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getCurrentlyPlayingMovies() {
        testCoroutineRule.runBlockingTest {
            val language = "en-US"
            val apiKey = "en-US"
            val pageNo = 1

            val repository = mock(MoviesRepository::class.java)

            val moviesUseCaseTest = GetMoviesUseCase(repository)
            moviesUseCaseTest.run(
                GetMoviesUseCase.Params(
                    MovieType.CURRENTLY_PLAYING,
                    language,
                    pageNo,
                    apiKey
                )
            )

            verify(repository, times(1)).getCurrentPlayingMovies(language, pageNo, apiKey)
            verify(repository, times(0)).getPopularMovies(language, pageNo, apiKey)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getCurrentlyPlayingMovieList_failed() {

        val latch = CountDownLatch(1)

        val repository = mock(MoviesRepository::class.java)
        val featureFailure = Failure.FeatureFailure
        `when`(
            repository.getCurrentPlayingMovies(
                "",
                1,
                ""
            )
        ).thenReturn(Either.Left(featureFailure))

        val moviesUseCaseTest = GetMoviesUseCase(repository)
        var response: MovieListResponse? = null
        var failureResponse: Failure? = null

        moviesUseCaseTest(
            GetMoviesUseCase.Params(
                MovieType.CURRENTLY_PLAYING,
                "", 1, ""
            )
        ) {
            it.either(fun(failure: Failure) {
                failureResponse = failure
                latch.countDown()
            }, fun(movieListResponse: MovieListResponse?) {
                response = movieListResponse
                latch.countDown()
            })
            try {
                latch.await(3000, TimeUnit.MILLISECONDS)
            } catch (e: InterruptedException) {
                e.printStackTrace()
                println("InterruptedException:" + e.printStackTrace())
            }
            Assert.assertNull(response)
            Assert.assertTrue(failureResponse is Failure.FeatureFailure)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getCurrentlyPlayingMovieList_no_internet() {

        val latch = CountDownLatch(1)

        val repository = mock(MoviesRepository::class.java)
        val noInternet = Failure.NetworkConnection
        `when`(
            repository.getCurrentPlayingMovies(
                "",
                1,
                ""
            )
        ).thenReturn(Either.Left(noInternet))

        val moviesUseCaseTest = GetMoviesUseCase(repository)
        var response: MovieListResponse? = null
        var failureResponse: Failure? = null

        moviesUseCaseTest(
            GetMoviesUseCase.Params(
                MovieType.CURRENTLY_PLAYING,
                "", 1, ""
            )
        ) {
            it.either(fun(failure: Failure) {
                failureResponse = failure
                latch.countDown()
            }, fun(movieListResponse: MovieListResponse?) {
                response = movieListResponse
                latch.countDown()
            })

        }
        try {
            latch.await(3000, TimeUnit.MILLISECONDS)
        } catch (e: InterruptedException) {
            e.printStackTrace()
            println("InterruptedException:" + e.printStackTrace())
        }
        Assert.assertNull(response)
        Assert.assertTrue(failureResponse is Failure.NetworkConnection)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun searchMovies_success() {

        val latch = CountDownLatch(1)

        val movieList: ArrayList<MovieDetails> = ArrayList()
        val title = "Cruella"
        val overview =
            "In 1970s London amidst the punk rock revolution, a young grifter named Estella is determined to make a name for herself with her designs. She befriends a pair of young thieves who appreciate her appetite for mischief, and together they are able to build a life for themselves on the London streets. One day, Estella’s flair for fashion catches the eye of the Baroness von Hellman, a fashion legend who is devastatingly chic and terrifyingly haute. But their relationship sets in motion a course of events and revelations that will cause Estella to embrace her wicked side and become the raucous, fashionable and revenge-bent Cruella."
        val postPath = "/rTh4K5uw9HypmpGslcKd4QfHl93.jpg"
        val releaseDate = "2021-05-26"
        val voteAverage = 8.6f

        movieList.add(MovieDetails(1, postPath, title, overview, voteAverage, releaseDate))

        val repository = mock(MoviesRepository::class.java)
        val movieListResponse = MovieListResponse(1, 1, 1, movieList)

        `when`(repository.searchMovies("en-US", 1, "some-api-key", "query"))
            .thenReturn(Either.Right(movieListResponse))

        val searchMoviesUseCase = SearchMoviesUseCase(repository)

        var response: MovieListResponse? = null
        var failureResponse: Failure? = null

        searchMoviesUseCase(
            SearchMoviesUseCase.Params(
                "en-US", 1, "some-api-key","query"
            )
        ) {
            it.either(fun(failure: Failure) {
                failureResponse = failure
                latch.countDown()
            }, fun(movieListResponse: MovieListResponse?) {
                response = movieListResponse
                latch.countDown()
            })
        }
        try {
            latch.await(3000, TimeUnit.MILLISECONDS)
        } catch (e: InterruptedException) {
            e.printStackTrace()
            println("InterruptedException:" + e.printStackTrace())
        }

        //Verify that rows is not empty and has some valid row data
        Assert.assertTrue(response?.movieList?.isNotEmpty() == true)

        //Verify no of rows available in the response
        Assert.assertEquals(1, response?.movieList?.size)

        //Verify the details for the first row
        Assert.assertEquals("Title not matched", title, response?.movieList?.get(0)?.title)
        Assert.assertEquals(overview, response?.movieList?.get(0)?.overview)
        Assert.assertEquals(postPath, response?.movieList?.get(0)?.posterPath)
        Assert.assertEquals(releaseDate, response?.movieList?.get(0)?.releaseDate)

        //Verify that there is no failure
        Assert.assertNull(failureResponse)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getMovieDetails_success() {

        val latch = CountDownLatch(1)
        val genresList: ArrayList<Genres> = ArrayList()
        val title = "Cruella"
        val overview =
            "In 1970s London amidst the punk rock revolution, a young grifter named Estella is determined to make a name for herself with her designs. She befriends a pair of young thieves who appreciate her appetite for mischief, and together they are able to build a life for themselves on the London streets. One day, Estella’s flair for fashion catches the eye of the Baroness von Hellman, a fashion legend who is devastatingly chic and terrifyingly haute. But their relationship sets in motion a course of events and revelations that will cause Estella to embrace her wicked side and become the raucous, fashionable and revenge-bent Cruella."
        val postPath = "/rTh4K5uw9HypmpGslcKd4QfHl93.jpg"
        val releaseDate = "2021-05-26"
        val runtime = 143L

        genresList.add(Genres(1, "Action"))
        genresList.add(Genres(2, "Darama"))

        val repository = mock(MoviesRepository::class.java)
        val movieDetailsResponse =
            MovieDetailsResponse(1, runtime, postPath, title, overview, releaseDate, genresList)

        `when`(repository.getMovieDetails(1, "", ""))
            .thenReturn(Either.Right(movieDetailsResponse))

        val movieDetailsUseCase = GetMovieDetailsUseCase(repository)

        var response: MovieDetailsResponse? = null
        var failureResponse: Failure? = null

        movieDetailsUseCase(GetMovieDetailsUseCase.Params(1, "", "")) {
            it.either(fun(failure: Failure) {
                failureResponse = failure
                latch.countDown()
            }, fun(movieDetailsResponse: MovieDetailsResponse?) {
                response = movieDetailsResponse
                latch.countDown()
            })
        }
        try {
            latch.await(3000, TimeUnit.MILLISECONDS)
        } catch (e: InterruptedException) {
            e.printStackTrace()
            println("InterruptedException:" + e.printStackTrace())
        }
        //Verify the movie details
        Assert.assertEquals("Title not matched", title, response?.title)
        Assert.assertEquals(overview, response?.overview)
        Assert.assertEquals(postPath, response?.posterPath)
        Assert.assertEquals(releaseDate, response?.releaseDate)
        Assert.assertEquals(runtime, response?.runtime)

        //Verify that genres is not empty and has some valid data
        Assert.assertTrue(response?.genres?.isNotEmpty() == true)

        //Verify no of rows available in the response
        Assert.assertEquals(2, response?.genres?.size)

        //Verify the details for the first row
        Assert.assertEquals("Genres id not matching",1, response?.genres?.get(0)?.id)
        Assert.assertEquals("Genres name not matching","Action", response?.genres?.get(0)?.name)

        //Verify the details for the first row
        Assert.assertEquals("Genres id not matching", 2, response?.genres?.get(1)?.id)
        Assert.assertEquals("Genres name not matching","Darama", response?.genres?.get(1)?.name)

        //Verify that there is no failure
        Assert.assertNull(failureResponse)
    }
}