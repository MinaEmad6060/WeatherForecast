package com.example.weatherforecast.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weatherforecast.Model.Local.Fav.FavWeather
import com.example.weatherforecast.Model.Local.Fav.FavWeatherDAO
import com.example.weatherforecast.Model.Local.Fav.dbFav
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@SmallTest
@RunWith(AndroidJUnit4::class)
class FavDaoTest {

    lateinit var db: dbFav
    lateinit var dao: FavWeatherDAO


    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp(){
        db = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            dbFav::class.java
        )
            .allowMainThreadQueries()
            .build()

        dao = db.getFavWeatherDao()
    }



    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertFavByInstanceDao_favInstance_greaterThanZeroIfSuccess() = runBlockingTest {
        // Given
        val favWeather = FavWeather()
        val resultInsert=dao.insert(favWeather)
        dao.insert(favWeather)
        dao.insert(favWeather)

        // When
        var resultTask= listOf(FavWeather())
        val job = launch {
            dao.getFavWeather().collect{
                resultTask = it
            }
        }
        job.cancelAndJoin()

        // Then
        assertThat(resultTask, not(nullValue()))
        assertThat(resultInsert, `is`(greaterThan(0)))
        assertThat(resultTask.size, `is`(3))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getFavWeatherDao_listOfFav() = runBlockingTest {
        // Given
        val favWeather = FavWeather()
        favWeather.cityName = "Alex"
        dao.insert(favWeather)

        // When
        var resultTask= listOf(FavWeather())
        val job = launch {
            dao.getFavWeather().collect{
                resultTask = it
            }
        }
        job.cancelAndJoin()

        // Then
        assertThat(resultTask, not(nullValue()))
        assertThat(resultTask.size, `is`(1))
        assertThat(resultTask[0].cityName, `is`("Alex"))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteFavWeatherDao_returnsOneIfSuccessAndZeroIfFail() = runBlockingTest {
        // Given
        val favWeather = FavWeather(1)
        val favWeather2 = FavWeather(2)
        dao.insert(favWeather)

        // When
        val result = dao.delete(favWeather)
        val result2 = dao.delete(favWeather2)


        // Then
        assertThat(result, `is`(1))
        assertThat(result2, `is`(0))
    }


    @After
    fun tearDown(){
        db.close()
    }

}