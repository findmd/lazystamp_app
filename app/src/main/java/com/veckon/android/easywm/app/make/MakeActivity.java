package com.veckon.android.easywm.app.make;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;



import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.graphics.BitmapFactory;

import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.veckon.android.easywm.app.R;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MakeActivity extends Activity {

    private int GONE = 1;
    private ViewPager mPager;
    private ImageAdapter mImageAdapter;
    private static final int NUM_PAGES = 5;

    //savePage()
    private int    wmColor;
    private float  wmAlpha;
    private float  wmSize;
    private String wmString = "";
    private float  wmLocationX;
    private float  wmLocationY;
    private Typeface wmFont;
    private String filePath="/sdcard/LazyStamp";

    //drag listener
    float mLastX, mLastY, mX, mY, mPrevX=0, mPrevY=0, mPosX, mPosY;
    int action;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("veckon_md", "MakeActivity start!!");
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //setContentView(R.layout.activity_make);



        Intent intent = getIntent();
        savedInstanceState = intent.getExtras();
        if (savedInstanceState != null) {
            if (savedInstanceState.getString("picturePath") != null) {
                setContentView(R.layout.activity_make);

                Log.d("veckon_md", "MakeActivity singlePic 111");
                String picturePath = savedInstanceState.getString("picturePath");

                ImageView imageView = (ImageView) findViewById(R.id.singleImageView);

                //image scaling
                Bitmap src;
                Bitmap check_src = BitmapFactory.decodeFile(picturePath);
                int srcW = check_src.getWidth();
                int srcH = check_src.getHeight();
                if(srcW > 2000 || srcH > 2000) {

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    src = BitmapFactory.decodeFile(picturePath, options);
                } else {
                    src = check_src;
                }
                imageView.setImageBitmap(src);
                //imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                //drag
                TextView tv_watermark = (TextView) findViewById(R.id.txt_watermark);
                tv_watermark.setOnTouchListener(mTouchListener);


            } else if (savedInstanceState.getStringArray("multiPicPath") != null) {

                Log.d("veckon_md", "MakeActivity multiPic 222");
                setContentView(R.layout.activity_make_multi);

                Log.d("veckon_md", "11="+savedInstanceState.getStringArray("multiPicPath").length);
                int length = savedInstanceState.getStringArray("multiPicPath").length;
                String path[] = savedInstanceState.getStringArray("multiPicPath");

                /*ImageView multiView = new ImageView(this);
                multiView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                multiView.setImageBitmap(multiBm);
                FrameLayout layout = (FrameLayout) findViewById(R.id.wmFramelayout);
                layout.addView(multiView);*/

                ImageView imageView = (ImageView) findViewById(R.id.multiImageView);

                //image scaling
                Bitmap src;
                Bitmap check_src = BitmapFactory.decodeFile(path[0]);
                int srcW = check_src.getWidth();
                int srcH = check_src.getHeight();
                if(srcW > 2000 || srcH > 2000) {

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    src = BitmapFactory.decodeFile(path[0], options);
                } else {
                    src = check_src;
                }
                imageView.setImageBitmap(src);
/*
                for (int i = 1; i < length; i++){
                    Log.d("veckon_md","22="+path[i]);
                    //URI GalImages = new GalImages();
                    //Uri GalImages = Uri.parse(path[i]);


                }

                //pager 객체 생성

                mPager = (ViewPager) findViewById(R.id.pager);
                mImageAdapter = new ImageAdapter(this);
                mPager.setAdapter(mImageAdapter);
*/
                //drag
                TextView tv_watermark = (TextView) findViewById(R.id.txt_watermark);
                tv_watermark.setOnTouchListener(mTouchListener);
            }
        }
    }

    private class ImageAdapter extends PagerAdapter {

        Context context;

        public URI[] GalImages = new URI[] {
                //images
        };

        ImageAdapter(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return GalImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object){
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position){
            ImageView imageView = new ImageView(context);
            //int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_medium);
            int padding = 0;

            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            //imageView.setImageURI(GalImages[position]);

            ((ViewPager) container).addView(imageView, 0);

            return imageView;



        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }


    //drag
    View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            action = motionEvent.getAction();
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                mLastX = motionEvent.getX();
                mLastY = motionEvent.getY();
                Log.d("veckon_md", "ACTION_DOWN="+mLastX+"::"+mLastY);
            } else if (action == MotionEvent.ACTION_MOVE){

                mX = motionEvent.getX() - mLastX;
                mY = motionEvent.getY() - mLastY;

                mPosX = mPrevX + mX;
                mPosY = mPrevY + mY;

                view.setX(mPosX);
                view.setY(mPosY);

                mPrevX = mPosX;
                mPrevY = mPosY;
            }
            return true;
        }
    };

    View.OnDragListener mDragListener = new View.OnDragListener() {
        public boolean onDrag(View view, DragEvent dragEvent) {
            int action = dragEvent.getAction();
            if(action == DragEvent.ACTION_DRAG_STARTED){
                //Log.d("veckon_md", "drag starting");
            } else if(action == DragEvent.ACTION_DRAG_ENTERED){
                //Log.d("veckon_md", "drag entering");
            } else if(action == DragEvent.ACTION_DRAG_EXITED){
                //Log.d("veckon_md", "drag exiting");
                return false;
            } else if(action == DragEvent.ACTION_DROP){
                //Log.d("veckon_md", "drag drop!");
                //TextView wm = (TextView) view.findViewById(R.id.txt_watermark);
                View v = (View) dragEvent.getLocalState();

                /*ViewGroup owner = (ViewGroup) v.getParent();
                Log.d("veckon_md", "222="+owner);
                Log.d("veckon_md", "222="+view.getParent());
                owner.removeView(v);
                FrameLayout container = (FrameLayout) view;
                container.addView(v);*/
                v.setVisibility(View.VISIBLE);
            } else if(action == DragEvent.ACTION_DRAG_ENDED){
                //Log.d("veckon_md", "drag ended");
                if(dragEvent.getResult() == true){
                    //((View)(dragEvent.getLocalState())).setVisibility(View.VISIBLE);
                }
            }
            return true;
        }

    };


    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            //저장 버튼
            case R.id.btn_save:
                Log.d("veckon_md", "onClick btn_save");
                //goSavePage();
                //Toast.makeText(getApplicationContext(), getString(R.string.toast_save), Toast.LENGTH_SHORT).show();
                break;
            //초기화 버튼
            case R.id.btn_text:
                Log.d("veckon_md", "onClick btn_text");
                textTool(view);
                break;
            //폰트 버튼
            case R.id.btn_tools_font:
                Log.d("veckon_md", "onClick btn_tools_font");
                fontTool();
                break;
            //투명도 버튼
            case R.id.btn_tools_transparency:
                Log.d("veckon_md", "onClick btn_tools_transparency");
                transparencyTool();
                break;
            //사이즈 조절 버튼
            case R.id.btn_tools_size:
                Log.d("veckon_md", "onClick btn_tools_size");
                sizeTool(view);
                break;
        }

    }

    //watermark text function
    public void textTool(View view){
        Log.d("veckon_md", "Start textTool()");
        final Dialog dialog = new Dialog(this);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.dialog_text, (ViewGroup) findViewById(R.id.text_layout));
        dialog.setContentView(layout);

        dialog.setTitle("Enter watermark!");
        dialog.setCancelable(true);

        TextView tv_text = (TextView) findViewById(R.id.txt_watermark);
        if(tv_text.getText()!=this.getString(R.string.app_name)){
            EditText mText = (EditText) dialog.findViewById(R.id.edit_text);
            mText.setText( tv_text.getText());
        }
        dialog.show();

        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);
        //click cancel botton
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //click ok Botton
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv_watermark = (TextView) findViewById(R.id.txt_watermark);
                EditText mText = (EditText) dialog.findViewById(R.id.edit_text);
                //int length = mText.getText().length();
                if(mText.getText().length()==0){
                    Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.textToolAlert), Toast.LENGTH_SHORT);
                    toast.show();

                } else {

                    //tv_watermark.setText(mText.getText());
                    tv_watermark.setText(mText.getText().toString());
                    dialog.dismiss();
                }

            }
        });

    }

    //watermark location function
    /*public void locationTool(){
        Log.d("veckon_md", "start locationTool()");

        final TextView tv_watermark = (TextView) findViewById(R.id.txt_watermark);

        tv_watermark.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent me) {

                float x = me.getX();
                float y = me.getY();

                if (me.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                    v.startDrag(data, shadowBuilder, v, 0);
                    return true;
                } else {
                    return false;
                }
            }
        });

    }
    */

    //watermark font function
    public void fontTool() {
        Log.d("veckon_md", "Start fontTool()");
        final Dialog dialog = new Dialog(this);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_font, (ViewGroup) findViewById(R.id.font_layout));
        dialog.setContentView(layout);

        dialog.setTitle("Set font!");
        dialog.setCancelable(true);
        dialog.show();

        ArrayAdapter<CharSequence> Adapter;
        Adapter = ArrayAdapter.createFromResource(this, R.array.fonts, android.R.layout.simple_list_item_1);
        ListView list = (ListView) dialog.findViewById(R.id.font_list);
        list.setAdapter(Adapter);

        //dialog.show();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Typeface typeface = Typeface.DEFAULT;
                TextView tv_watermark_origin = (TextView) findViewById(R.id.txt_watermark);
                String sFont = parent.getItemAtPosition(position).toString();
                if (sFont.equals("normal_bold")) {
                    tv_watermark_origin.setTypeface(Typeface.DEFAULT_BOLD);
                } else if(sFont.equals("normal")) {
                    tv_watermark_origin.setTypeface(Typeface.DEFAULT);
                } else if(sFont.equals("serif")) {
                    tv_watermark_origin.setTypeface(Typeface.SERIF);
                } else if(sFont.equals("sans")) {
                    tv_watermark_origin.setTypeface(Typeface.SANS_SERIF);
                } else if(sFont.equals("monospace")) {
                    tv_watermark_origin.setTypeface(Typeface.MONOSPACE);
                } else {
                    typeface = Typeface.createFromAsset(getAssets(), "fonts/"+sFont + ".otf");
                }
                tv_watermark_origin.setTypeface(typeface);

                dialog.dismiss();

            }
        });


    }


    //watermark transparency function
    public void transparencyTool() {
        Log.d("veckon_md", "Start transparencyTool()");
        final Dialog dialog = new Dialog(this);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_transparency, (ViewGroup) findViewById(R.id.transparency_layout));
        dialog.setContentView(layout);

        dialog.setTitle("Set transparency!");
        dialog.setCancelable(true);
        dialog.show();

        SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.transparency_seekbar);
        final TextView set_transparency_now = (TextView) dialog.findViewById(R.id.set_transparency_now);
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);

        //original text transparency value setting
        TextView tv_watermark_origin = (TextView) findViewById(R.id.txt_watermark);
        float wm_origin_transparency = tv_watermark_origin.getAlpha();

        float transparency_percent = wm_origin_transparency * 100;
        int transparency_percent_int = (int) transparency_percent;
        set_transparency_now.setText(Integer.toString(transparency_percent_int)+ " %");
        seekBar.setProgress(transparency_percent_int);

        SeekBar.OnSeekBarChangeListener seekBarListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBark, int progress, boolean fromUser) {
                set_transparency_now.setText(progress+" %");
            }
        };
        seekBar.setOnSeekBarChangeListener(seekBarListener);
        //click cancel botton
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //click ok Botton
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv_watermark = (TextView) findViewById(R.id.txt_watermark);
                float transparency_to_alpha = Float.parseFloat(set_transparency_now.getText().toString().split(" ")[0]) / 100;
                tv_watermark.setAlpha(transparency_to_alpha);
                dialog.dismiss();
            }
        });
    }


    //watermark size function
    public void sizeTool(View v) {
        Log.d("veckon_md", "Start sizeTool()");
        final Dialog dialog = new Dialog(this);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_size, (ViewGroup) findViewById(R.id.size_layout));
        dialog.setContentView(layout);

        dialog.setTitle("Set size!");
        dialog.setCancelable(true);
        dialog.show();

        SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.size_seekbar);
        final TextView set_size_now = (TextView) dialog.findViewById(R.id.set_size_now);
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);

        //original text size value setting
        TextView tv_watermark_origin = (TextView) findViewById(R.id.txt_watermark);
        int wm_origin_size = (int) tv_watermark_origin.getTextSize();
        set_size_now.setText(Integer.toString(wm_origin_size) + " px");
        seekBar.setProgress(wm_origin_size);


        SeekBar.OnSeekBarChangeListener seekBarListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBark, int progress, boolean fromUser) {
                set_size_now.setText(progress + " px");
            }
        };
        seekBar.setOnSeekBarChangeListener(seekBarListener);
        //click cancel botton
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //click ok Botton
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv_watermark = (TextView) findViewById(R.id.txt_watermark);
                tv_watermark.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        Float.parseFloat(set_size_now.getText().toString().split(" ")[0]));
                dialog.dismiss();
            }
        });

    }



    public File shareFile;
    public void goSavePage() {
        Log.d("veckon_md", "Start goSavePage()");
        //파일 저장
        File myDir = new File(filePath);
        myDir.mkdirs();

        //naming
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
        String todayFormat = format.format(today);
        String newFileName = todayFormat + "_stamp"+".jpg";

        File file = new File (myDir, newFileName);
        if (file.exists()) {
            newFileName = "1_"+newFileName;
            file = new File (myDir, newFileName);
        }

        //share
        shareFile = file;

        try{
            FileOutputStream out = new FileOutputStream(file);

            //get status now
            ImageView singleImageView = (ImageView) findViewById(R.id.singleImageView);

            Bitmap bitMap = ((BitmapDrawable) singleImageView.getDrawable()).getBitmap();

            TextView tv_watermark = (TextView) findViewById(R.id.txt_watermark);
            wmColor  = tv_watermark.getCurrentTextColor();
            wmAlpha  = tv_watermark.getAlpha()*255;
            wmSize   = tv_watermark.getTextSize();
            wmString = tv_watermark.getText().toString();
            wmLocationX = tv_watermark.getX();
            wmLocationY = tv_watermark.getY()+wmSize;
            wmFont   = tv_watermark.getTypeface();

            Point p = new Point();
            p.set((int)wmLocationX, (int)wmLocationY);
            //URLSpan urlSpan[] = tv_watermark.getUrls();
            //Log.d("veckon_md", ":::::: watermark params ::::::"+bitMap + ", " + wmString + ", " + wmLocationX + ", " + wmLocationY + ", " + wmColor + ", " + wmAlpha + ", " + wmSize + ", " + wmFont + ".");

            //set watermark
            Bitmap bm = waterMark(bitMap,             //src
                                  wmString,           //string wm
                                  p,                  //location
                                  wmColor,            //color
                                  (int) wmAlpha,      //alpha
                                  (int) wmSize,       //size
                                  wmFont,             //font
                                  false);             //underline

            /////////////////////
            //화면에 전달
            //singleImageView.setImageBitmap(bm);
            /////////////////////

            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Toast.makeText(getApplicationContext(), getString(R.string.toast_save), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Bitmap waterMark(Bitmap src, String watermark, Point location, int color, float alpha, float size, Typeface font, boolean underline){
        Log.d("veckon_md", "waterMark start!");

        //image size
        int w = src.getWidth();
        int h = src.getHeight();

        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());

        //create canvas object
        Canvas canvas = new Canvas(result);
        //draw bitmap on canvas
        canvas.drawBitmap(src, 0, 0, null);

        //create paint object
        Paint paint = new Paint();
        //apply color
        paint.setColor(color);

        //set transparency
        paint.setAlpha((int)alpha);

        //set text size
        paint.setTextSize(size);
        paint.setAntiAlias(true);

        //set should be underlined or not
        paint.setUnderlineText(underline);

        //font
        paint.setTypeface(font);

        /////

        //draw text on given location
        canvas.drawText(watermark, location.x, location.y, paint);

        return result;
    }


    /*private class ListViewItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            Log.d("veckon_md", "onItemClick start!!");

            dialog.dismiss();
            //ContactsContract.Data data = (ContactsContract.Data) adapterView.getItemAtPosition(position);
        }
    }*/
    private ShareActionProvider mShareActionProvider;
    private ShareActionProvider mMultiShareActionProvider;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.make, menu);
        MenuItem shareItem = menu.findItem(R.id.menu_share);

        Log.d("veckon_md", "VERSION=" + Build.VERSION.SDK_INT);
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            Log.d("veckon_md", "actionbar over14-1");
            mShareActionProvider = new ShareActionProvider(this);
            //mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

            Log.d("veckon_md", "actionbar over14-2" + mShareActionProvider);
            shareItem.setActionProvider(mShareActionProvider);

            Log.d("veckon_md", "actionbar over14-3");
            //shareItem.setIcon(R.drawable.share);
            getDefaultIntent();
        } else {
            Log.d("veckon_md", "actionbar remove??");
            menu.removeItem(shareItem.getItemId());
        }



        /*if (android.os.Build.VERSION.SDK_INT >= 14) {
            mShareActionProvider = new ShareActionProvider(this);
            shareItem.setActionProvider(mShareActionProvider);
        } else {
            menu.removeItem(shareItem.getItemId());
        }
        shareImages(null, false);*/


        /*if(shareFile == null) {
            //저장된 파일이 없으면 메뉴 숨김
            shareItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        }*/

        Log.d("veckon_md", "onCreateOptionsMenu end");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                goSavePage();
                return true;

            case R.id.menu_share:
                if (android.os.Build.VERSION.SDK_INT >= 14) {
                    Log.d("veckon_md", "!!!!!!!!!_1="+getDefaultIntent());
                    Intent intentTemp = getDefaultIntent();
                    Log.d("veckon_md", "!!!!!!!!!_2="+intentTemp);
                    Log.d("veckon_md", "!!!!!!!!!_3="+mShareActionProvider);

                    //Log.d("veckon_md", "!!!!!!!!!_4="+mShareActionProvider);
                }
                //shareImages(imgList, false);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private Intent getDefaultIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpeg");
        Log.d("veckon_md", "shareFile"+shareFile);
        if(shareFile != null) {

            Uri uri = Uri.fromFile(shareFile);
            Log.d("vekcon_md", "uri="+uri);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

            mShareActionProvider.setShareIntent(shareIntent);
        }
        Log.d("veckon_md", "shareIntent"+shareIntent);
        return shareIntent;

    }

/*
    private void shareImages(List<ScanImage> imgList_, boolean fromAction_) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.setType("image/jpeg");

        ArrayList<Uri> uris = new ArrayList<Uri>();
        if (imgList_ != null) {
            for (ScanImage image : imgList_) {
                uris.add(Uri.fromFile(new File(image.getImgURL())));
            }
        }
        shareIntent.putExtra(Intent.EXTRA_STREAM, uris);

        setShareIntent(shareIntent, fromAction_);
    }


    @SuppressLint("NewApi")
    private void setShareIntent(Intent shareIntent, boolean fromAction_) {

        if (fromAction_) {
            Log.d("veckon_md", "MultiShareActionProvider : " + mMultiShareActionProvider);
            if (mMultiShareActionProvider != null) {
                mMultiShareActionProvider.setShareIntent(shareIntent);
            }
        } else {
            Log.d("veckon_md", "ShareActionProvider : " + mShareActionProvider);
            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(shareIntent);
            }

        }
    }
*/

    /**
     * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment} objects, in
     * sequence.
     */
    /*
    private class ScreenSlidePagerAdapter extends PagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.create(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }*/
}
