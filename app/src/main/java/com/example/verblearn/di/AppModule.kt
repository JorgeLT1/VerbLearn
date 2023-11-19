package com.example.verblearn.di

import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.verblearn.data.remote.VerbsAPI
import com.example.verblearn.data.repository.VerbsRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn( SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideVerbApi(moshi: Moshi): VerbsAPI {
        return Retrofit.Builder()
            .baseUrl("http://www.VerbLearnAPI.somee.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(VerbsAPI::class.java)
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @Provides
    @Singleton
    fun provideGastoRepository(verbApi: VerbsAPI): VerbsRepository {
        return VerbsRepository(verbApi)
    }
}