package com.notebook.luk.spzapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStorageDirectory;

public class MainActivity extends Activity {

    private Button cameraBtn;
    private TextView textView;
    private static final int MY_PERMISSION_REQUEST_CAMERA = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraBtn = (Button) findViewById(R.id.cameraButton);
        textView = (TextView) findViewById(R.id.textView);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
//                if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.M && result != PackageManager.PERMISSION_GRANTED) {
//
//                    requestPermissions(new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);
//                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                    startActivity(intent);
//
//                } else {
//                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                    startActivity(intent);
//                }
                if (Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    textView.append("||in da onCreate check permissions||");
                    return;
                }

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, MY_PERMISSION_REQUEST_CAMERA);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.append("||in da onCreate request permissions||");
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);
        }
    }

    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory(),"/SPZimages");

        if (!direct.exists()) {
            direct.mkdirs();

            for (String f : getExternalStorageDirectory().list()) {
                System.out.println(f.toString());
            }
        }
        for (String f : getExternalStorageDirectory().list()) {
            System.out.println(f.toString());
        }

        File file = new File(direct, fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            textView.setText(file.getAbsolutePath());
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_PERMISSION_REQUEST_CAMERA && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("MM.dd.yyyy_hh:mm");
            String date = format.format(now);
            File file = new File(getExternalStorageDirectory(), "img_" + date + ".jpg");


            createDirectoryAndSaveFile(photo, file.getName());
        }
    }
}

