package dvachmovie.di.core

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dvachmovie.AppConfig
import dvachmovie.api.DvachMovieApi
import dvachmovie.api.getOwnerContactConverterFactory
import dvachmovie.storage.SettingsStorage
import kotlinx.coroutines.runBlocking
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
internal class ApiModule {

    @Provides
    @Singleton
    fun dvachRetrofitService(cookieJar: CookieJar,
                             appConfig: AppConfig): DvachMovieApi {
        val httpClient = OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(appConfig.DVACH_URL)
                .client(httpClient)
                .addConverterFactory(getOwnerContactConverterFactory())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
        return retrofit.create(DvachMovieApi::class.java)
    }

    @Provides
    @Singleton
    fun cookieJar(settingsStorage: SettingsStorage) = object : CookieJar {
        private val header = "usercode_auth"

        val cookieValue by lazy { runBlocking { settingsStorage.getCookie().await() } }

        override fun saveFromResponse(url: HttpUrl, cookies: MutableList<okhttp3.Cookie>) {
        }

        override fun loadForRequest(url: HttpUrl): MutableList<okhttp3.Cookie> {
            return mutableListOf(okhttp3.Cookie
                    .Builder()
                    .name(header)
                    .value(cookieValue)
                    .domain("2ch.hk")
                    .build())
        }
    }
}
