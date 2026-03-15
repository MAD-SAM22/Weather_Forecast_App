package com.example.weatherapp.data.source.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.source.local.entity.FavoriteCityEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherDaoTest {

    private lateinit var database: WeatherDatabase
    private lateinit var dao: WeatherDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, WeatherDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.weatherDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndGetFavoriteCity() = runBlocking {
        val city = FavoriteCityEntity("Cairo", "Egypt", 25.0, "Sunny", "01d", 30.0, 31.0)
        dao.insertFavoriteCity(city)

        val favorites = dao.getFavoriteCities().first()
        assertEquals(1, favorites.size)
        assertEquals("Cairo", favorites[0].name)
    }

    @Test
    fun deleteFavoriteCity() = runBlocking {
        val city = FavoriteCityEntity("Cairo", "Egypt", 25.0, "Sunny", "01d", 30.0, 31.0)
        dao.insertFavoriteCity(city)
        dao.deleteFavoriteCity(city)

        val favorites = dao.getFavoriteCities().first()
        assertTrue(favorites.isEmpty())
    }
}
