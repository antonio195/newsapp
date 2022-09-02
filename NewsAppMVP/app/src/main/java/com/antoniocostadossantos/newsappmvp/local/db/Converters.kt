package com.antoniocostadossantos.newsappmvp.local.db

import androidx.room.TypeConverter
import com.antoniocostadossantos.newsappmvp.local.model.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }

}