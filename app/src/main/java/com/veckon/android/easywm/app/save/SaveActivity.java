package com.veckon.android.easywm.app.save;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.veckon.android.easywm.app.R;
import com.veckon.android.easywm.app.main.MainActivity;


public class SaveActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_save);

    }

    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            //저장 버튼
            case R.id.btn_save_local:
                Log.d("veckon_md", "onClick btn_save_local");
                doSaveLocal();
                break;
            //페이스북 연동
            case R.id.btn_share_fb:
                Log.d("veckon_md", "onClick btn_share_fb");
                break;
            //카카오스토리 연동
            case R.id.btn_share_ks:
                Log.d("veckon_md", "onClick btn_share_ks");
                break;
            //인스타그럄 연동
            case R.id.btn_share_ig:
                Log.d("veckon_md", "onClick btn_share_ig");
                break;
        }
    }

    public void doSaveLocal(){
        //로컬에 저장

        //첫화면으로 이동
        goFirstPage();
    }

    public void goFirstPage(){
        //첫 화면으로 이동
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.make, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
