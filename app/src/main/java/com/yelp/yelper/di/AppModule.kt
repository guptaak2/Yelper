package com.yelp.yelper.di

import android.content.Context
import com.yelp.yelper.StringResourceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun stringResourceProvider(@ApplicationContext context: Context) =
        StringResourceProvider(context)
}