package com.android.intdv.core.extension

fun <T> List<T>.toArrayList(): ArrayList<T> {
    if (this.isNullOrEmpty())
        return arrayListOf<T>()

    val arrList = arrayListOf<T>()
    arrList.addAll(this)
    return arrList
}