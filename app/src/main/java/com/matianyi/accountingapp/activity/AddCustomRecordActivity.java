package com.matianyi.accountingapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.matianyi.accountingapp.R;
import com.matianyi.accountingapp.bean.RecordBean;
import com.matianyi.accountingapp.util.DateUtil;
import com.matianyi.accountingapp.util.GlobalUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AddCustomRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialCalendarView calendarView;

    private TextView dateLayout;

    private NiceSpinner niceSpinner;

    private String userInput = "";

    private String selectedItem;

    private TextView amountText;

    private int type;

    List<String> dataSet;

    RecordBean record = new RecordBean();

    String selectedDate;

    private static final String TAG = "AddCustomRecordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_custom_record);

        calendarView = findViewById(R.id.calendarView);

        handleView();
        handleCalendar();
        handleListener();
        handleSpinner();
        handleClear();
        handleBackSpace();
        handleDone();
    }

    private void handleDone() {
        findViewById(R.id.c_keyboard_done).setOnClickListener(v -> {

            if (selectedItem == null) {
                selectedItem = "支出";
                type = 1;
            }

            Log.d(TAG, "onClick: " + userInput + " no selected date:" + selectedDate + " " + selectedItem + " ");

            if (!userInput.equals("") && !userInput.equals("0") && !DateUtil.isSelectedDateAfterToday(selectedDate, DateUtil.getFormattedDate())) {
                double amount = Double.valueOf(userInput);

                record.setAmount(amount);

                record.setDate(selectedDate);

                record.setTimeStamp(System.currentTimeMillis());

                record.setCategory(selectedItem);
                record.setRemark(selectedItem);
                record.setType(type);

                Log.d(TAG, "onClick: record details:"
                        + record.getUuid() + record.getCategory() + record.getAmount());


                Toast.makeText(getApplicationContext(), "完成", Toast.LENGTH_SHORT).show();

                GlobalUtil.getInstance().databaseHelper.addRecord(record);

                finish();

                // Log.d(TAG, "Done! Record: " + record.getUuid() + " " + record.getCategory() + " " + record.getAmount());
            } else if (userInput.equals("") || userInput.equals("0")) {
                Toast.makeText(getApplicationContext(), "錢數不可為0", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onClick: record details:"
                        + record.getUuid() + record.getDate() + record.getAmount());
            } else if (DateUtil.isSelectedDateAfterToday(selectedDate, DateUtil.getFormattedDate())) {
                Toast.makeText(getApplicationContext(), "明日未知", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleBackSpace() {
        findViewById(R.id.c_keyboard_backspace).setOnClickListener(v -> {
            if (userInput.length() > 0) {
                userInput = userInput.substring(0, userInput.length() - 1);
            }

            // 恢复大小
            if (userInput.equals("")) {
                amountText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);
            }
            updateAmountText();
        });
    }

    private void handleClear() {
        findViewById(R.id.c_keyboard_clear).setOnClickListener(v -> {
            userInput = "";
            updateAmountText();
            amountText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);
        });
    }

    // 更新数字面板
    private void updateAmountText() {
        Log.d(TAG, "UserInput is : " + userInput);


        if (userInput.equals("")) {
            amountText.setText("0");
        } else {
            amountText.setText(userInput);
        }

    }


    private void handleView() {
        amountText = findViewById(R.id.custom_add_amount_text);
        dateLayout = findViewById(R.id.selected_date_layout);
        // 点击该视图时回到今天
        dateLayout.setOnClickListener(v -> {
            dateLayout.setText(DateUtil.getFormattedDate());
            calendarView.setSelectedDate(CalendarDay.today());
            selectedDate = DateUtil.getFormattedDate();
        });
    }

    private void handleListener() {
        findViewById(R.id.c_keyboard_one).setOnClickListener(this);
        findViewById(R.id.c_keyboard_two).setOnClickListener(this);
        findViewById(R.id.c_keyboard_three).setOnClickListener(this);
        findViewById(R.id.c_keyboard_four).setOnClickListener(this);
        findViewById(R.id.c_keyboard_five).setOnClickListener(this);
        findViewById(R.id.c_keyboard_six).setOnClickListener(this);
        findViewById(R.id.c_keyboard_seven).setOnClickListener(this);
        findViewById(R.id.c_keyboard_eight).setOnClickListener(this);
        findViewById(R.id.c_keyboard_nine).setOnClickListener(this);
        findViewById(R.id.c_keyboard_zero).setOnClickListener(this);
    }

    private void handleCalendar() {
        calendarView.setSelectedDate(CalendarDay.today());

        selectedDate = DateUtil.getFormattedDate();

        dateLayout.setText(selectedDate);

        Log.d(TAG, "handleCalendar: selectedDate init " + selectedDate);


        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            selectedDate = date.getDate().toString();
            dateLayout.setText(selectedDate);
            Log.d(TAG, "onDateSelected: " + selectedDate);
        });
    }

    private void handleSpinner() {
        niceSpinner = findViewById(R.id.custom_add_category_spinner);

        dataSet = new LinkedList<>(Arrays.asList(GlobalUtil.ExpenditureCategories));

        dataSet.addAll(new LinkedList<>(Arrays.asList(GlobalUtil.IncomeCategories)));

        niceSpinner.attachDataSource(dataSet);

        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                niceSpinner.getSelectedItem();
                selectedItem = niceSpinner.getSelectedItem().toString();

                if (Arrays.asList(GlobalUtil.ExpenditureCategories).contains(selectedItem)) {
                    type = 1;
                } else {
                    type = 2;
                }

                Log.d(TAG, "onItemSelected: selectedItem: " + selectedItem + " type:" + type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                niceSpinner.setSelectedIndex(0);
                selectedItem = niceSpinner.getSelectedItem().toString();

                if (Arrays.asList(GlobalUtil.ExpenditureCategories).contains(selectedItem)) {
                    type = 1;
                } else {
                    type = 2;
                }

                Log.d(TAG, "onItemSelected: no item selected: " + selectedItem + " type:" + type);
            }
        });

        Log.d(TAG, "handleSpinner: " + dataSet);
    }


    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String input = button.getText().toString();

        // 处理过大数据的输入问题

        int integerLength = userInput.length();
        Log.d(TAG, "onClick: integer: " + integerLength);
        if (integerLength >= 3 && integerLength <= 5) {
            GlobalUtil.getInstance().handleTextViewStyle(amountText);
        } else if (integerLength > 5 && integerLength <= 7) {
            GlobalUtil.getInstance().handleTextViewStyle(amountText);
        } else if (integerLength > 7) {
            Toast.makeText(getApplicationContext(), "你哪來的那麼多錢？", Toast.LENGTH_SHORT).show();
            userInput = amountText.getText().toString();
        }

        userInput += input;
        updateAmountText();

    }
}
