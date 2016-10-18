package kr.hs.emirim.sebin2519.simplediary;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    //view에 대한 멤버 필드 3개
    DatePicker datePic;
    EditText editDiary;
    Button butSave;
    String fileName;//파일이름저장할 변순

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datePic = (DatePicker) findViewById(R.id.date_picker);
        editDiary = (EditText) findViewById(R.id.content);
        butSave = (Button) findViewById(R.id.but_save);

        //현재날짜 구하기
        Calendar calendar = Calendar.getInstance(); //추상클래스
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH)+1; //1월은 0반환
        int nowDate = calendar.get(Calendar.DATE);

        //구한 현재날짜 datePicker에 설정
        datePic.init(nowYear, nowMonth, nowDate, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) { //날짜가 변경될때마다 호출됨
                fileName = year + "_" + monthOfYear + "_" + dayOfMonth + ".txt";//fileName저장, 초기화
                String content = readDiary(fileName);//파일 읽어와서 저장
                editDiary.setText(content);//가져온 content설정
                butSave.setEnabled(true);//버튼 활성화
            }
        });

        butSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                try{
                    FileOutputStream out=openFileOutput(fileName, Context.MODE_WORLD_WRITEABLE);
                    String diaryContents=editDiary.getText().toString();
                    out.write(diaryContents.getBytes());
                    out.close();
                    Toast.makeText(getApplicationContext(),"저장이 완료되었습니다!",Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        );//익명클래스로 구현
    }
    String readDiary(String fileName){
        String diaryContents=null;
        try {
            FileInputStream in=openFileInput(fileName);
            byte[] txt=new byte[500];
            in.read(txt);
            in.close();
            diaryContents=new String(txt);
            butSave.setText("수정하기");
        } catch (IOException e) {//읽오올것이 없으면
            //e.printStackTrace();
            editDiary.setHint("읽어올 일기가 없습니다.");
            butSave.setText("새로 저장");
        }
        return diaryContents;
    }
}
