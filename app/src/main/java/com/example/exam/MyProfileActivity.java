package com.example.exam;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.example.exam.Interface.MyCompleteListener;

public class MyProfileActivity extends AppCompat {
    EditText name, email, phone;
    LinearLayout linearLayout, layout_btn;
    Button btn_save, btn_cancel;
    TextView tv_name;
    Toolbar toolbar;
    String nameStr, emailStr, phoneStr;
    simple_progress simple_progress = new simple_progress(this);



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        toolbar = findViewById(R.id.toolbar3);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.my_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        name = findViewById(R.id.mp_name);
        phone = findViewById(R.id.mp_phone_no);
        email = findViewById(R.id.mp_email);
        linearLayout = findViewById(R.id.edit_button);
        btn_cancel = findViewById(R.id.btn_Cancel);
        btn_save = findViewById(R.id.btn_save);
        tv_name = findViewById(R.id.profile_txt);
        layout_btn = findViewById(R.id.button_layout);


        setDisabled();


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnable();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDisabled();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsValidate()){
                    saveData();
                }
            }
        });

    }

    private void setDisabled() {
        name.setEnabled(false);
        email.setEnabled(false);
        phone.setEnabled(false);

        layout_btn.setVisibility(View.GONE);

        name.setText(DbQuery.myProfile.getName());
        email.setText(DbQuery.myProfile.getEmail());

        if (DbQuery.myProfile.getPhoneNo()!=null){
            phone.setText(DbQuery.myProfile.getPhoneNo());
        }

        tv_name.setText(DbQuery.myProfile.getName().toUpperCase().substring(0,1));
    }
    private void setEnable() {
        name.setEnabled(true);
        //email.setEnabled(false);
        phone.setEnabled(true);
        layout_btn.setVisibility(View.VISIBLE);

    }
    private boolean IsValidate(){

        nameStr = name.getText().toString();
        phoneStr= phone.getText().toString();
        if (nameStr.isEmpty()){
            name.setError(getResources().getString(R.string.errName));
            name.requestFocus();
            return false;
        }

        if (phoneStr.isEmpty() || !phoneStr.startsWith("+93") || phoneStr.length() < 12){
            phone.setError(getResources().getString(R.string.errPhone));
            phone.requestFocus();
            return false;
        }
        return true;
    }
    private void saveData() {
        if (phoneStr.isEmpty()){
            phone=null;
        }
        simple_progress.startLoadDialog(getResources().getString(R.string.update));
        DbQuery.saveMyProfileData(nameStr, phoneStr, new MyCompleteListener() {
            @Override
            public void OnSuccess() {
                Toast.makeText(MyProfileActivity.this,getResources().getString(R.string.updaing), Toast.LENGTH_SHORT).show();
                setDisabled();
                simple_progress.dissmissDialog();
            }

            @Override
            public void OnFailure() {
                Toast.makeText(MyProfileActivity.this,getResources().getString(R.string.errorMsg), Toast.LENGTH_SHORT).show();
                simple_progress.dissmissDialog();
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            MyProfileActivity.this.finish();
        return super.onOptionsItemSelected(item);
    }
}