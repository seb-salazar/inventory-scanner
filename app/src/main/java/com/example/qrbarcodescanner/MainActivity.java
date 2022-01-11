package com.example.qrbarcodescanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    View scanBtn, removeBtn, inventoryBtn;

    private String current_date;

    int buttonID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanBtn = findViewById(R.id.scan_button);
        removeBtn = findViewById(R.id.remove_item_button);
        inventoryBtn = findViewById(R.id.inventory_button);


        //current date
        current_date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());



        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buttonID = 1;
                scanCode();


            }


        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buttonID = 2;
                scanCode();

            }
        });


        inventoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buttonID = 3;

                Intent intent = new Intent(MainActivity.this, InventoryMain.class);

                startActivity(intent);



            }
        });

    }

    void showToast(String msg){
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }


    private void scanCode(){

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(NewScanClass.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning code");
        integrator.initiateScan();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){



            if (result.getContents() != null){


                final DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);

                if (databaseHelper.searchForItem(result.getContents())) {

                    Toast.makeText(this, "It is already in the database", Toast.LENGTH_LONG).show();

                    if (buttonID == 1) {
                        incrementRecognisedItemAmount(result.getContents());
                    }

                    if (buttonID == 2) {
                        decrementRecognisedItemAmount(result.getContents());
                    }


                }

                else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(result.getContents());
                    builder.setTitle("Scanning Result");

                    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //finish();
                            Intent intent = new Intent(MainActivity.this, NewScanInputDetails.class);
                            intent.putExtra("numberScanned", result.getContents());
                            startActivity(intent);

                        }
                    }).setNeutralButton("Scan Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            scanCode();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }


            }

            else {
                Toast.makeText(this, "No Results", Toast.LENGTH_LONG).show();
            }
        } else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    Integer amount;

    private void incrementRecognisedItemAmount(final String barcodeNumber) {

        final DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Item Found!");                                //look for how to handle an  error on submision


        // set the custom layout
        View customLayout = getLayoutInflater().inflate(R.layout.found_item_amount_layout, null);
        builder.setView(customLayout);

        final TextView amountIncrement = customLayout.findViewById(R.id.amount_increment);
        FloatingActionButton reduceBtn = customLayout.findViewById(R.id.minus_button);
        FloatingActionButton increaseBtn = customLayout.findViewById(R.id.plus_button);

        final TextView items_string = customLayout.findViewById(R.id.items_string);


        amount = 1;

        amountIncrement.setText("" +amount);

        reduceBtn.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {

                int amount_local = amount;

                amount_local -= 1;

                if (amount_local < 1){
                    amount_local = 1;
                }

                amount = amount_local;

                amountIncrement.setText("" +amount_local);


                //items
                if (amount == 1) {
                    items_string.setText("ITEM");
                }
                else {
                    items_string.setText("ITEMS");
                }
            }
        });

        increaseBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                int amount_local = amount;

                amount_local += 1;

                if (amount_local > 99){
                    amount_local = 99;
                }

                amount = amount_local;

                amountIncrement.setText("" +amount_local);


                //items
                if (amount == 1) {
                    items_string.setText("ITEM");
                }
                else {
                    items_string.setText("ITEMS");
                }


            }
        });






        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String amount_string_local = String.valueOf(amount);

                databaseHelper.updateAmountUP(amount_string_local, barcodeNumber);
                databaseHelper.updateDate(current_date, barcodeNumber);


                Toast.makeText(MainActivity.this, "Amount Updated Successfully" , Toast.LENGTH_SHORT).show();



            }
        }).setNeutralButton("?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scanCode();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }


    
    Integer amount2;

    private void decrementRecognisedItemAmount(final String barcodeNumber) {

        final DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Amount to Delete!");                                //look for how to handle an  error on submision


        // set the custom layout
        View customLayout = getLayoutInflater().inflate(R.layout.delete_existing_items, null);
        builder.setView(customLayout);

        final TextView amountIncrement = customLayout.findViewById(R.id.amount_increment);
        FloatingActionButton reduceBtn = customLayout.findViewById(R.id.minus_button);
        FloatingActionButton increaseBtn = customLayout.findViewById(R.id.plus_button);

        final TextView items_string = customLayout.findViewById(R.id.items_string);


        amount2 = 1;

        amountIncrement.setText("" +amount2);

        reduceBtn.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {

                int amount_local = amount2;

                amount_local -= 1;

                if (amount_local < 1){
                    amount_local = 1;
                }

                amount2 = amount_local;

                amountIncrement.setText("" +amount_local);


                //items
                if (amount2 == 1) {
                    items_string.setText("ITEM");
                }
                else {
                    items_string.setText("ITEMS");
                }
            }
        });

        increaseBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                int amount_local = amount2;

                amount_local += 1;

                if (amount_local > 99){
                    amount_local = 99;
                }

                amount2 = amount_local;

                amountIncrement.setText("" +amount_local);


                //items
                if (amount2 == 1) {
                    items_string.setText("ITEM");
                }
                else {
                    items_string.setText("ITEMS");
                }


            }
        });






        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String amount_string_local = String.valueOf(amount2);

                databaseHelper.updateAmountDOWN(amount_string_local, barcodeNumber);
                databaseHelper.updateDate(current_date, barcodeNumber);


                Toast.makeText(MainActivity.this, "Amount Updated Successfully" , Toast.LENGTH_SHORT).show();



            }
        }).setNeutralButton("?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scanCode();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }



}
