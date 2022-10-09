package com.example.demo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.demo.network.Api
import com.example.demo.network.data.Attractions
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel : ViewModel(){
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _attractions = MutableLiveData<Attractions>()
    val attractions : LiveData<Attractions>
        get() = _attractions

    init {
        getAllRx()
    }

    private fun getAll(){
        coroutineScope.launch {
            val getAllDeferred = Api.retrofitService.getAll()
            try {
                _attractions.value  = getAllDeferred.await()
            } catch (t: Throwable){
                _attractions.value = null
            }
        }
    }

    private fun getAllRx(){
        Api.retrofitRxService.getAllRx()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getAllObserver())
    }

    private fun getAllObserver(): Observer<Attractions> {
        return object :Observer<Attractions>{
            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Attractions) {
                _attractions.value  = t
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}