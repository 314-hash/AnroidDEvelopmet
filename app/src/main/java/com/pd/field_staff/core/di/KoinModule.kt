package com.pd.field_staff.core.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pd.field_staff.BuildConfig
import com.pd.field_staff.core.api.StaffApi
import com.pd.field_staff.ui.models.JobEntry
import com.pd.field_staff.ui.models.StaffRealmMigration
import com.pd.field_staff.ui.viewmodel.HomeViewModel
import com.pd.field_staff.ui.viewmodel.JobDetailViewModel
import com.pd.field_staff.ui.viewmodel.JobListService
import com.pd.field_staff.ui.viewmodel.LoginViewModel
import com.pd.field_staff.ui.viewmodel.MapViewModel
import com.pd.field_staff.ui.viewmodel.ProfileViewModel
import com.pd.field_staff.ui.viewmodel.UploadViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.core.module.dsl.*
import org.koin.dsl.module


fun provideKtorClient(): HttpClient {
    return HttpClient(CIO) {
        /*engine {
            https {
                serverName = "olivia.ph"
                trustManager = OliviaTrustManager(this)
            }
        }*/
        //Header
        install(DefaultRequest) {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            // TODO try to set token here
            //bearerAuth("TOKEN-HERE")
        }
        install(HttpRequestRetry){
            retryOnServerErrors(maxRetries = 2)
            exponentialDelay()
        }
        // Json
        install(ContentNegotiation) {
            gson()
        }
        //Now you see response logs inside terminal
        install(Logging) {
            logger = Logger.SIMPLE
            if (BuildConfig.DEBUG) {
                //level = LogLevel.HEADERS
                level = LogLevel.BODY
            }
        }
    }
}

fun provideGson(): Gson = GsonBuilder().create()

fun provideRealm(): Realm {
    val config = RealmConfiguration.Builder(schema = setOf(JobEntry::class))
        .name("staff-realm").schemaVersion(1).migration(StaffRealmMigration())
        .build()
    return Realm.open(config)
}

fun provideSharedPreferences(context: Context): SharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

val pdKoinModule = module {
    single {
        provideSharedPreferences(get())
    }
    single { provideGson() }
    single { provideRealm() }
    single { provideKtorClient()  }
    single { StaffApi(get(), get(), get())  }
    single { JobListService(get())  }

    // view models
    viewModelOf(::LoginViewModel)
    viewModelOf(::MapViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::UploadViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::JobDetailViewModel)

}