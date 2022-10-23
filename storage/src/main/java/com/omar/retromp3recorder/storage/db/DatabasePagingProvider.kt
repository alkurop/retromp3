package com.omar.retromp3recorder.storage.db

import javax.inject.Inject


class DatabasePagingProvider @Inject constructor(
    private val itemsSource: ItemsSource
) {
    fun provideItemSource(): ItemsSource {
        return itemsSource
    }
}

