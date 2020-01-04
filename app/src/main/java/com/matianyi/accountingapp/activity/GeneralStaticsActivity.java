package com.matianyi.accountingapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.matianyi.accountingapp.R;
import com.matianyi.accountingapp.adapter.GeneralStatisticListViewAdapter;
import com.matianyi.accountingapp.util.DateUtil;
import com.matianyi.accountingapp.util.GlobalUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneralStaticsActivity extends AppCompatActivity {

    private int totalMonths;
    private String mostExpenditureMonth;
    private String mostIncomeMonth;
    private double mostExpenditureMonthAmount;
    private double mostIncomeMonthAmount;
    private String mostExpenditureCategory;
    private String mostIncomeCategory;
    private double mostExpenditureCategoryAmount;
    private double mostIncomeCategoryAmount;


    private static final String TAG = "GeneralStatisticActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_statics);

        setTotalMonths();

        if (getTotalMonths() == 0){
            Toast.makeText(getApplicationContext(), "沒有相關記錄可以生成統計訊息！", Toast.LENGTH_SHORT).show();
            finish();
        }

        setMostExpenditureMonth();

        setMostExpenditureMonthAmount();

        setMostIncomeMonth();

        setMostIncomeMonthAmount();

        setMostExpenditureCategory();

        setMostExpenditureCategoryAmount();

        setMostIncomeCategory();

        setMostIncomeCategoryAmount();

        ListView listView = findViewById(R.id.general_list_view);
        List<String> statements = new ArrayList<>();
        statements.add("TianPurse已成為你過去"+ getTotalMonths() + "個月的記帳助手.");
        statements.add("在" + DateUtil.convertSqlMonthToCharacter(getMostExpenditureMonth())
                    + ", 你花掉了最多的錢,達到了NT$" + getMostExpenditureMonthAmount() + "元.");
        statements.add("在" + DateUtil.convertSqlMonthToCharacter(getMostIncomeMonth())
                    + ", 你獲得了最多的收入,達到了NT$" + getMostIncomeMonthAmount() + "元.");
        statements.add("總體來說， 你在" + getMostExpenditureCategory() + "上花的錢最多，共計NT$"
                    + getMostExpenditureCategoryAmount() + "元,");
        statements.add("而在" + getMostIncomeCategory() + "方面獲得了最多的收入，共計NT$"
                    + getMostIncomeCategoryAmount() + "元.");
        Log.d(TAG, "onCreate: statements: " + statements);

        GeneralStatisticListViewAdapter listViewAdapter = new GeneralStatisticListViewAdapter(statements, GeneralStaticsActivity.this);
        listView.setAdapter(listViewAdapter);
    }


    public int getTotalMonths() {
        return totalMonths;
    }

    public void setTotalMonths() {
        this.totalMonths = GlobalUtil.getInstance().databaseHelper.getAvailableMonths().size();
    }

    public String getMostExpenditureMonth() {
        return mostExpenditureMonth;
    }

    public void setMostExpenditureMonth() {
        Map<String, Double> monthAndAmount = new HashMap<>();
        for (String month:GlobalUtil.getInstance().databaseHelper.getAvailableMonths()){
            monthAndAmount.put(month, GlobalUtil.getInstance().databaseHelper.getExpenditureThisMonth(month));
        }

        Log.d(TAG, "setMostExpenditureMonth: month and amount map: " + monthAndAmount);

        this.mostExpenditureMonth = monthAndAmount.entrySet()
                .stream()
                .max((Comparator.comparingDouble(Map.Entry::getValue)))
                .map(Map.Entry::getKey)
                .orElse("暫無記錄");
    }

    public String getMostIncomeMonth() {
        return mostIncomeMonth;
    }

    public void setMostIncomeMonth() {
        Map<String, Double> monthAndAmount = new HashMap<>();
        for (String month:GlobalUtil.getInstance().databaseHelper.getAvailableMonths()){
            monthAndAmount.put(month, GlobalUtil.getInstance().databaseHelper.getIncomeThisMonth(month));
        }

        Log.d(TAG, "setMostIncomeMonth: month and amount map: " + monthAndAmount);

        this.mostIncomeMonth = monthAndAmount.entrySet()
                .stream()
                .max((Comparator.comparingDouble(Map.Entry::getValue)))
                .map(Map.Entry::getKey)
                .orElse("暫無記錄");
    }

    public double getMostExpenditureMonthAmount() {
        return mostExpenditureMonthAmount;
    }

    public void setMostExpenditureMonthAmount() {
        this.mostExpenditureMonthAmount = GlobalUtil.getInstance().databaseHelper.getExpenditureThisMonth(this.mostExpenditureMonth);
    }

    public double getMostIncomeMonthAmount() {
        return mostIncomeMonthAmount;
    }

    public void setMostIncomeMonthAmount() {
        this.mostIncomeMonthAmount = GlobalUtil.getInstance().databaseHelper.getIncomeThisMonth(this.mostIncomeMonth);
    }

    public String getMostExpenditureCategory() {
        return mostExpenditureCategory;
    }

    public void setMostExpenditureCategory() {
        Map<String, Double> categoryAndAmount;
        categoryAndAmount = GlobalUtil.getInstance().databaseHelper.getMostExpenditureCategory();

        this.mostExpenditureCategory = categoryAndAmount.entrySet()
                .stream()
                .max((Comparator.comparingDouble(Map.Entry::getValue)))
                .map(Map.Entry::getKey)
                .orElse("暫無記錄");
    }

    public String getMostIncomeCategory() {
        return mostIncomeCategory;
    }

    public void setMostIncomeCategory() {
        Map<String, Double> categoryAndAmount = GlobalUtil.getInstance().databaseHelper.getMostIncomeCategory();

        this.mostIncomeCategory = categoryAndAmount.entrySet()
                .stream()
                .max((Comparator.comparingDouble(Map.Entry::getValue)))
                .map(Map.Entry::getKey)
                .orElse("暫無記錄");
    }

    public double getMostExpenditureCategoryAmount() {
        return mostExpenditureCategoryAmount;
    }

    public void setMostExpenditureCategoryAmount() {
        for (String month:GlobalUtil.getInstance().databaseHelper.getAvailableMonths()){
            this.mostExpenditureCategoryAmount
                    += GlobalUtil.getInstance().databaseHelper
                        .getExpenditureCategoryTotalAmountThisMonth(month, this.mostExpenditureCategory);
        }
    }

    public double getMostIncomeCategoryAmount() {
        return mostIncomeCategoryAmount;
    }

    public void setMostIncomeCategoryAmount() {
        for (String month : GlobalUtil.getInstance().databaseHelper.getAvailableMonths()){
            this.mostIncomeCategoryAmount
                    += GlobalUtil.getInstance().databaseHelper
                        .getIncomeCategoryTotalAmountThisMonth(month, this.mostIncomeCategory);
        }
    }
}
