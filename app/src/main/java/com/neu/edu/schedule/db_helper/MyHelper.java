package com.neu.edu.schedule.db_helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.neu.edu.schedule.bean.ScheduleBean;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.neu.edu.schedule.recordFragments.RecordFragment;

import java.sql.SQLException;

/**
 *
 */
public class MyHelper extends OrmLiteSqliteOpenHelper {

    private static final String DB_NAME = "Schedule.db";
    private static final int DB_VERSION = 1;

    private Dao<ScheduleBean,Integer> myScheduleDao = null;
    private  static MyHelper myHelperInstance = null;

    private MyHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    public static synchronized MyHelper getInstance(Context context){
        if(myHelperInstance == null){
            myHelperInstance = new MyHelper(context);
        }
        return myHelperInstance;
    }

    public Dao<ScheduleBean,Integer> getMyScheduleDao() throws SQLException {
        if(myScheduleDao == null){
            myScheduleDao = myHelperInstance.getDao(ScheduleBean.class);
        }
        return myScheduleDao;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, ScheduleBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource, ScheduleBean.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        super.close();
        myScheduleDao = null;
    }
}