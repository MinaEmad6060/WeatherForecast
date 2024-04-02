package com.example.weatherforecast.Model.ViewModel.Fav

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherforecast.Favourite.ViewModel.FavFragmentViewModel
import com.example.weatherforecast.Model.Local.Fav.DataStateFavRoom
import com.example.weatherforecast.Model.Local.Fav.FavWeather
import com.example.weatherforecast.Model.Repo.FakeFavLocalDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat

@ExperimentalCoroutinesApi
class FavViewModelTest {
    val favWeather = FavWeather(cityName = "Cairo")
    val favWeather2 = FavWeather(cityName = "Alex")
    val favWeather3 = FavWeather(cityName = "Paris")
    val favWeather4 = FavWeather(cityName = "Madrid")
    val favWeatherList = listOf(favWeather,favWeather2,favWeather3,favWeather4)

    lateinit var fakeFavLocalDataSource: FakeFavLocalDataSource
    lateinit var repository: FakeFavRepo
    lateinit var viewModel: FavFragmentViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp(){
        fakeFavLocalDataSource= FakeFavLocalDataSource(favWeatherList.toMutableList())
        repository= FakeFavRepo(fakeFavLocalDataSource)
        viewModel= FavFragmentViewModel(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertFavInstanceVM_favInstance_greaterThanZeroIfSuccess() = runBlockingTest {
        // Given
        val favWeather = FavWeather(cityName = "New City")
        var resultInsert: Long=0

        // When
        val job = launch {
            resultInsert = viewModel.insertFavWeatherVM(favWeather)
        }
        job.cancelAndJoin()

        // Then
        assertThat(resultInsert, not(nullValue()))
        assertThat(resultInsert, `is`(1))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getFavInstanceVM_listOfFav()= runBlockingTest{
        //given
        viewModel.getFavWeatherVM()


        //when
        val result=viewModel.favWeather.first()
        var resultList = emptyList<FavWeather>()
        when(result){
            is DataStateFavRoom.Success -> resultList=result.data
            else ->{}
        }

        //Then
        assertThat(resultList, not(nullValue()))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteFavWeatherVM_returnsOneIfSuccessAndZeroIfFail()= runBlockingTest{
        // Given
        val favWeather = FavWeather(cityName = "Alex")
        var resultDelete=0


        //when
        val job = launch {
            resultDelete = viewModel.deleteFavWeatherVM(favWeather)
        }
        job.cancelAndJoin()

        //Then
        assertThat(resultDelete, not(nullValue()))
        assertThat(resultDelete, `is`(1))
    }
}