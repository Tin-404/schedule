package com.neu.edu.schedule.dao;

import android.content.Context;
import android.os.Parcelable;

import com.neu.edu.schedule.RecordActivity;
import com.neu.edu.schedule.bean.ScheduleBean;
import com.neu.edu.schedule.db_helper.MyHelper;
import com.j256.ormlite.dao.Dao;
import com.neu.edu.schedule.recordFragments.RecordFragment;

import java.sql.SQLException;
import java.util.List;

/**
 *
 */
public class ScheduleDao {
    private MyHelper myHelper;
    private Dao<ScheduleBean,Integer> dao;

    public ScheduleDao(Context context){
        myHelper = MyHelper.getInstance(context);
        try {
            dao = myHelper.getMyScheduleDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(ScheduleBean scheduleBean) throws SQLException {
        dao.createOrUpdate(scheduleBean);
    }

    public void update(ScheduleBean scheduleBean) throws SQLException {
        dao.update(scheduleBean);
    }

    public void delete(ScheduleBean scheduleBean) throws SQLException {
        dao.delete(scheduleBean);
    }

    public List<ScheduleBean> getAll() throws SQLException {
        return dao.queryForAll();
    }
}
