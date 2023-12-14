package com.example.tomate;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class RecordActivity extends AppCompatActivity {
    //선 그래프
    private LineChart lineChart;
    ArrayList<Entry> entry_chart1 = new ArrayList<>(); // 데이터를 담을 Arraylist

    ArrayList<String> xVals = new ArrayList<String>(); // 변환할 String 형태 x축 값
    LineDataSet lineDataSet1;
    private TextView textViewPureStudyTime;
    private TextView textViewTotalStudyTime;
    private DatabaseReference mDatabase;
    String userId;
    String date;
    RecordData recordData;


    @Override
    protected void onStart() {
        super.onStart();
        date = getIntent().getStringExtra("date");

        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        userId = sharedPref.getString("userId", "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_record);
        String date = getIntent().getStringExtra("date");
        TextView dateTv = (TextView) findViewById(R.id.record_date_tv);
        dateTv.setText(formatDate(date));
        recordData = (RecordData) getIntent().getSerializableExtra("recordData");
        TextView totalStudyTimeTv = findViewById(R.id.textViewTotalStudyTime);
        totalStudyTimeTv.setText(recordData.getTotal_study_time());
        TextView pureStudyTimeTv = findViewById(R.id.textViewPureStudyTime);
        pureStudyTimeTv.setText(recordData.getPure_study_time());
        TextView tomatoCntTv = findViewById(R.id.record_tomato_cnt_tv);
        tomatoCntTv.setText(recordData.getTomato_cnt());

        lineChart = (LineChart) findViewById(R.id.chart);
        xVals.clear(); // x축 배열 모든 값 제거
        entry_chart1.clear();
        LineData chartData = new LineData(); // 차트에 담길 데이터
        List<Integer> seconds = recordData.getSeconds();

        for (int i = 0; i < seconds.size(); i++) {
            // Y축 값은 seconds 리스트의 값이 됩니다.
            entry_chart1.add(new Entry(i, seconds.get(i)));

            // X축 레이블을 생성합니다.
            String xValue = (i + 1) + "번째";
            Log.d("x값 확인", xValue);
            xVals.add(xValue);
        }

        lineDataSet1 = new LineDataSet(entry_chart1, "LineGraph1"); // 데이터가 담긴 Arraylist 를 LineDataSet 으로 변환한다.
        lineDataSet1.setColor(Color.RED); // 해당 LineDataSet의 색 설정 :: 각 Line 과 관련된 세팅은 여기서 설정한다.


        chartData.addDataSet(lineDataSet1); // 해당 LineDataSet 을 적용될 차트에 들어갈 DataSet 에 넣는다.
        lineChart.setData(chartData); // 차트에 위의 DataSet을 넣는다.


        XAxis xAxis = lineChart.getXAxis(); // XAxis : x축 속성 설정하기 위해서 xAxis 객체 만들어줌
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals)); // String 형태의 x 값이 들어있는 배열을 Formmatter 인자값으로 넣어줌]
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // x축 아래쪽
        xAxis.setTextSize(13f); // x축에 표출되는 텍스트의 크기
        xAxis.setDrawGridLines(false); //x축의 그리드 라인을 없앰
        xAxis.setLabelCount(3); //x축의 데이터를 최대 몇 개 까지 나타낼지에 대한 설정


        lineChart.invalidate(); // 차트 업데이트

        Button backbtn = (Button)findViewById(R.id.backbtn);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private String formatDate(String inputDate) {
        // 날짜 파싱을 위한 형식을 정의합니다.
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");

        // 원하는 결과 형식을 정의합니다.
        SimpleDateFormat targetFormat = new SimpleDateFormat("MM월 dd일");
        String formattedDateString = null;
        try {
            // 문자열을 Date 객체로 변환합니다.
            Date date = originalFormat.parse(inputDate);

            // 변환된 Date 객체를 새로운 형식으로 포맷합니다.
            formattedDateString = targetFormat.format(date);

            System.out.println("변환된 날짜: " + formattedDateString);
        } catch (ParseException e) {
            // ParseException을 처리합니다.
            e.printStackTrace();
        }
        return formattedDateString;
    }
}
