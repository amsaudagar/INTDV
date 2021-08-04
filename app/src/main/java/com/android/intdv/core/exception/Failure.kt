package com.android.intdv.core.exception

sealed class Failure {
    object NetworkConnection : Failure()
    object ServerError : Failure()
    object FeatureFailure : Failure()
}