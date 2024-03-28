package com.example.weatherforecast.Model.ViewModel.Alert

import com.example.weatherforecast.Model.ViewModel.Fav.FakeFavRepo
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherforecast.Alert.ViewModel.AlertFragmentViewModel
import com.example.weatherforecast.Favourite.ViewModel.FavFragmentViewModel
import com.example.weatherforecast.Model.Local.Alert.AlertCalendar
import com.example.weatherforecast.Model.Local.Alert.DataStateAlertRoom
import com.example.weatherforecast.Model.Local.Fav.DataStateFavRoom
import com.example.weatherforecast.Model.Local.Fav.FavWeather
import com.example.weatherforecast.Model.Remote.Alert.DataStateAlertRemote
import com.example.weatherforecast.Model.Remote.Alert.OneCallAlert
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
class AlertViewModelTest {
    val oneCallAlert = OneCallAlert(timezone = "Alex")
    val alertWeather = AlertCalendar("alert")
    val alertWeather2 = AlertCalendar("alert2")
    val alertWeather3 = AlertCalendar("alert3")
    val alertWeather4 = AlertCalendar("alert4")
    val alertWeatherList = listOf(alertWeather,alertWeather2,alertWeather3,alertWeather4)

    lateinit var fakeAlertLocalDataSource: FakeAlertLocalDataSource
    lateinit var fakeAlertRemoteDataSource: FakeAlertRemoteDataSource
    lateinit var repository: FakeAlertRepo
    lateinit var viewModel: AlertFragmentViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp(){
        fakeAlertLocalDataSource=FakeAlertLocalDataSource(alertWeatherList.toMutableList())
        fakeAlertRemoteDataSource=FakeAlertRemoteDataSource(oneCallAlert)
        repository= FakeAlertRepo(
            fakeAlertRemoteDataSource,
            fakeAlertLocalDataSource
        )
        viewModel= AlertFragmentViewModel(repository)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getTasksRemote_requestTasks_remoteTasks()= runBlockingTest{
        //given
        viewModel.getAlertWeatherRemoteVM(
            33.44, -94.04, "a92ea15347fafa48d308e4c367a39bb8"
        )


        //when
        val result=viewModel.alertWeatherRemote.first()
        var resultList = OneCallAlert()
        val job = launch {
            when(result){
                is DataStateAlertRemote.Success -> resultList=result.data
                else ->{}
            }
        }
        job.cancelAndJoin()


        //Then
        assertThat(resultList, not(nullValue()))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertTasks_requestTasks_remoteTasks() = runBlockingTest {
        // Given
        val alertCalendar = AlertCalendar()
        var resultInsert: Long=0
        // When
        val job = launch {
            resultInsert = viewModel.insertAlertWeatherVM(alertCalendar)
        }
        job.cancelAndJoin()


        // Then
        assertThat(resultInsert, not(nullValue()))
        assertThat(resultInsert, `is`(1))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getTasks_requestTasks_remoteTasks()= runBlockingTest{
        //given
        viewModel.getAlertWeatherVM()


        //when
        val result=viewModel.alertWeatherRoom.first()
        var resultList = emptyList<AlertCalendar>()
        when(result){
            is DataStateAlertRoom.Success -> resultList=result.data
            else ->{}
        }

        //Then
        assertThat(resultList, not(nullValue()))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteTasks_requestTasks_remoteTasks()= runBlockingTest{
        // Given
        val alertCalendarId = "alert"
        var resultDelete=0


        //when
        val job = launch {
            resultDelete = viewModel.deleteAlertWeatherVM(alertCalendarId)
        }
        job.cancelAndJoin()

        //Then
        assertThat(resultDelete, not(nullValue()))
        assertThat(resultDelete, `is`(1))
    }
}