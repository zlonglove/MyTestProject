package com.ISHello.Contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.ISHello.Contentprovider.Employees.Employee;

public class EmployeesDBHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "providerEmployees.db";
    private final static int VERSION = 1;
    final static String TABEL_NAME = "employee";

    public EmployeesDBHelper(Context context, String name,
                             CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public EmployeesDBHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    public EmployeesDBHelper(Context context, String name) {
        this(context, name, VERSION);
    }

    public EmployeesDBHelper(Context context) {
        this(context, DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABEL_NAME + " (" + Employee._ID
                + " INTEGER PRIMARY KEY," + Employee.NAME + " TEXT,"
                + Employee.GENDER + " TEXT," + Employee.AGE + " INTEGER" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS employee");
        onCreate(db);
    }
}
