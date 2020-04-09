package com.example.pagunoi.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pagunoi.R;
import com.example.pagunoi.roomdb.OnReportRepositoryActionListener;
import com.example.pagunoi.roomdb.Report;
import com.example.pagunoi.roomdb.ReportRepository;
import com.example.pagunoi.utils.EmailSender;
import com.example.pagunoi.utils.PdfCreator;
import com.example.pagunoi.utils.PermissionUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.itextpdf.text.DocumentException;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;

public class ReportCreatorActivity extends AppCompatActivity
                                   implements ActivityCompat.OnRequestPermissionsResultCallback,
                                                View.OnClickListener,
                                                OnReportRepositoryActionListener {
    //Constants
    private static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;
    public static final String LOCATION_KEY = "location";
    private static final int RESULT_LOAD_IMG = 1;
    //View Items
    private ImageView imageView;
    private EditText adresaPersonala;
    private EditText numeComplet;
    private EditText mentiuniEditText;
    //Class Fields
    private Uri imageUri;
    private Bitmap savedImage;
    private boolean mPermissionDenied;
    private String locatieSesizare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_creator);
        initialiseViewItems();
        findViewById(R.id.gallery_button).setOnClickListener(this);
        findViewById(R.id.save_report).setOnClickListener(this);
        findViewById(R.id.email_report).setOnClickListener(this);
        findViewById(R.id.checkBox_local_data).setOnClickListener(this);
    }
    private void initialiseViewItems() {
        locatieSesizare = ((TextView)findViewById(R.id.location_textview)).getText().toString();
        Bundle bundle = getIntent().getExtras();
        String location = bundle.getString(LOCATION_KEY);
        mentiuniEditText = findViewById(R.id.editText);
        ((TextView)findViewById(R.id.location_textview)).setText(location);
        imageView = findViewById(R.id.imageView);
        numeComplet = findViewById(R.id.nume_complet_editText);
        adresaPersonala = findViewById(R.id.adresa_personala_editText);
    }
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == RESULT_LOAD_IMG && resultCode == RESULT_OK) {
            try {
                imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                savedImage = selectedImage;
               imageView.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            Log.d("REPORT", "Another request code : " + requestCode);
            return;
        }
        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Enable the my location layer if the permission has been granted.
            savePdf();
        } else {
            // Permission was denied. Display an error message
            mPermissionDenied = true;
        }
    }

    public void savePdf() {
        if (ContextCompat.checkSelfPermission(ReportCreatorActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d("REPORT", "Creating Report in Activity");
            String mentiuni = mentiuniEditText.getText().toString();
            try {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                PdfCreator creator = new PdfCreator(getApplicationContext());
                String pdfPath = creator.buildPdf(savedImage,
                        numeComplet.getText().toString(),
                        adresaPersonala.getText().toString(),
                        locatieSesizare,
                        mentiuni);
                Toast.makeText(ReportCreatorActivity.this,"Report is saved at:\n" + pdfPath, Toast.LENGTH_LONG).show();

                ReportRepository reportRepository = new ReportRepository(getApplicationContext());
                reportRepository.insertTask(new Report(pdfPath,auth.getCurrentUser().getEmail()),this);

                displayPdf(pdfPath);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
                Toast.makeText(this,"Please install a PDF Reader", Toast.LENGTH_LONG).show();
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else {
            Log.d("REPORT", "Requesting permission in Activity");
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(this, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
        }
    }

    private void displayPdf(String pdfPath) {
        File file = new File(pdfPath);
        Intent target = new Intent(Intent.ACTION_VIEW);
        if(Build.VERSION.SDK_INT>=24){
            try{
                //For API's > 24, runtime exception occurs when a URI is exposed BEYOND this particular app that you are writing (AKA when user attempts to open in device/emulator
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        target.setDataAndType(Uri.fromFile(file),"application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");

        startActivity(intent);
    }
    private void addPhoto(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true, Manifest.permission.WRITE_EXTERNAL_STORAGE).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.gallery_button) {
            addPhoto();
        } else if (i == R.id.save_report) {
            String mentiuni = mentiuniEditText.getText().toString();
            if (mentiuni.isEmpty()
                    || numeComplet.getText().toString().isEmpty()
                    || adresaPersonala.getText().toString().isEmpty()
                    || savedImage == null) {
                Toast.makeText(this,"Please fill in all the fields, and select an image",Toast.LENGTH_LONG).show();
            }
            else {
                saveDataToSharedPreferences();
                savePdf();
            }
        } else if (i == R.id.email_report){
            String mentiuni = mentiuniEditText.getText().toString();
            if (mentiuni.isEmpty()
                    || numeComplet.getText().toString().isEmpty()
                    || adresaPersonala.getText().toString().isEmpty()
                    || savedImage == null) {
                Toast.makeText(this,"Please fill in all the fields, and select an image",Toast.LENGTH_LONG).show();
            }
            else {
                saveDataToSharedPreferences();
                EmailSender.sendEmail(this,
                        imageUri,
                        numeComplet.getText().toString(),
                        adresaPersonala.getText().toString(),
                        locatieSesizare,
                        mentiuniEditText.getText().toString()
                );
            }
        } else if (i == R.id.checkBox_local_data){
            Log.d("CHECKBOX", "the checkbox has been clicked");
            fillWithSavedLocalData();
        }
    }
    private void saveDataToSharedPreferences(){
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(String.valueOf(R.string.SHARED_NAME_KEY), numeComplet.getText().toString());
        editor.putString(String.valueOf(R.string.ADDRESS_KEY), adresaPersonala.getText().toString());
        editor.apply();
    }
    private void fillWithSavedLocalData() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String name = sharedPreferences.getString(String.valueOf(R.string.SHARED_NAME_KEY), String.valueOf(R.string.default_value));
        String address = sharedPreferences.getString(String.valueOf(R.string.ADDRESS_KEY), String.valueOf(R.string.default_value));
        adresaPersonala.setText(address, TextView.BufferType.NORMAL);
        numeComplet.setText(name, TextView.BufferType.NORMAL);
    }

    @Override
    public void actionSuccess() {
        Log.d("DB", "PDF was saved in the database");
    }

    @Override
    public void actionFailed() {
        Log.e("DB-ERROR", "PDF was not saved in the database");
    }

    @Override
    public void updateRecycler(List<Report> users) {
        /*
        DO NOTHING HERE
         */
    }
}
