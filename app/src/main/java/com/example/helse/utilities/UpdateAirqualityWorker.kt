package com.example.helse.utilities

import android.content.Context
import android.util.Log.d
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay

class UpdateAirqualityWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        d("ARAN", "Det har gått 15 min")
        return Result.success()
    }
}