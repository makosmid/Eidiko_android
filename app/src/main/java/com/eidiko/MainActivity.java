package com.eidiko;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.IOException;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar appBar = (Toolbar) findViewById(R.id.uptoolbar);
        this.setSupportActionBar(appBar);

    // Floating Action Buttons
    //1. SD or Camera
        final FloatingActionsMenu menu = (FloatingActionsMenu)findViewById(R.id.fLButtons);

        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.SDsource);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.collapse();
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        button = (FloatingActionButton) findViewById(R.id.CMRsource);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.collapse();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });
    //2. Effects
        final FloatingActionsMenu filtermenu = (FloatingActionsMenu)findViewById(R.id.effectButton);

        button = (FloatingActionButton) findViewById(R.id.negfilter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtermenu.collapse();
                ImageFilter img = new ImageFilter((ImageView)findViewById(R.id.imageView));
                img.Apply();
            }
        });
    
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    // Fortwsi eikonas --eite apo sdCard eite apo Camera--
    // 1. me to swsto orientation
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {

                    Uri selectedImage = data.getData();

                    try {
                        ScrollView container = (ScrollView) findViewById(R.id.imgcontainer);
                        ImageView myImage = (ImageView) findViewById(R.id.imageView);

                        if (myImage.getDrawable() != null)
                            ((BitmapDrawable) myImage.getDrawable()).getBitmap().recycle();

                        Bitmap yourSelectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                        int orientation = getOrientation(this, selectedImage);
                        Bitmap rightOrientation = RotateBitmap(yourSelectedImage, orientation);
                        if (orientation != 0)
                            yourSelectedImage.recycle();

    // 2. me to swsto megethos antistoixo me to ScrollView container
                        int maxwidth = container.getWidth();
                        int maxheight = container.getHeight();


                        int width = rightOrientation.getWidth();
                        int height = rightOrientation.getHeight();
                        float ratio = ((float) width) / height;
                        if (width > maxwidth) {
                            width = maxwidth;
                            height = Math.round(maxwidth / ratio);
                            if (height > maxheight) {
                                height = maxheight;
                                width = Math.round(height * ratio);
                            }
                        }

                        if (height > maxheight) {
                            height = maxheight;
                            width = Math.round(height * ratio);
                            if (width > maxwidth) {
                                width = maxwidth;
                                height = Math.round(width / ratio);
                            }
                        }

                        rightOrientation = Bitmap.createScaledBitmap(rightOrientation, width, height, true);

                        myImage.setImageBitmap(rightOrientation);

                    }
                    catch (IOException e) {

                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
         /*
       ------------- Makis-------------

       ImageView myImage = (ImageView) findViewById(R.id.imageView);

        switch(requestCode) {
            case 0:
                Uri imageFileUri = data.getData();
                try {
                    BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
                    bmpFactoryOptions.inJustDecodeBounds = true;
                    bmpFactoryOptions.inSampleSize = 2;
                    bmpFactoryOptions.inJustDecodeBounds = false;
                    Bitmap bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageFileUri), null, bmpFactoryOptions);

                    Display display = getWindowManager().getDefaultDisplay();
                    int width = display.getWidth();  // deprecated
                    int height = display.getHeight();

                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);

                    //Create bitmap with new values.
                    bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                    bmp = Bitmap.createScaledBitmap(bmp, width, height, true);
                    myImage.setImageBitmap(bmp);
                } catch (Exception e) {
                    Log.v("ERROR", e.toString());
                }
            }
        }*/
    }

    // Mazi me to rotate tis kathe eikonas, kanoume kai to rotate tou antistoixou bitmap
    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    // Vriskoume to orientation kathe eikonas
    public static int getOrientation(Context context, Uri photoUri) {
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION },
                null, null, null);

        try {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
            else {
                return -1;
            }
        }
        finally {
            cursor.close();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Invert filter
    static class ImageFilter {

        public ImageView getImgView() {
            return imgView;
        }

        public void setImgView(ImageView imgView) {
            this.imgView = imgView;
        }

        private ImageView imgView;

        public ImageFilter(ImageView imgView) {
            this.imgView = imgView;
        }

        public void Apply() {
            if (imgView.getDrawable() == null)
                return;

            Bitmap orig = ((BitmapDrawable)imgView.getDrawable()).getBitmap();
            Bitmap temp = orig.copy(Bitmap.Config.ARGB_8888,true) ;

            int height = temp.getHeight();
            int width = temp.getWidth();

            for (int i=0;i<height;i++)
                for (int j=0;j<width;j++) {
                    int pixelVal = temp.getPixel(j,i);
                    temp.setPixel(j, i, Color.argb(0xFF, 0xFF - Color.red(pixelVal), 0xFF - Color.green(pixelVal), 0xFF - Color.blue(pixelVal)));
                }
            orig.recycle();
            imgView.setImageBitmap(temp);
        }
    }
}

