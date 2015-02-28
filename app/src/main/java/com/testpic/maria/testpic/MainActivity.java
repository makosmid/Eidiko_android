package com.testpic.maria.testpic;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){

                    Uri selectedImage = data.getData();

                    try {
                        ImageView myImage = (ImageView) findViewById(R.id.imageView);

                        if (myImage.getDrawable() != null)
                            ((BitmapDrawable)myImage.getDrawable()).getBitmap().recycle();

                        Bitmap yourSelectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                        int orientation = getOrientation(this,selectedImage);
                        Bitmap rightOrientation = RotateBitmap(yourSelectedImage, orientation);
                        if (orientation!=0)
                            yourSelectedImage.recycle();

                        if ((rightOrientation.getWidth() > 1920) || (rightOrientation.getHeight() > 1080))
                        {
                            int width = (rightOrientation.getWidth() > 1920) ? 1920 :rightOrientation.getWidth();
                            int height = (rightOrientation.getHeight() > 1080) ? 1080 : rightOrientation.getHeight();
                            rightOrientation = Bitmap.createScaledBitmap(rightOrientation, width, height, true);
                        }




                        myImage.setImageBitmap(rightOrientation);



                       /* RotateAnimation rotateAnimation = new RotateAnimation(0, getOrientation(this,selectedImage),
                                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                                0.5f);
                        rotateAnimation.setInterpolator(new LinearInterpolator());
                        rotateAnimation.setDuration(200);
                        rotateAnimation.setFillAfter(true);

                        myImage.startAnimation();*/

                    }catch (IOException e) {

                        e.printStackTrace();
                    }

                }
            case 1:
            if(resultCode == RESULT_OK){

                Uri selectedImage = data.getData();

                try {
                    ImageView myImage = (ImageView) findViewById(R.id.imageView);

                    if (myImage.getDrawable() != null)
                        ((BitmapDrawable)myImage.getDrawable()).getBitmap().recycle();

                    Bitmap yourSelectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                    int orientation = getOrientation(this,selectedImage);
                    Bitmap rightOrientation = RotateBitmap(yourSelectedImage, orientation);
                    if (orientation!=0)
                        yourSelectedImage.recycle();

                    if ((rightOrientation.getWidth() > 1920) || (rightOrientation.getHeight() > 1080))
                    {
                        int width = (rightOrientation.getWidth() > 1920) ? 1920 :rightOrientation.getWidth();
                        int height = (rightOrientation.getHeight() > 1080) ? 1080 : rightOrientation.getHeight();
                        rightOrientation = Bitmap.createScaledBitmap(rightOrientation, width, height, true);
                    }




                    myImage.setImageBitmap(rightOrientation);



                           /* RotateAnimation rotateAnimation = new RotateAnimation(0, getOrientation(this,selectedImage),
                                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                                    0.5f);
                            rotateAnimation.setInterpolator(new LinearInterpolator());
                            rotateAnimation.setDuration(200);
                            rotateAnimation.setFillAfter(true);

                            myImage.startAnimation();*/

                } catch (IOException e) {

                    e.printStackTrace();
                }

            }
        }
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static int getOrientation(Context context, Uri photoUri) {
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION },
                null, null, null);

        try {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            } else {
                return -1;
            }
        } finally {
            cursor.close();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sdBtn = (Button) findViewById(R.id.sdBtn);
        sdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        Button cmrBtn = (Button) findViewById(R.id.cameraBtn);
        cmrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });
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
}
