package com.antoniocostadossantos.newsappmvp.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.antoniocostadossantos.newsappmvp.local.model.Article

@Database(entities = [Article::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ArticleDataBase : RoomDatabase() {

    abstract fun getArticlesDao(): ArticleDao

    companion object {

        @Volatile
        private var instance: ArticleDataBase? = null

        private val Lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(Lock) {
            instance ?: createdDataBase(context).also { articleDataBase ->
                instance = articleDataBase
            }
        }

        private fun createdDataBase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDataBase::class.java,
                "article_db.db"
            ).build()
    }
}
