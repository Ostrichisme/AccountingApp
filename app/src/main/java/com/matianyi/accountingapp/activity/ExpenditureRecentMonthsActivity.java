package com.matianyi.accountingapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.matianyi.accountingapp.R;
import com.matianyi.accountingapp.util.DateUtil;
import com.matianyi.accountingapp.util.GlobalUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExpenditureRecentMonthsActivity extends AppCompatActivity {

    // 进行统计的每月消费，用作y轴

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_common);

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();

        // 获取可用月份
        // 进行统计的月份，用作x轴
        ArrayList<String> availableMonths = GlobalUtil.getInstance().databaseHelper.getAvailableMonths();
        // 将月份排序
        Collections.sort(availableMonths);
        // 给坐标点赋值
        for (int i = 0; i < availableMonths.size(); i ++) {
            data.add(new ValueDataEntry(DateUtil.convertSqlMonthToCharacter(availableMonths.get(i)),
                    GlobalUtil.getInstance().databaseHelper.
                            getExpenditureThisMonth(availableMonths.get(i))));
        }

        if (data.isEmpty()){
            Toast.makeText(getApplicationContext(), "請先做相關記錄再來查看統計！",Toast.LENGTH_SHORT).show();
            finish();
        }

        Column column = cartesian.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("${%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("最近月份支出統計");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.FLOAT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("月份");
        cartesian.yAxis(0).title("支出金額");

        anyChartView.setChart(cartesian);
    }
}
