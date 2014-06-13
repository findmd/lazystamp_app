package com.veckon.android.easywm.app.main;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ViewSwitcher;

import com.veckon.android.easywm.app.R;
import com.veckon.android.easywm.app.gallery.Action;
import com.veckon.android.easywm.app.gallery.CustomGallery;
import com.veckon.android.easywm.app.gallery.GalleryAdapter;
import com.veckon.android.easywm.app.make.MakeActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends Activity {

    private PopupWindow mPopupWindow;
    private Button btnPopup;

    ViewSwitcher viewSwitcher;
    GalleryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

    }

/*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

*/

    public void onClick(View view) {
        //갤러리 불러오기
        if (view.getId() == R.id.btn_load_image) {
            Log.d("veckon_md", "Start btn_load_image popup");

            new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.popup_title))
                .setItems(R.array.image_type,
                //.setItems(new String[] {"@string/popup_load_image_single", "@string/popup_load_image_multi"},
                    new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which) {
                            String[] imageType = getResources().getStringArray(R.array.image_type);
                            //사진 불러오기
                            if(imageType[which].equals(getResources().getString(R.string.select_single))){
                                loadImage();
                            //사진 다중선택
                            } else if(imageType[which].equals(getResources().getString(R.string.select_multi))){
                                loadMultiImage();
                            }
                        }
                    })
                .setNegativeButton(getResources().getString(R.string.cancel), null)
                .show();
        //카메라 불러오기
        } else if(view.getId() == R.id.btn_load_camera) {

            Log.d("veckon_md", "Load camera function");
            loadCamera();
        } else if(view.getId() == R.id.btn_load_multi_img) {

            Log.d("veckon_md", "Load camera function");
            loadMultiImage();
        } else  if(view.getId() == R.id.btn_load_single_img) {

            Log.d("veckon_md", "Load camera function");
            loadImage();
        }
    }

    private static int RESULT_LOAD_CAMERA = 0;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_LOAD_MULTI_IMAGE = 2;
    Uri mImageCaptureUri = null;
    Uri imgUri;

    public void loadImage(){
        Log.d("veckon_md","Start function loadImage()");

        //load gallery
        Intent intentS = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intentS, RESULT_LOAD_IMAGE);

        Log.d("veckon_md","End function loadImage()");
    }

    public void loadMultiImage(){

        Log.d("veckon_md", "start function loadMultiImage()");
        Intent intentM = new Intent(Action.ACTION_MULTIPLE_PICK);
        startActivityForResult(intentM, RESULT_LOAD_MULTI_IMAGE);

        Log.d("veckon_md","end function loadMultiImage()");
    }

    public void loadCamera(){
        Log.d("veckon_md", "start function loadCamera()");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String url = "origin_"+ String.valueOf(System.currentTimeMillis())+".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent, RESULT_LOAD_CAMERA);
        Log.d("veckon_md", "end function loadCamera()");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("veckon_md","Start onActivityResult()");
        if(resultCode == RESULT_OK && null != data) {
            //사진 한장 불러오기
            if (requestCode == RESULT_LOAD_IMAGE) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturesPath = cursor.getString(columnIndex);
                cursor.close();
                if (picturesPath != null) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, MakeActivity.class);
                    intent.putExtra("picturePath", picturesPath);
                    startActivity(intent);
                }

            //사진 다중 불러오기
            } else if (requestCode == RESULT_LOAD_MULTI_IMAGE) {
                Log.d("veckon_md", "function for multi");
                String[] all_path = data.getStringArrayExtra("all_path");

/*
                ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();

                for (String string : all_path) {

                    CustomGallery item = CustomGallery.CREATOR.createFromParcel(new Parcel());
                    //item.sdcardPath = string;
                    item.setString(string);

                    dataT.add(item);

                }
                //intentM.putExtra("multiPicPath", dataT);

                //viewSwitcher.setDisplayedChild(0);
                //adapter.addAll(dataT);

*/
                Intent intentM = new Intent(this, MakeActivity.class);
Log.d("veckon_md", "1111111="+all_path);
                intentM.putExtra("multiPicPath", all_path);
                startActivity(intentM);

            }
        } else if (requestCode == RESULT_LOAD_CAMERA) {
            //from camera app
            Log.d("veckon_md", "function for camera");
            if(resultCode == RESULT_OK) {
                imgUri = mImageCaptureUri;
                String picturePath = imgUri.getPath();
                if (picturePath != null) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, MakeActivity.class);
                    intent.putExtra("picturePath", picturePath);
                    startActivity(intent);
                }

            }

        }
    }


}
