package com.example.anuteistravelingjournal.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anuteistravelingjournal.R;
import com.example.anuteistravelingjournal.database.MemoriesDatabase;
import com.example.anuteistravelingjournal.database.MemoryDao;
import com.example.anuteistravelingjournal.entities.Memory;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateMemoryActivity extends AppCompatActivity {
    private EditText inputMemoryTitle, inputMemorysubtitle, inputMemoryText;
    private TextView dateandtime;
    private static final int REQUEST_CODE_STORAGE_PERMISSION=1;
    private static final int REQUEST_CODE_SELECT_IMAGE=2;
    private ImageView memoryimage;
    private String selectedImagePath="";
    //this entity keeps the entity who will get updated
    private Memory alreadyAvailableMemory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memory);

        //fields from the add memory layout
        ImageView back_button = findViewById(R.id.backbutton);
        inputMemoryTitle = findViewById(R.id.inputMemoryTitle);
        inputMemorysubtitle = findViewById(R.id.inputMemorySubtitle);
        inputMemoryText = findViewById(R.id.inputMemoryText);
        dateandtime = findViewById(R.id.dateandtime);

        //image path
        selectedImagePath=null;
        //done button which trigers the adding of a new memory
        ImageView donebutton = findViewById(R.id.done_button);
        donebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveMemory();
            }
        });
        //the format for date
        dateandtime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date())

        );

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //add photo button + trigger
        ImageView add_photo=findViewById(R.id.insertphoto_button);
        add_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //it checks if the permision to the internal storage is given, otherwise it asks for it
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(CreateMemoryActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_STORAGE_PERMISSION);
                }
                else
                    //the function which selects a picture from the internal storage
                    selectpicture();

            }
        });
        // the image from the create memory layout
        memoryimage=findViewById(R.id.memoryimage);

        // if a memory is selected the alreadyAvailableMemory will be initialised
        if(getIntent().getBooleanExtra("isViewOrUpdate",false)){
            alreadyAvailableMemory=(Memory) getIntent().getSerializableExtra("memory");
            setViewOrUpdateMemory();
        }

        ImageView deleteImage=findViewById(R.id.deleteimage);
        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memoryimage.setImageBitmap(null);
                memoryimage.setVisibility(View.GONE);
                findViewById(R.id.deleteimage).setVisibility(View.GONE);
                selectedImagePath="";
            }
        });
    }

    //this function get the inputs from the
    private void SaveMemory() {
        if (inputMemoryTitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Memory title cant be empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (inputMemorysubtitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Memory subtitle cant be empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (inputMemoryText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "You must write something", Toast.LENGTH_SHORT).show();
            return;
        }
        final Memory m = new Memory();
        m.setTitle(inputMemoryTitle.getText().toString());
        m.setText(inputMemoryText.getText().toString());
        m.setSubtitle(inputMemorysubtitle.getText().toString());
        m.setDateTime(dateandtime.getText().toString());
        m.setImagePath(selectedImagePath);

        // if there is a memory to update m will take its inputs and its id, if there are 2 entities with the same id
        // the one from the database will be replaced OnConflictStrategy.REPLACE
        if(alreadyAvailableMemory!=null)
            m.setId(alreadyAvailableMemory.getId());

        //a separate thread from main thread which inserts the new data into the database
        new Thread(new Runnable() {
            @Override
            public void run() {
                MemoriesDatabase.getDatase(getApplicationContext()).memoryDao().insertMemory(m);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);

                finish();
            }
        }).start();

    }


    private void selectpicture(){
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(intent.resolveActivity(getPackageManager())!=null)
            startActivityForResult(intent,REQUEST_CODE_SELECT_IMAGE);
    }


    //this function checks the permisions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE_STORAGE_PERMISSION && grantResults.length>0 )
        {if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
                selectpicture();
            else
                Toast.makeText(this,"Permision Denied", Toast.LENGTH_SHORT).show();
    }}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_SELECT_IMAGE && resultCode==RESULT_OK){
            if(data!=null){
                Uri selectedImageUri=data.getData();
                if(selectedImageUri!=null){
                    try{
                        InputStream inputStream=getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                        memoryimage.setImageBitmap(bitmap);
                        memoryimage.setVisibility(View.VISIBLE);
                        findViewById(R.id.deleteimage).setVisibility(View.VISIBLE);
                        selectedImagePath=getpathFromUri(selectedImageUri);
                    }
                    catch(Exception exception){Toast.makeText(this,exception.getMessage(),Toast.LENGTH_SHORT);}
                }

            }
        }
    }
    //URI =  a compact sequence of characters that identifies an abstract or physical resource
    //URI= encapsulation+data encryption
    private String getpathFromUri(Uri contentUri){
        String filepath;
        Cursor cursor=getContentResolver().query(contentUri,null,null,null,null);
        if(cursor==null)
         filepath=contentUri.getPath();
        else{
            cursor.moveToFirst();
            int index= cursor.getColumnIndex("_data");
            filepath=cursor.getString(index);
            cursor.close();
        }
        return filepath;
    }

    //update function
    private void setViewOrUpdateMemory(){
        inputMemoryText.setText(alreadyAvailableMemory.getText());
        inputMemoryTitle.setText(alreadyAvailableMemory.getTitle());
        inputMemorysubtitle.setText(alreadyAvailableMemory.getSubtitle());
        dateandtime.setText(alreadyAvailableMemory.getDateTime());
        if(alreadyAvailableMemory.getImagePath()!=null && !alreadyAvailableMemory.getImagePath().trim().isEmpty())
        {
            memoryimage.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableMemory.getImagePath()));
            memoryimage.setVisibility(View.VISIBLE);
            findViewById(R.id.deleteimage).setVisibility(View.VISIBLE);
            selectedImagePath=alreadyAvailableMemory.getImagePath();

        }
    }
}
