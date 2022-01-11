package com.example.qrbarcodescanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import com.google.zxing.Result;
import com.journeyapps.barcodescanner.CaptureActivity;


public class NewScanClass extends CaptureActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }







    public void handleResult(Result result) {
        final String scanResult = result.getText();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // for URLs Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(scanResult));

                Intent intent = new Intent(NewScanClass.this, NewScanInputDetails.class);

                intent.putExtra("scan_result", scanResult);

                startActivity(intent);


            }
        });
        builder.setNeutralButton("Scan again", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });


        builder.setMessage(scanResult);
        AlertDialog alert = builder.create();
        alert.show();
    }

}
