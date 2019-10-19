package com.example.myapplication.PersonalInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.Slider.PreferenceManager;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class PersonalInfo extends AppCompatActivity implements View.OnClickListener{
    ImageView backbutton, profile;
    Spinner agespinner, careerspinner,strokespinner;
    TextView text_man, text_woman,saveButton;
    EditText name;
    String  Gender,imgPath;
    PreferenceManager preferenceManager;
    final int PICK_FROM_ALBUM = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        initComponents();

    }
    private void initComponents() {
        backbutton = findViewById(R.id.back_button);
        backbutton.setOnClickListener(this);

        profile = findViewById(R.id.profile);
        profile.setOnClickListener(this);



        name = findViewById(R.id.name);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ColorSetting(saveButton);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                ColorSetting(saveButton);
            }
        });

        agespinner = findViewById(R.id.age_spinner);
        String[] ageList ={"-- 연령 --","20대 이하","20대","30대","40대","50대","60대","60대 이상"};
        SpinnerSetting(agespinner,ageList);


        careerspinner = findViewById(R.id.career_spinner);
        String[] careerlist = {"-- 구력 --","1년 이하","1년","2년","3년","4년","5년","6년 이상"};
        SpinnerSetting(careerspinner,careerlist);

        strokespinner = findViewById(R.id.stroke_spinner);
        String[] storkelist ={"-- 타수 --","60대","70대","80대","90대","100대","110대","110대 이상"};
        SpinnerSetting(strokespinner,storkelist);


        text_man = findViewById(R.id.text_man);
        text_man.setOnClickListener(this);

        preferenceManager = new PreferenceManager(getApplicationContext());

        text_woman = findViewById(R.id.text_woman);
        text_woman.setOnClickListener(this);

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_button:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            break;

            case R.id.profile:
//                Intent album = new Intent(Intent.ACTION_PICK);
//                album.setType(MediaStore.Images.Media.CONTENT_TYPE);
//                album.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(album,PICK_FROM_ALBUM);
                break;

            case R.id.text_man:
                ColorSetting(saveButton);
                ColorSettingOn(text_man);
                ColorSettingOff(text_woman);
                Gender = "남성";
            break;
            case R.id.text_woman:
                ColorSetting(saveButton);
                ColorSettingOn(text_woman);
                ColorSettingOff(text_man);
                Gender = "여성";
                break;


            case R.id.saveButton:
                SharedPreferences sharedPreferences = getSharedPreferences("Profile",MODE_PRIVATE);
                String PlayerName = name.getText().toString();
                String PlayerAge = agespinner.getSelectedItem().toString();
                String PlayerCareer = careerspinner.getSelectedItem().toString();
                String PlayerStroke = strokespinner.getSelectedItem().toString();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("PlayerName",PlayerName);
                editor.putString("PlayerAge",PlayerAge);
                editor.putString("PlayerCareer",PlayerCareer);
                editor.putString("PlayerStroke",PlayerStroke);
                editor.putString("Gender",Gender);
                editor.putString("Profile_Image",imgPath);
                editor.commit();

                Intent ToMain = new Intent(this,MainActivity.class);
                startActivity(ToMain);
                preferenceManager.setFirstTimeLaunch(false);
                finish();
        }
    }

    public void ColorSettingOn(TextView textView){
        GradientDrawable bgShape = (GradientDrawable) textView.getBackground();
        bgShape.setColor(getResources().getColor(R.color.select_gender));
        textView.setTextColor(Color.WHITE);
    }

    public void ColorSettingOff(TextView textView){
        GradientDrawable bgShape = (GradientDrawable) textView.getBackground();
        bgShape.setColor(getResources().getColor(R.color.text_color));
        textView.setTextColor(Color.BLACK);
    }

    public void ColorSetting(TextView textView){
        textView.setBackgroundResource(R.color.select_gender);
        textView.setTextColor(Color.WHITE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(requestCode == PICK_FROM_ALBUM){
            if(resultCode == Activity.RESULT_OK){
                try{
                    String name_Str = getImageNameToUri(intent.getData());
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),intent.getData());
                    image_bitmap = ExifUtils.rotateBitmap(imgPath,image_bitmap);
                    profile.setImageBitmap(image_bitmap);
                    ColorSetting(saveButton);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public String getImageNameToUri(Uri data){
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data,proj,null,null,null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);
        return imgName;
    }

    public void SpinnerSetting(Spinner spinner, String[] spinnerlist){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, spinnerlist);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = (String) spinner.getSelectedItem();
                if(text != spinner.getItemAtPosition(0)){
                    ColorSetting(saveButton);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner.setAdapter(adapter);
        spinner.setGravity(Gravity.RIGHT);
    }

}
