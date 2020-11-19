package com.example.imagetotext;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.util.List;


public class MainActivity extends AppCompatActivity
{
     Button captureImage,detectImage;
     ImageView img;
     TextView tv;
    Bitmap imageBitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        img=(ImageView) findViewById(R.id.imageView);
        tv=(TextView) findViewById(R.id.textView);
        captureImage=(Button) findViewById(R.id.button6);
        detectImage=(Button) findViewById(R.id.button7);
       captureImage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dispatchTakePictureIntent();
           }
       });
       detectImage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                       detectImagefromtext();
           }
       });
    }




    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
             imageBitmap = (Bitmap) extras.get("data");
            img.setImageBitmap(imageBitmap);
        }
    }
    private void detectImagefromtext()
    {
        final FirebaseVisionImage firebaseVisionImage=FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextDetector firebaseVisionTextDetector=FirebaseVision.getInstance().getVisionTextDetector();
        firebaseVisionTextDetector.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText)
            {

                detectextfromimage(firebaseVisionText);
               // Toast.makeText(getApplicationContext(),"NO ",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
           Toast.makeText(getApplicationContext(),"ERROR OCCURED",Toast.LENGTH_LONG).show();
            }
        });



    }

    private void detectextfromimage(FirebaseVisionText text)
    {
        List<FirebaseVisionText.Block> list=text.getBlocks();
        if(list.size()==0)
            Toast.makeText(getApplicationContext(),"ERROR OCCURED",Toast.LENGTH_LONG).show();
        else
        {
            String s="";
            for(int i=0;i<list.size();i++)
            {
                s=s+(list.get(i).getText())+"\n";
            }
            tv.setText(s);
        }

    }




}
