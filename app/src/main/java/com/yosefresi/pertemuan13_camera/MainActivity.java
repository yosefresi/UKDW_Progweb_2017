package com.yosefresi.pertemuan13_camera;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //definisikan request code
    private static final int REQUEST_CAMERA = 29;
    private static final int REQUEST_SOUND_RECORDER = 30;

    private Button btnCamera;
    private ImageView imgPhoto;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("sp",MODE_PRIVATE);
        btnCamera = (Button)findViewById(R.id.btn_camera);
        imgPhoto = (ImageView)findViewById(R.id.img_photo);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case REQUEST_CAMERA:
                Bitmap bitmap = BitmapFactory.decodeFile(sp.getString("pathPhoto",null));
                imgPhoto.setImageBitmap(bitmap);
                break;
            case REQUEST_SOUND_RECORDER:
                break;
        }
    }

    private void takePhoto(){
        File image = createFile();
        if(image != null){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
            startActivityForResult(intent, REQUEST_CAMERA);
        }

    }

    //tempat camera menampung foto
    private File createFile(){
        //timestamp untuk nama file
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "IMG_"+timestamp;
        //direktori penyimpanan file
        File storage = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
        );
        //membuat direktori sendiri:
            //File storage = new File(Environment.getExternalStoragePublicDirectory)+"/progweb";
            //storage.mkdirs();

        //buat file image
        File image = null;
        try{
            image = File.createTempFile(fileName,".jpg",storage);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("pathPhoto", image.getAbsolutePath());
            editor.commit();
        }catch(Exception ex){

        }
        return image;
    }
}
