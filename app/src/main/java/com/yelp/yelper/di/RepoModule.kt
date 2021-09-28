package com.yelp.yelper.di

import com.yelp.yelper.repository.BusinessRepository
import com.yelp.yelper.repository.BusinessRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class RepoModule {

    @Binds
    abstract fun provideBusinessRepository(
        businessRepoImpl: BusinessRepositoryImpl
    ): BusinessRepository
}