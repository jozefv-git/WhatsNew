package com.jozefv.whatsnew.feat_profile.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

object ArticleEntities {

    @Entity(tableName = "articles_fts")
    @Fts4(contentEntity = ArticleEntity::class)
    data class ArticleEntityFts(
        // fields that are indexable in which we are going to do the search
        @ColumnInfo(name = FTS_COLUMN_NAME)
        @PrimaryKey(autoGenerate = true) val id: Int,
        val title: String,
        val description: String,
        val filters: List<String>?
    )

    @Entity(tableName = "articles")
    class ArticleEntity(
        @PrimaryKey(autoGenerate = false)
        val articleLink: String,
        val title: String?,
        val imageByteArray: ByteArray?,
        val description: String?,
        val publishedDate: String,
        val sourceName: String?,
        val sourceUrl: String?,
        val filters: List<String>?
    )

    // For more - check doc:
    // https://developer.android.com/reference/androidx/room/Fts4
    // https://www.sqlite.org/fts3.html
    private const val FTS_COLUMN_NAME = "rowid"
}

