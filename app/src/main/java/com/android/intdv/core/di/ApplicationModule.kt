package com.android.intdv.core.di

import com.android.intdv.movie.data.remote.MoviesService
import com.android.intdv.core.platform.NetworkHandler
import com.android.intdv.movie.data.MoviesRepository
import com.android.intdv.movie.usecase.GetMovieDetailsUseCase
import com.android.intdv.movie.usecase.GetMoviesUseCase
import com.android.intdv.movie.usecase.SearchMoviesUseCase
import com.android.intdv.movie.viewmodel.MoviesViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


const val PROD_ENVIRONMENT = "https://api.themoviedb.org/3/"
const val TEST_ENVIRONMENT = "https://api.themoviedb.org/3/"

const val API_KEY = "bec334d6579b292848916f0e271fb52d"
const val LANGUAGE = "en-US"

const val REST_CONNECTION_TIMEOUT_MS = 20000L

//Sets the current environment
var CURRENT_ENV = PROD_ENVIRONMENT

val applicationModule = module {

    single {
        Retrofit.Builder()
            .baseUrl(CURRENT_ENV)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {

        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()

        okHttpClientBuilder
            .callTimeout(REST_CONNECTION_TIMEOUT_MS * 2, TimeUnit.MILLISECONDS)
            .connectTimeout(REST_CONNECTION_TIMEOUT_MS * 2, TimeUnit.MILLISECONDS)
            .writeTimeout(REST_CONNECTION_TIMEOUT_MS * 2, TimeUnit.MILLISECONDS)
            .readTimeout(REST_CONNECTION_TIMEOUT_MS * 2, TimeUnit.MILLISECONDS)
        okHttpClientBuilder.build() as OkHttpClient
    }

    factory { NetworkHandler(get()) }
    factory { MoviesService(get()) }
}

val movieModule = module {
    single { MoviesRepository.MoviesRepositoryImp(get(), get()) as MoviesRepository }
    single { GetMoviesUseCase(get()) }
    single { GetMovieDetailsUseCase(get()) }
    single { SearchMoviesUseCase(get()) }
    viewModel { MoviesViewModel(get(), get(), get()) }
}