package com.study.database

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.study.learndagger.CSBApp
import java.util.*

/**
 * Favorite table data access object class
 */
object CowinDao : DBHelper() {
    private const val TABLE_NAME = "tbl_cowin_data"

    // Column Names
    private const val KEY_SERIAL_ID = "_id"
    private const val KEY_TIMESTAMP = "key_timestamp"
    private const val KEY_ERROR = "key_error"
    private const val KEY_CONFIRMATION_NO = "key_confirmation_no"
    private const val KEY_LABEL_LOCALE_CODE = "key_label_locale_code"
    private const val KEY_LABEL_VALUE = "key_label_value"
    const val CREATE_TABLE_QUERY = ("CREATE TABLE "
            + TABLE_NAME + "(" + KEY_SERIAL_ID + " TEXT PRIMARY KEY, "
            + " TEXT, " + KEY_TIMESTAMP + " TEXT, " + KEY_LABEL_VALUE + " TEXT, " + KEY_LABEL_LOCALE_CODE
            + " TEXT, " + KEY_ERROR + " TEXT, " + KEY_CONFIRMATION_NO + " TEXT)")
    private val mDatabase: SQLiteDatabase

    init {
        mDatabase = openDB()
    }

    @Synchronized
    fun insertCowinData(cowinDaoModel: CowinDaoModel) {
        mDatabase.beginTransaction()
        try {
            val localContentValues = ContentValues()

            localContentValues.put(KEY_TIMESTAMP, cowinDaoModel.timestamp)
            localContentValues.put(KEY_ERROR, cowinDaoModel.error)
            localContentValues.put(KEY_CONFIRMATION_NO, cowinDaoModel.confirmationNo)
            localContentValues.put(KEY_LABEL_LOCALE_CODE, "")
            localContentValues.put(KEY_LABEL_VALUE, "")

            mDatabase.insertWithOnConflict(
                TABLE_NAME, null, localContentValues,
                SQLiteDatabase.CONFLICT_REPLACE
            )
            mDatabase.setTransactionSuccessful()
        } catch (e: Exception) {
        } finally {
            mDatabase.endTransaction()
        }
    }

    fun getCowinData(): ArrayList<CowinDaoModel>? {
        val dataArrayList: ArrayList<CowinDaoModel> = ArrayList<CowinDaoModel>()
        val cursor: Cursor = mDatabase.query(
            TABLE_NAME, null, null,
            null, null, null, "$KEY_TIMESTAMP DESC", "20"
        )
        try {
            if (cursor.count > 0) {
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val data = CowinDaoModel()
                    data.timestamp = cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMP))
                    data.error = cursor.getString(cursor.getColumnIndex(KEY_ERROR))
                    data.confirmationNo =
                        cursor.getString(cursor.getColumnIndex(KEY_CONFIRMATION_NO))
                    dataArrayList.add(data)
                    cursor.moveToNext()
                }
            }
        } catch (e: java.lang.Exception) {
        } finally {
            cursor.close()
        }
        return dataArrayList
    }

    @Synchronized
    override fun deleteAllData() {
        mDatabase.delete(TABLE_NAME, null, null)
    }

    override fun openDB(): SQLiteDatabase {
        return DatabaseHelper.getDatabaseHelperInstance(CSBApp.mCSBApp)
            .writableDatabase
    }
}