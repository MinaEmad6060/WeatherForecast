package com.example.weatherforecast.data.source.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.MediumTest
import com.example.weatherforecast.Model.Local.Fav.FavLocalDataSource
import com.example.weatherforecast.Model.Local.Fav.FavWeather
import com.example.weatherforecast.Model.Local.Fav.FavWeatherDAO
import com.example.weatherforecast.Model.Local.Fav.dbFav
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.greaterThan
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.sql.ResultSet



@MediumTest
class FavLocalDataSourceTest {

    lateinit var db: dbFav
    lateinit var dao: FavWeatherDAO
    lateinit var localDataSource: FavLocalDataSource
    lateinit var context: Context


    //test live data synchronously
    @get:Rule
    val rule = InstantTaskExecutorRule()


    @Before
    fun setUp(){
        context= ApplicationProvider.getApplicationContext()


        db = Room.inMemoryDatabaseBuilder(
            context,
            dbFav::class.java
        )
            .allowMainThreadQueries()
            .build()

        dao = db.getFavWeatherDao()


        localDataSource= FavLocalDataSource(dao)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertFavByInstanceLocal_favInstanceAndContext_greaterThanZeroIfSuccess()= runBlockingTest{
        //given
        val favWeather=FavWeather()

        //when
        val resultInsert=localDataSource.insertFavWeatherLocal(
            favWeather
        )

        //then
        assertThat(resultInsert, `is`(greaterThan(0)))

    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getCurrentWeather_returnTrueOrFalse()= runBlockingTest {
        //Given
        val favWeather = FavWeather()
        favWeather.cityName="Alex"
        localDataSource.insertFavWeatherLocal(favWeather)


        var resultTask=listOf(FavWeather())
        //When
        val job = launch {
            localDataSource.getFavWeatherLocal().collect{
                resultTask = it
            }
        }
        job.cancelAndJoin()

        //Then
        assertThat(resultTask, not(nullValue()))
        assertThat(resultTask.size, `is`(1))
        assertThat(resultTask[0].cityName, `is`("Alex"))
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteFavWeatherLocal_returnsOneIfSuccessAndZeroIfFail() = runBlockingTest {
        // Given
        val favWeather = FavWeather(1)
        val favWeather2 = FavWeather(2)
        localDataSource.insertFavWeatherLocal(
            favWeather)


        // When
        val result = localDataSource.deleteFavWeatherLocal(
            favWeather)
        val result2 = localDataSource.deleteFavWeatherLocal(
            favWeather2)


        // Then
        assertThat(result, `is`(1))
        assertThat(result2, `is`(0))
    }

    @After
    fun tearDown(){
        db.close()
    }
}