package com.neu.edu.schedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.j256.ormlite.stmt.query.In;
import com.neu.edu.schedule.courseFragments.CourseFragment;
import com.neu.edu.schedule.noteFragments.NoteFragment;
import com.neu.edu.schedule.recordFragments.RecordFragment;
import com.neu.edu.schedule.userFragments.UserFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    // 声明变量
    private ViewPager mViewPager;
    // ViewPager需要适配器
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragment;
//    private TabHost tabHost;

    private LinearLayout mll_notice;
    private LinearLayout mll_course;
    private LinearLayout mll_user;
    private LinearLayout mll_record;
    private ImageButton mll_noticeImg;
    private ImageButton mll_courseImg;
    private ImageButton mll_userImg;
    private ImageButton mll_recordImg;
    private TextView mll_noticetv;
    private TextView mll_coursetv;
    private TextView mll_usertv;
    private TextView mll_recordtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main2);
//        tabHost = getTabHost();

        // 设置登录状态为true，这样如果不退出账号，那么再次登录时直接进入此页面
        changeLoginState(true);

        initView();
        initEvents();
        setSelect(1);
    }

    private void initView() {
        mViewPager = findViewById(R.id.viewPager);

        mll_notice =  findViewById(R.id.ll_notice);
        mll_course =  findViewById(R.id.ll_course);
        mll_user =  findViewById(R.id.ll_user);
        mll_record = findViewById(R.id.ll_record);

        mll_noticeImg =  findViewById(R.id.ll_notice_img);
        mll_courseImg = findViewById(R.id.ll_course_img);
        mll_userImg =  findViewById(R.id.ll_user_img);
        mll_recordImg = findViewById(R.id.ll_record_img);

        mll_noticetv =  findViewById(R.id.ll_notice_tv);
        mll_coursetv =  findViewById(R.id.ll_course_tv);
        mll_usertv =  findViewById(R.id.ll_user_tv);
        mll_recordtv = findViewById(R.id.ll_record_tv);


        mFragment = new ArrayList<Fragment>();
        Fragment mtabnotice = new NoteFragment();
        Fragment mtabcourse = new CourseFragment();
        Fragment mtabuser = new UserFragment();
        Fragment mtabrecord = new RecordFragment();
        mFragment.add(mtabnotice);
        mFragment.add(mtabrecord);
        mFragment.add(mtabcourse);
        mFragment.add(mtabuser);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return mFragment.size();// 有多少数据
            }

            @Override
            public Fragment getItem(int arg0) {
                // TODO Auto-generated method stub
                return mFragment.get(arg0);// 返回不同的fragment
            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int arg0) {
                int currentItem = mViewPager.getCurrentItem();
                setTab(currentItem);
            }

//            @Override
//            public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private void initEvents() {
        // 增加点击效果
        mll_notice.setOnClickListener(this);
        mll_course.setOnClickListener(this);
        mll_user.setOnClickListener(this);
        mll_record.setOnClickListener(this);

    }

    private void setSelect(int i) {
        // 设置图片为亮色
        // 切换内容区域
        setTab(i);
        mViewPager.setCurrentItem(i);
    }

    private void setTab(int i) {
        resetImg();
        switch (i) {
            case 0:
                mll_noticeImg.setImageResource(R.drawable.ic_notice_light);
                mll_noticetv.setTextColor(0xff1afa29);
                break;
            case 1:
                mll_recordImg.setImageResource(R.drawable.ic_record_light);
                mll_recordtv.setTextColor(0xff1afa29);
                break;
            case 2:
                mll_courseImg.setImageResource(R.drawable.ic_course_light);
                mll_coursetv.setTextColor(0xff1afa29);
                break;
            case 3:
                mll_userImg.setImageResource(R.drawable.ic_user_light);
                mll_usertv.setTextColor(0xff1afa29);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        // 创建方法，把所有图片先变暗
        resetImg();
        switch (v.getId()) {
            case R.id.ll_notice:
                setSelect(0);
                break;
            case R.id.ll_record:
                setSelect(1);
//                Intent intent = new Intent(MainActivity.this,RecordActivity.class);
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
                break;
            case R.id.ll_course:
                setSelect(2);
                break;
            case R.id.ll_user:
                setSelect(3);
                break;
            default:
                break;
        }

    }

    private void resetImg() {
        mll_noticeImg.setImageResource(R.drawable.ic_notice);
        mll_noticetv.setTextColor(0xff000000);
        mll_recordImg.setImageResource(R.drawable.ic_record);
        mll_recordtv.setTextColor(0xff000000);
        mll_courseImg.setImageResource(R.drawable.ic_course);
        mll_coursetv.setTextColor(0xff000000);
        mll_userImg.setImageResource(R.drawable.ic_user);
        mll_usertv.setTextColor(0xff000000);

    }

    // 用于设置登录的状态
    // 若登录了，则将登录状态设置为true，若退出登录则设置为false
    public void changeLoginState(Boolean b) {
        SharedPreferences.Editor editor = getSharedPreferences("data",
                MODE_PRIVATE).edit();
        editor.putBoolean("LoginState", b);
        editor.apply();
    }
}