package com.example.weatherforecast.Model.Repo.FavTest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherforecast.Model.Local.Fav.FavWeather
import com.example.weatherforecast.Model.Repo.FakeRepo
import com.example.weatherforecast.Model.Repo.Fav.FavRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavRepoTest {
    val favWeather = FavWeather()
    val favWeatherList = listOf(favWeather)

    lateinit var fakeFavLocalDataSource: FakeFavLocalDataSource
    lateinit var repository: FavRepo

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp(){
        fakeFavLocalDataSource= FakeFavLocalDataSource(favWeatherList.toMutableList())
        repository= FavRepo(fakeFavLocalDataSource)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertTasks_requestTasks_remoteTasks()= runBlockingTest{
        // Given
        val favWeather = FavWeather(1)
        val resultInsert=repository.insertFavWeatherLocalRepo(favWeather)

        //Then
        assertThat(resultInsert, not(nullValue()))
        assertThat(resultInsert, `is`(greaterThan(0)))

    }
}