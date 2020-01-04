package com.matianyi.accountingapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.easyandroidanimations.library.BounceAnimation;
import com.matianyi.accountingapp.R;
import com.matianyi.accountingapp.adapter.MainViewPagerAdapter;
import com.matianyi.accountingapp.util.BuilderManager;
import com.matianyi.accountingapp.util.DateUtil;
import com.matianyi.accountingapp.util.GlobalUtil;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;


public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    
    private static final String TAG = "MainActivity";

    private MainViewPagerAdapter pagerAdapter;

    private TickerView expenditureAmountText;
    private TickerView incomeAmountText;
    private TextView dateText;

    private int currentPagePosition = 0;

    private BoomMenuButton rightBmb;

    RefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // animation
        new BounceAnimation(findViewById(R.id.activity_main))
                .setBounceDistance(20)
                .setNumOfBounces(6)
                .setDuration(150)
                .animate();

        GlobalUtil.getInstance().setContext(getApplicationContext());

        GlobalUtil.getInstance().mainActivity = this;

        setMyActionBar();

        // 设置ticker view显示钱数
        handleTickerView();

        // 设置日期显示
        dateText = findViewById(R.id.date_text);

        ViewPager viewPager = findViewById(R.id.view_pager);
        pagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(this);

        // 让用户打开时显示最后一天
        viewPager.setCurrentItem(pagerAdapter.getLastIndex());

        try {
            handleFab();
        }catch (Exception e){
            Log.d(TAG, "onCreate: " + e.toString());
        }
        // update header
        updateHeader();

        handleRefreshView();

    }

    // smart refresh
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
            return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        });

    }


    private void handleTickerView(){
        expenditureAmountText = findViewById(R.id.amount_text);
        expenditureAmountText.setCharacterLists(TickerUtils.provideNumberList());
        incomeAmountText = findViewById(R.id.amount_income_text);
        incomeAmountText.setCharacterLists(TickerUtils.provideNumberList());
    }

    private void setMyActionBar(){
        ActionBar mActionBar = getSupportActionBar();
        assert mActionBar != null;
        mActionBar.setElevation(10);
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View actionBar;
        actionBar = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = actionBar.findViewById(R.id.title_text);
        mTitleTextView.setText("今日");
        mActionBar.setCustomView(actionBar);
        mActionBar.setDisplayShowCustomEnabled(true);
        ((Toolbar) actionBar.getParent()).setContentInsetsAbsolute(0,0);

        rightBmb = actionBar.findViewById(R.id.action_bar_right_bmb);

        rightBmb.setButtonEnum(ButtonEnum.Ham);
        rightBmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_2);
        rightBmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_2);
        for (int i = 0; i < rightBmb.getPiecePlaceEnum().pieceNumber(); i++) {
            //自定义添加方法
            addBuilder();

        }

    }

    // 菜单builder
    private void addBuilder(){
        rightBmb.addBuilder(new HamButton.Builder()
            .normalImageRes(BuilderManager.getImageResource())
                .normalTextRes(BuilderManager.getTextResource())
                .listener(index -> {
                    switch (index) {
                        case 0:
                            Log.d(TAG, "onBoomButtonClick: " + 0);
                            Intent intent0 = new Intent(MainActivity.this, StatisticsActivity.class);
                            startActivity(intent0);
                            break;
                        case 1:
                            Log.d(TAG, "onBoomButtonClick: " + 1);
                            Intent intent1 = new Intent(MainActivity.this, AboutPageActivity.class);
                            startActivity(intent1);
                            break;
                    }
                })
                .shadowEffect(true)
                .pieceColor(Color.BLACK)
                .normalColor(Color.parseColor("#ffffff"))
                .normalTextColor(Color.BLACK)
                .textGravity(Gravity.CENTER)
                .textSize(24)
        );
    }

    protected void handleFab(){
        // 为+按钮设置启动activity
        findViewById(R.id.floating_add_button).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
            startActivityForResult(intent, 1);

        });

        // 为+按钮设置长按选项
        findViewById(R.id.floating_add_button).setOnLongClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddCustomRecordActivity.class);
            startActivityForResult(intent, 2);
            return true;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            refreshSelf();
        }

        if (requestCode == 2){
            refreshSelf();
        }
    }


    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        currentPagePosition = i;
        updateHeader();
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    public void updateHeader(){
        String amount = String.valueOf(pagerAdapter.getTotalCost(currentPagePosition));
        String incomeAmount = String.valueOf(pagerAdapter.getTotalIncom(currentPagePosition));
        expenditureAmountText.setText(amount);
        incomeAmountText.setText(incomeAmount);

        String date = pagerAdapter.getDateStr(currentPagePosition);
        dateText.setText(DateUtil.getWeekDay(date));
    }


    // 按下返回键时将应用放在后台
    // 解决fab重复添加的问题
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void handleRefreshView(){
        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            refreshlayout.finishRefresh(500/*,false*/);//传入false表示刷新失败
            refreshSelf();
        });

    }

    public void refreshSelf(){
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();//关闭自己
        overridePendingTransition(0, 0);
    }
}