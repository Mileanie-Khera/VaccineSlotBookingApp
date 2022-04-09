package com.study.database

import android.database.sqlite.SQLiteDatabase

/**
 * Abstract DB Helper class
 */
abstract class DBHelper {
    /**
     * Abstract method to get an instance of DB
     *
     * @return An instance of Sqlite DB
     */
    abstract fun openDB(): SQLiteDatabase?

    /**
     * Abstract method to delete/truncate all data in a table
     */
    abstract fun deleteAllData()
}