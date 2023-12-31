package com.neu.edu.schedule;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.neu.edu.schedule.adapter.GridAdapter;
import com.neu.edu.schedule.bean.ScheduleBean;
import com.neu.edu.schedule.dao.ScheduleDao;
import com.neu.edu.schedule.recordFragments.RecordFragment;
import com.neu.edu.schedule.service.PushService;
import com.neu.edu.schedule.thread.UiRefresh;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import static java.lang.Thread.sleep;

public class RecordActivity extends AppCompatActivity {

    private static final String TAG = "zhujin";

    private List<ScheduleBean> scheduleBeanList = new ArrayList<>();
    private GridView gridView;
    GridAdapter adapter;
    private Button btn_add;

    private Date datePicked;
    private int dayGet;
    private int monthGet;
    private int yearGet;
    private int hourGet;
    private int minuteGet;

    private ScheduleDao scheduleDao;

    private UiRefresh uiRefresh = UiRefresh.getInstance(initThread());

    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    private Boolean needAutoFill = false;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scheduleDao = new ScheduleDao(RecordActivity.this);
        try {
            scheduleBeanList.clear();
            scheduleBeanList.addAll(scheduleDao.getAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        viewInit();
        adapter = new GridAdapter(RecordActivity.this, scheduleBeanList, callback);
        gridView.setAdapter(adapter);

        Intent intent = new Intent(RecordActivity.this, PushService.class);
        startService(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (uiRefresh.getState() == Thread.State.NEW) {
            uiRefresh.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uiRefresh.interrupt();
        Log.i(TAG, "onDestroy: the state of uiRefresh is " + uiRefresh.getState());
    }

    private Runnable initThread() {
        Runnable runnable = () -> {
            while (true) {
                try {
                    sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(() -> adapter.notifyDataSetChanged());
            }
        };
        return runnable;
    }

    private void viewInit() {
        gridView = findViewById(R.id.gv_main);
        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener((v) -> {
            {
                final View view = LayoutInflater.from(RecordActivity.this).inflate(R.layout.dialog_add_view, null, false);
                Dialog dialog = makeDialog(view);
                dialog.setOnCancelListener(dialog1 -> needAutoFill = false);
                final EditText edt_title = view.findViewById(R.id.edt_title);
                final EditText edt_desc = view.findViewById(R.id.edt_desc);
                final Button btn_pickTime = view.findViewById(R.id.btn_pickTime);
                final Button btn_confirm = view.findViewById(R.id.btn_confirm);
                TextView title_add = view.findViewById(R.id.title_addDialog);

                if (needAutoFill) {
                    edt_title.setText(bundle.getString("title"));
                    edt_desc.setText(bundle.getString("content"));
                    title_add.setText("编辑事项");
                    btn_pickTime.setText("修改日期");
                }


                View.OnClickListener listener = v2 -> {
                    final int mYear, mMonth, mDay, mHourOfday, mMinute;

                    if (needAutoFill) {
                        mYear = Integer.parseInt(Objects.requireNonNull(bundle.getString("year")));
                        mMonth = Integer.parseInt(Objects.requireNonNull(bundle.getString("month")));
                        mDay = Integer.parseInt(Objects.requireNonNull(bundle.getString("day")));
                        mHourOfday = Integer.parseInt(Objects.requireNonNull(bundle.getString("hour")));
                        mMinute = Integer.parseInt(Objects.requireNonNull(bundle.getString("minute")));
                    } else {
                        Calendar ca = Calendar.getInstance();
                        mYear = ca.get(Calendar.YEAR);
                        mMonth = ca.get(Calendar.MONTH);
                        mDay = ca.get(Calendar.DAY_OF_MONTH);
                        mHourOfday = ca.get(Calendar.HOUR_OF_DAY);
                        mMinute = ca.get(Calendar.MINUTE);
                    }
                    switch (v2.getId()) {
                        case R.id.btn_pickTime:
                            //收起软键盘
                            InputMethodManager manager = ((InputMethodManager) RecordActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE));
                            if (manager != null)
                                manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


                            final DatePickerDialog datePickerDialog = new DatePickerDialog(RecordActivity.this,
                                    android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK,
                                    (view1, year, month, dayOfMonth) -> {
                                        yearGet = year;
                                        monthGet = month + 1;
                                        dayGet = dayOfMonth;

                                        final TimePickerDialog timePickerDialog = new TimePickerDialog(RecordActivity.this,
                                                TimePickerDialog.THEME_DEVICE_DEFAULT_DARK,
                                                (view11, hourOfDay, minute) -> {
                                                    hourGet = hourOfDay;
                                                    minuteGet = minute;
                                                    try {
                                                        datePicked = format.parse(yearGet + "/" + monthGet + "/" + dayGet + " " + hourGet + ":" + minuteGet);
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                }, mHourOfday, mMinute, true);
                                        timePickerDialog.setTitle("请选择具体时间");
                                        timePickerDialog.show();
                                    }, mYear, mMonth, mDay);
                            datePickerDialog.setTitle("请选择日期");
                            datePickerDialog.show();
                            break;
                        case R.id.btn_confirm:
                            String edt_get_title = edt_title.getText().toString();
                            String edt_get_desc = edt_desc.getText().toString();
                            if (edt_get_title.equals("")) {
                                Toast.makeText(RecordActivity.this, "标题不能为空", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            if (datePicked == null) {
                                if (needAutoFill) {
                                    try {
                                        datePicked = format.parse(mYear + "/" + mMonth + "/" + mDay + " " + mHourOfday + ":" + mMinute);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(RecordActivity.this, "必须设置提醒时间", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                            ScheduleBean scheduleBean = new ScheduleBean(edt_get_title, edt_get_desc, datePicked);
                            if (needAutoFill) {
                                int id = bundle.getInt("id");
                                scheduleBean.setId(id);
                                String fromTimeStr = bundle.getString("fromTimeStr");
                                try {
                                    Date fromTime = format.parse(fromTimeStr);
                                    scheduleBean.setFromTime(fromTime);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            try {
                                scheduleDao.add(scheduleBean);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            scheduleBeanList.clear();
                            try {
                                scheduleBeanList.addAll(scheduleDao.getAll());
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();

                            String str;
                            if (needAutoFill) {
                                needAutoFill = false;
                                str = "更新事项成功";
                            } else {
                                str = "新建事项成功";
                            }
                            Toast.makeText(RecordActivity.this, str, Toast.LENGTH_SHORT).show();
                            datePicked = null;
                            break;
                    }
                };
                btn_pickTime.setOnClickListener(listener);
                btn_confirm.setOnClickListener(listener);
            }
        });
    }

    private GridAdapter.Callback callback = new GridAdapter.Callback() {
        @Override
        public void onLongClick(final int position) {
            ScheduleBean bean = scheduleBeanList.get(position);

            View view = LayoutInflater.from(RecordActivity.this).inflate(R.layout.item_popmenu, null, false);
            Dialog dialog = makeDialog(view);
            TextView tv_edt = view.findViewById(R.id.tv_edit);
            TextView tv_delete = view.findViewById(R.id.tv_delete);


            tv_edt.setOnClickListener((v) -> {
                dialog.dismiss();
                bundle = new Bundle();
                needAutoFill = true;

                Date deadline = bean.getDeadline();
                Date fromTime = bean.getFromTime();
                String fromTimeStr = format.format(fromTime);
                String deadlineStr = format.format(deadline);
                String year = deadlineStr.substring(0, 4);
                String month = deadlineStr.substring(5, 7);
                String day = deadlineStr.substring(8, 10);
                String hour = deadlineStr.substring(11, 13);
                String minute = deadlineStr.substring(14, 16);

                bundle.putString("title", bean.getTitle());
                bundle.putString("content", bean.getDesc());
                bundle.putString("year", year);
                bundle.putString("month", month);
                bundle.putString("day", day);
                bundle.putString("hour", hour);
                bundle.putString("minute", minute);
                bundle.putInt("id", bean.getId());
                bundle.putString("fromTimeStr", fromTimeStr);
                btn_add.callOnClick();
            });

            tv_delete.setOnClickListener((v) -> {
                dialog.dismiss();

                View view1 = LayoutInflater.from(RecordActivity.this).inflate(R.layout.delete_dialog, null, false);
                Dialog dialog_delete = makeDialog(view1);

                TextView tv_cancel = view1.findViewById(R.id.tv_cancel);
                TextView tv_confirm = view1.findViewById(R.id.tv_confirm);

                tv_cancel.setOnClickListener((q) -> {
                    dialog_delete.dismiss();
                });

                tv_confirm.setOnClickListener((w) -> {
                    dialog_delete.dismiss();
                    try {
                        scheduleDao.delete(bean);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    scheduleBeanList.remove(bean);
                    adapter.notifyDataSetChanged();
                });
            });
        }

        @Override
        public void onClick(Bundle bundle) {

            final View view = LayoutInflater.from(RecordActivity.this).inflate(R.layout.dialog_detail, null, false);
            final TextView tv_title = view.findViewById(R.id.tv_title);
            final TextView tv_desc = view.findViewById(R.id.tv_desc);

            final TextView tv_fromTime = view.findViewById(R.id.tv_fromTime);
            final TextView tv_tillTime = view.findViewById(R.id.tv_tillTime);

            tv_title.setText(bundle.getString("title"));
            tv_desc.setText(bundle.getString("content"));
            tv_fromTime.setText(bundle.getString("fromTime"));
            tv_tillTime.setText(bundle.getString("deadline"));

            makeDialog(view);
        }
    };

    private Dialog makeDialog(View view) {
        Dialog dialog = new Dialog(RecordActivity.this, R.style.dialog);
        Window window = dialog.getWindow();
        assert window != null;
        window.setContentView(view);
        dialog.show();
        return dialog;
    }

    private void startAlarm(ViewGroup parent, int hour, int minute) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());//获取当前时间
            //获取当前毫秒值
            long systemTime = System.currentTimeMillis();
            c.setTimeZone(TimeZone.getTimeZone("GMT+8"));//设置时区
            c.set(Calendar.HOUR_OF_DAY, hour);//设置几点提醒
            c.set(Calendar.MINUTE, minute);//设置几分提醒
            //获取上面设置的时间
            long selectTime = c.getTimeInMillis();
            // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
            if (systemTime > selectTime) {
                c.add(Calendar.DAY_OF_MONTH, 1);
            }
            /* 闹钟时间到了的一个提醒类 */
            Intent intent = new Intent(parent.getContext(), RecordActivity.class);
            @SuppressLint("UnspecifiedImmutableFlag")
            PendingIntent pi = PendingIntent.getActivity(parent.getContext(), 0, intent, 0);
            //得到AlarmManager实例
            AlarmManager am = (AlarmManager)parent.getContext().getSystemService(ALARM_SERVICE);
            //重复提醒
            am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 60*60*1000*24, pi);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}