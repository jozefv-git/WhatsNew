package com.jozefv.whatsnew.feat_profile.data.database

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RoomStringListTypeConverters {
    @TypeConverter
    fun convertFromListOfStringsToString(listOfString: List<String>): String {
        return Json.encodeToString(listOfString)
    }

    @TypeConverter
    fun convertFromStringToListOfStrings(string: String): List<String> {
        return Json.decodeFromString<List<String>>(string)
    }
}