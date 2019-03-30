package com.example.helse.utilities

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class UpdateAirqualityWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        //this should be empty until the bug is resolved
        return Result.success()
    }
}