package com.matianyi.accountingapp.util;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import com.matianyi.accountingapp.bean.CategoryResBean;
import com.matianyi.accountingapp.activity.MainActivity;
import com.matianyi.accountingapp.R;

import java.util.LinkedList;

public class GlobalUtil {

    private static final String TAG = "GlobalUtil";

    private static GlobalUtil instance;

    public RecordDatabaseHelper databaseHelper;
    private Context context;
    public MainActivity mainActivity;

    public LinkedList<CategoryResBean> costRes = new LinkedList<>();
    public LinkedList<CategoryResBean> earnRes = new LinkedList<>();

    public static String[] ExpenditureCategories = {
        "支出", "食物", "飲料", "百貨", "購物", "個人",
        "娛樂", "電影", "社交", "交通", "課金", "電話費",
        "軟體", "禮物", "房租", "旅行", "門票", "書籍", "醫療",
        "轉帳"
    };

    public static String[] IncomeCategories = {
        "收入", "報銷", "薪水", "红包",
        "兼職", "獎金", "投資"
    };

    private static int [] costIconResWhite = {
            R.drawable.icon_general_white,
            R.drawable.icon_food_white,
            R.drawable.icon_drinking_white,
            R.drawable.icon_groceries_white,
            R.drawable.icon_shopping_white,
            R.drawable.icon_personal_white,
            R.drawable.icon_entertain_white,
            R.drawable.icon_movie_white,
            R.drawable.icon_social_white,
            R.drawable.icon_transport_white,
            R.drawable.icon_appstore_white,
            R.drawable.icon_mobile_white,
            R.drawable.icon_computer_white,
            R.drawable.icon_gift_white,
            R.drawable.icon_house_white,
            R.drawable.icon_travel_white,
            R.drawable.icon_ticket_white,
            R.drawable.icon_book_white,
            R.drawable.icon_medical_white,
            R.drawable.icon_transfer_white
    };
    private static int [] costIconResBlack = {
            R.drawable.icon_general,
            R.drawable.icon_food,
            R.drawable.icon_drinking,
            R.drawable.icon_groceries,
            R.drawable.icon_shopping,
            R.drawable.icon_presonal,
            R.drawable.icon_entertain,
            R.drawable.icon_movie,
            R.drawable.icon_social,
            R.drawable.icon_transport,
            R.drawable.icon_appstore,
            R.drawable.icon_mobile,
            R.drawable.icon_computer,
            R.drawable.icon_gift,
            R.drawable.icon_house,
            R.drawable.icon_travel,
            R.drawable.icon_ticket,
            R.drawable.icon_book,
            R.drawable.icon_medical,
            R.drawable.icon_transfer
    };

    private static int[] earnIconResWhite = {
            R.drawable.icon_general_white,
            R.drawable.icon_reimburse_white,
            R.drawable.icon_salary_white,
            R.drawable.icon_redpocket_white,
            R.drawable.icon_parttime_white,
            R.drawable.icon_bonus_white,
            R.drawable.icon_investment_white};

    private static int[] earnIconResBlack = {
            R.drawable.icon_general,
            R.drawable.icon_reimburse,
            R.drawable.icon_salary,
            R.drawable.icon_redpocket,
            R.drawable.icon_parttime,
            R.drawable.icon_bonus,
            R.drawable.icon_investment};


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
        databaseHelper = new RecordDatabaseHelper(context,RecordDatabaseHelper.DB_NAME,null,1);
        addRes();
    }

    public void addRes(){
        for (int i=0;i<ExpenditureCategories.length;i++){
            CategoryResBean res = new CategoryResBean();
            res.title = ExpenditureCategories[i];
            res.resBlack = costIconResBlack[i];
            res.resWhite = costIconResWhite[i];
            costRes.add(res);
        }

        for (int i=0;i<IncomeCategories.length;i++){
            CategoryResBean res = new CategoryResBean();
            res.title = IncomeCategories[i];
            res.resBlack = earnIconResBlack[i];
            res.resWhite = earnIconResWhite[i];
            earnRes.add(res);
        }

        setRes(costRes, earnRes);

        Log.d(TAG, "addRes: costRes & earnRes size: " + costRes.size() + " ," + earnRes.size());
    }

    public static GlobalUtil getInstance(){
        if (instance==null){
            instance = new GlobalUtil();
        }
        return instance;
    }

    private void setRes(LinkedList<CategoryResBean> costRes, LinkedList<CategoryResBean> earnRes){
        this.costRes = costRes;
        this.earnRes = earnRes;
    }

    public int getResourceIcon(String category){
        for (CategoryResBean res :
                costRes) {
            if (res.title.equals(category)){
                return res.resBlack;
            }
        }

        for (CategoryResBean res :
                earnRes) {
            if (res.title.equals(category)){
                return res.resBlack;
            }
        }

        return costRes.get(0).resBlack;
    }


    public void handleTextViewStyle(TextView textView){
        textView.setGravity(Gravity.END|Gravity.CENTER_VERTICAL);
        Log.d(TAG, "handleTextViewStyle: textSize " + textView.getTextSize());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,textView.getTextSize() - 10);
        Log.d(TAG, "handleTextViewStyle: textSize " + textView.getTextSize());

    }


}