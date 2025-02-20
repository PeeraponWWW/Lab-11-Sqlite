package com.example.lab_11_sec2version2

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context): SQLiteOpenHelper(context,DB_NAME,null,DB_VERSION){
    companion object {
        private val DB_NAME = "employeeDB"
        private val DB_VERSION = 2
        private val TABLE_NAME = "employee"
        private val COLUMN_ID = "emp_id"
        private val COLUMN_NAME = "emp_name"
        private val COLUMN_SALARY = "emp_salary"
        private val COLUMN_POSITION = "emp_position"
        private val sqliteHelper: DatabaseHelper? = null

        @Synchronized
        fun getInstance(c: Context): DatabaseHelper {
            if (sqliteHelper == null) {
                return DatabaseHelper(c.applicationContext)
            } else {
                return sqliteHelper
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($COLUMN_ID TXET PRIMARY KEY, " +
                "$COLUMN_NAME TEXT, " + "$COLUMN_SALARY INTEGER," + "$COLUMN_POSITION TEXT)"
        db?.execSQL(CREATE_TABLE)

        val sqlInsert ="INSERT INTO $TABLE_NAME VALUES('EMP-001','David Lee',160000, 'Front end developer')"
        db?.execSQL(sqlInsert)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    @SuppressLint("Range")
    fun getAllEmployee():ArrayList<Employee>{
        val employee = ArrayList<Employee>()
        val db = readableDatabase
        var cursor: Cursor? = null
        try{ cursor = db.rawQuery("SELECT * FROM $TABLE_NAME",null)
        }catch (e: SQLiteException){
            onCreate(db)
            return ArrayList()
        }
        var emp_id :String
        var emp_name :String
        var emp_salary :Int
        var emp_position: String
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast){
                emp_id = cursor.getString(cursor.getColumnIndex(COLUMN_ID))
                emp_name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                emp_salary = cursor.getInt(cursor.getColumnIndex(COLUMN_SALARY))
                emp_position = cursor.getString(cursor.getColumnIndex(COLUMN_POSITION))
                employee.add(Employee(emp_id,emp_name,emp_salary, emp_position))
                cursor.moveToNext()
            }
        }
        db.close()
        return employee
    }
    fun insertEmployee(emp:Employee):Long{
        val db = writableDatabase
        val value = ContentValues()
        value.put(COLUMN_ID,emp.emp_id)
        value.put(COLUMN_NAME,emp.emp_name)
        value.put(COLUMN_SALARY,emp.emp_salary)
        value.put(COLUMN_POSITION,emp.emp_position)
        val success = db.insert(TABLE_NAME,null,value)
        db.close()
        return success
    }

    fun updateEmployee(emp:Employee):Int{
        val db = writableDatabase
        val value = ContentValues()
        value.put(COLUMN_NAME, emp.emp_name)
        value.put(COLUMN_SALARY, emp.emp_salary)
        value.put(COLUMN_POSITION, emp.emp_position)
        val success =db.update(TABLE_NAME,value,"$COLUMN_ID = ?", arrayOf(emp.emp_id))
        db.close()
        return success
    }

    fun deleteEmployee(emp_id:String):Int{
        val db = writableDatabase
        val success = db.delete(TABLE_NAME,"$COLUMN_ID = ?", arrayOf(emp_id))
        db.close()
        return success
    }

}