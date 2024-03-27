package com.example.weatherforecast.Model.Repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherforecast.Model.Local.Fav.FavWeather
import com.example.weatherforecast.Model.Repo.Fav.FavRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavRepoTest {
    val favWeather = FavWeather(cityName = "Cairo")
    val favWeather2 = FavWeather(cityName = "Alex")
    val favWeather3 = FavWeather(cityName = "Paris")
    val favWeather4 = FavWeather(cityName = "Madrid")
    val favWeatherList = listOf(favWeather,favWeather2,favWeather3,favWeather4)

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

        //when
        val resultInsert=repository.insertFavWeatherLocalRepo(favWeather)

        //Then
        assertThat(resultInsert, not(nullValue()))
        assertThat(resultInsert, `is`(1))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getTasks_requestTasks_remoteTasks()= runBlockingTest{
        // Given
        var resultList = listOf(FavWeather())

        //when
        val resultGet=repository.getFavWeatherLocalRepo()
        resultGet.collect{
            resultList=it
        }

        //Then
        assertThat(resultList, not(nullValue()))
        assertThat(resultList.size, `is`(4))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteTasks_requestTasks_remoteTasks()= runBlockingTest{
        // Given
        val favWeather = FavWeather(cityName = "Alex")
        var resultList = listOf(FavWeather())


        //when
        val resultInsert=repository.deleteFavWeatherLocalRepo(favWeather)
        val resultGet=repository.getFavWeatherLocalRepo()
        resultGet.collect{
            resultList=it
        }

        //Then
        assertThat(resultInsert, not(nullValue()))
        assertThat(resultList.size, `is`(3))
        assertThat(resultInsert, `is`(1))
    }
}