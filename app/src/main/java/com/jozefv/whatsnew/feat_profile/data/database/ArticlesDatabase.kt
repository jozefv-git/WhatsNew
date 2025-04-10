package com.jozefv.whatsnew.feat_profile.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

// Placement of the @TypeConverters determinate the scope level. The largest scope
// is when annotation is placed above the @Database
@TypeConverters(value = [RoomStringListTypeConverters::class])
@Database(
    entities = [
        ArticleEntities.ArticleEntity::class, ArticleEntities.ArticleEntityFts::class
    ],
    version = 1
)


abstract class ArticlesDatabase : RoomDatabase() {
    abstract val articleDao: ArticleDao
}