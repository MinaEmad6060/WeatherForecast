package com.example.weatherforecast.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.MediumTest
import com.example.weatherforecast.Model.Local.Fav.FavLocalDataSource
import com.example.weatherforecast.Model.Local.Fav.FavWeather
import com.example.weatherforecast.Model.Local.Fav.dbFav
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
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
    lateinit var localDataSource: FavLocalDataSource


    //test live data synchronously
    @get:Rule
    val rule = InstantTaskExecutorRule()


    @Before
    fun setUp(){
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            dbFav::class.java
        )
            .allowMainThreadQueries()
            .build()

        localDataSource= FavLocalDataSource()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertFavByInstanceLocal_favInstanceAndContext_greaterThanZeroIfSuccess()= runBlockingTest{
        //given
        val favWeather=FavWeather()

        //when
        val resultInsert=localDataSource.insertFavWeatherLocal(
            favWeather,ApplicationProvider.getApplicationContext()
        )


        //then
        assertThat(resultInsert, `is`(greaterThan(0)))

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getFavWeatherLocal_listOfFav() = runBlockingTest {
        // Given
        val favWeather = FavWeather()
        favWeather.cityName = "Alex"
        localDataSource.insertFavWeatherLocal(
            favWeather,ApplicationProvider.getApplicationContext()
        )

        // When

//        val resultTask = localDataSource.getFavWeatherLocal(ApplicationProvider.getApplicationContext())
//        val result = resultTask.value[0].cityName
        var resultTask: StateFlow<List<FavWeather>> = MutableStateFlow(emptyList())
        val job=launch {
            resultTask=localDataSource.getFavWeatherLocal(
                ApplicationProvider.getApplicationContext())
            }
        job.cancelAndJoin()

        // Then
        assertThat(resultTask, not(nullValue()))
        //assertThat(resultTask.size, `is`(1))
        assertThat(resultTask.value.size, `is`(1))
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteFavWeatherLocal_returnsOneIfSuccessAndZeroIfFail() = runBlockingTest {
        // Given
        val favWeather = FavWeather(1)
        val favWeather2 = FavWeather(2)
        localDataSource.insertFavWeatherLocal(
            favWeather,ApplicationProvider.getApplicationContext())

        // When
        val result = localDataSource.deleteFavWeatherLocal(
            favWeather,ApplicationProvider.getApplicationContext())
        val result2 = localDataSource.deleteFavWeatherLocal(
            favWeather2,ApplicationProvider.getApplicationContext())


        // Then
        assertThat(result, `is`(1))
        assertThat(result2, `is`(0))
    }

    @After
    fun tearDown(){
        db.close()
    }
}