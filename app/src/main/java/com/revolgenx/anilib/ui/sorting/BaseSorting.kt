package com.revolgenx.anilib.ui.sorting

abstract class BaseSorting() {

    interface SortingType

    @FunctionalInterface
    interface SortingColumnsInterface<F, T : SortingType> {
        fun compare(item1: F, item2: F, type: T): Int
    }
}