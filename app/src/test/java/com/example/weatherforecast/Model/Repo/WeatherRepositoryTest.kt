package com.example.weatherforecast.Model.Repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherforecast.Model.Local.Fav.FavWeather
import com.example.weatherforecast.Model.Repo.FavTest.FakeFavLocalDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class WeatherRepositoryTest{

    val favWeather = FavWeather()
    val favWeatherList = listOf(favWeather)

    lateinit var fakeFavLocalDataSource: FakeFavLocalDataSource
    lateinit var repository: FakeRepo

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp(){
        fakeFavLocalDataSource= FakeFavLocalDataSource(favWeatherList.toMutableList())
        repository=FakeRepo(fakeFavLocalDataSource)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertTasks_requestTasks_remoteTasks()= runBlockingTest{
// Given
        val favWeather = FavWeather(1)
//        val resultInsert=repository.insertFavWeatherLocalRepo(favWeather)

        // Then
//        assertThat(resultInsert, not(nullValue()))
//        assertThat(resultInsert, `is`(greaterThan(0)))

    }
}