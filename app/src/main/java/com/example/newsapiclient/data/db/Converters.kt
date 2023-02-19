package com.example.newsapiclient.data.db

import androidx.room.TypeConverter
import com.example.newsapiclient.data.model.Source
import com.example.newsapiclient.data.util.Resource

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String? {
        return source.name
    }

    @TypeConverter
    fun resource(name: String): Source{
        return Source(name, name)
    }

}