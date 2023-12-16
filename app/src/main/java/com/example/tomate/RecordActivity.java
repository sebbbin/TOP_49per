package com.example.tomate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

import java.util.ArrayList;



public class RecordActivity extends AppCompatActivity {
    //선 그래프
    private LineChart lineChart;
    ArrayList<Entry> entry_chart1 = new ArrayList<>(); // 데이터를 담을 Arraylist
    ArrayList<String> xVals = new ArrayList<String>(); // 변환할 String 형태 x축 값
    LineDataSet lineDataSet1;
    private TextView textViewPureStudyTime;
    private TextView textViewTotalStudyTime;
    private TextView textViewTomatoGoal;
    private DatabaseReference mDatabase;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_record);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        userId = getIntent().getStringExtra("userId");
        textViewPureStudyTime = findViewById(R.id.textViewPureStudyTime);
        mDatabase.child("RecordData").orderByChild("userId").equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            RecordData recordData = snapshot.getValue(RecordData.class);
                            if (recordData != null) {
                                textViewPureStudyTime.setText(String.valueOf(recordData.getPure_study_time()));
                                break; // 첫 번째 일치하는 데이터만 사용
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
                    }
                });

        textViewTotalStudyTime = findViewById(R.id.textViewTotalStudyTime);
        mDatabase.child("RecordData").orderByChild("userId").equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            RecordData recordData = snapshot.getValue(RecordData.class);
                            if (recordData != null) {
                                textViewTotalStudyTime.setText(String.valueOf(recordData.getTotal_study_time()));
                                break; // 첫 번째 일치하는 데이터만 사용
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
                    }
                });
        textViewTomatoGoal = findViewById(R.id.textViewTomatoGoal);
        if (userId != null) {
            // Firebase Realtime Database 참조 가져오기

            DatabaseReference tomatoRef = FirebaseDatabase.getInstance().getReference("TomatoData").child(userId);

            tomatoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int totalTomatoCount = 0; // 토마토 개수의 총합을 저장할 변수 초기화
                    for (DataSnapshot tomatoSnapshot : dataSnapshot.getChildren()) {
                        // 각 userId의 tomato_cnt 값을 가져와서 더하기
                        String tomatoCount = tomatoSnapshot.child("tomato_cnt").getValue(String.class);
                        if (tomatoCount != null) {
                            int count = Integer.parseInt(tomatoCount);
                            totalTomatoCount += count;
                        }
                    }
                    // 총합을 textViewTomatoGoal에 설정
                    textViewTomatoGoal.setText(String.valueOf(totalTomatoCount));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
                }
            });
        }
        else{
            Log.w("TAG", "userId: null");
        }


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
        Button backbtn = findViewById(R.id.backbtn);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FragmentTransaction 시작
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Fragment fragment = new StudyrecordFragment(userId); // 프래그먼트를 생성합니다.
                transaction.replace(R.id.fragment_container, fragment); // fragment_container에 프래그먼트를 추가합니다.
                transaction.commit();
            }
        });



    }

}
