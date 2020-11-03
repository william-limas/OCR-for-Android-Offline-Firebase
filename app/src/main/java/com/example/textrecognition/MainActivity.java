package com.example.textrecognition;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button chooseImageBtn, detectTextBtn;
    private ImageView imageView;
    private TextView textView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chooseImageBtn = findViewById(R.id.choose_image_btn);
        detectTextBtn = findViewById(R.id.detect_text_image_btn);
        imageView = findViewById(R.id.image_view);
        textView = findViewById(R.id.text_display);

        detectTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectTextFromImage();
            }
        });
    }

//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(imageBitmap);
//        }
//    }
    public void chooseImg(View view) {
        CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this);
        textView.setText("");
    }
    private void detectTextFromImage() {
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextDetector firebaseVisionTextDetector = FirebaseVision.getInstance().getVisionTextDetector();
        firebaseVisionTextDetector.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                displayTextFromImage(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Error: ", e.getMessage());
            }
        });
    }

    private void displayTextFromImage(FirebaseVisionText firebaseVisionText) {
        List<FirebaseVisionText.Block> blockList = firebaseVisionText.getBlocks();
        if (blockList.size()==0)
        {
            Toast.makeText(this, "No Text Found in image.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            for (FirebaseVisionText.Block block: firebaseVisionText.getBlocks())
            {
                String text = block.getText();
                // baca gambar apa adanya
                //textView.setText(text);

                //flowmeter String manipulation
                String replaceText_o = text.replace('o','0');
                String replaceText_O = replaceText_o.replace('O','0');
                String replaceText_Z = replaceText_O.replace('Z','7');
                String replaceText_A = replaceText_Z.replace('A','4');
                String replaceText_l = replaceText_A.replace("l","");
                String replaceGaris = replaceText_l.replace("|","");
                String replaceD = replaceGaris.replace('D','0');
                String replaced = replaceD.replace('d','0');
                String replacee = replaced.replace('e','9');
                String replaceP = replacee.replace('P','0');
                String replacep = replaceP.replace('p','0');
                String replaceText_a = replacep.replace("a","");
                String replaceTitik = replaceText_a.replace(".","");
                String replacebagi = replaceTitik.replace(":","");
                String replacegaring = replacebagi.replace("/","");
                String replaceI = replacegaring.replace("I","");
                String replaceL = replaceI.replace("L","1");
                String replaceS = replaceL.replace("S","3");
                String replaceJU = replaceS.replace("JU","1");
                String replacei = replaceJU.replace("i","1");
                String replace_kotak1 = replacei.replace("[","");
                String replace_kotak2 = replace_kotak1.replace("]","");
                String replaceb = replace_kotak2.replace("b","0");
                String replaceh = replaceb.replace("h","0");
                String replaceSpace = replaceh.replace(" ","");
                String replacea = replaceSpace.replace("a","0");
                String replaceT = replacea.replace("T","1");
                String replaceg = replaceT.replace('g','9');
                String replaceB = replaceg.replace('B','8');
                String replaceU = replaceB.replace('U','0');

                if(replaceSpace.substring(0,1).equals("E")){
                    String replaceE = replaceU.replace('E','3');
                    textView.setText(replaceE);
                }
                else{
                    String replaceE = replaceU.replace("E","");
                    textView.setText(replaceE);
                }


                //nyari NIE doang
//                String NIE_text = text.replaceAll("[^0-9]","");
//                StringBuilder textNIE = new StringBuilder(NIE_text);
//                //coba get Tahun, jangan pake angka 202
//                int year = Calendar.getInstance().get(Calendar.YEAR);
//                String tahun = String.valueOf(year);
//                int a = textNIE.indexOf(tahun);

//                textview1.setText("NIE : " + NIE_text.substring(a,a+11));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of CropImageActivity
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                ((ImageView) findViewById(R.id.image_view)).setImageURI(result.getUri());
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);

                //grayscale
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                imageView.setColorFilter(filter);

                imageView.buildDrawingCache();
                imageBitmap=imageView.getDrawingCache();
                Toast.makeText(
                        this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG)
                        .show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }


}
