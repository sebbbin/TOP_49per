package com.example.tomate;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;


public class RecordActivity extends AppCompatActivity {
    //선 그래프
    private LineChart lineChart;
    ArrayList<Entry> entry_chart1 = new ArrayList<>(); // 데이터를 담을 Arraylist
    ArrayList<String> xVals = new ArrayList<String>(); // 변환할 String 형태 x축 값
    LineDataSet lineDataSet1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_record);


        lineChart = (LineChart) findViewById(R.id.chart);
        xVals.clear(); // x축 배열 모든 값 제거
        entry_chart1.clear();
        LineData chartData = new LineData(); // 차트에 담길 데이터

        entry_chart1.add(new Entry(0, 1)); //entry_chart1에 좌표 데이터를 담는다.
        entry_chart1.add(new Entry(1, 2));
        entry_chart1.add(new Entry(2, 3));
        entry_chart1.add(new Entry(3, 4));
        entry_chart1.add(new Entry(4, 2));


        for (int i = 0; i < 5; i++) {
            String xValue = i + 1 + "번째";
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
                Toast.makeText(getApplicationContext(), "뒤로가기 버튼 클릭", Toast.LENGTH_LONG).show();
            }
        });

    }

}
