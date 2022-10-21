package com.omar.retromp3recorder.storage.db

import androidx.paging.DataSource
import androidx.room.*
import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.dto.Wavetable

@Entity
data class FileDbEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(defaultValue = "0")
    val id: Long,
    val created: Long = 0,
    val lastModified: Long,
    val filepath: String,
    @Embedded
    val waveform: WaveformDbEntity?,
    val length: Long?
)

@Entity
data class WaveformDbEntity(
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val waveform: ByteArray = ByteArray(0),
    val stepMillis: Int? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WaveformDbEntity

        if (!waveform.contentEquals(other.waveform)) return false
        if (stepMillis != other.stepMillis) return false

        return true
    }

    override fun hashCode(): Int {
        var result = waveform.contentHashCode()
        result = 31 * result + (stepMillis ?: 0)
        return result
    }
}

@Dao
interface FileDbEntityDao {

    @Query("SELECT * FROM FileDbEntity ORDER BY id DESC limit 1")
    fun takeLast(): FileDbEntity?

    @Query("SELECT * FROM FileDbEntity ORDER BY lastModified DESC")
    fun getAllPagingData(): DataSource.Factory<Int, FileDbEntity>

    @Query("SELECT * FROM FileDbEntity ORDER BY id DESC limit :pageSize offset :offsetItems")
    fun getAllPaging(pageSize: Int, offsetItems: Int): List<FileDbEntity>

    @Query("SELECT * from FileDbEntity WHERE filepath = :filepath")
    fun getByFilepath(filepath: String): List<FileDbEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBatch(items: List<FileDbEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: FileDbEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateItem(item: FileDbEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(items: List<FileDbEntity>)

    @Delete
    fun delete(items: List<FileDbEntity>)

    @Query("DELETE from FileDbEntity where filepath=:filePath")
    fun deleteByFilepath(filePath: String)

    companion object {
        const val LOAD_SIZE = 5
    }
}

fun FileDbEntity.toFileWrapper(): ExistingFileWrapper =
    ExistingFileWrapper(
        this.id,
        this.filepath,
        this.created,
        this.lastModified,
        this.waveform?.toWavetable(),
        this.length
    )

fun ExistingFileWrapper.toDatabaseEntity(): FileDbEntity = FileDbEntity(
    this.id,
    this.createTimedStamp,
    this.modifiedTimestamp,
    this.path,
    waveform = this.wavetable?.toDatabaseEntity(),
    length
)

fun Wavetable.toDatabaseEntity() = WaveformDbEntity(this.data, stepMillis)
fun WaveformDbEntity.toWavetable() =
    Wavetable(this.waveform, stepMillis ?: 100 /*old format, may still be present in old versions*/)

