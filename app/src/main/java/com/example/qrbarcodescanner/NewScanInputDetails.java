package com.example.qrbarcodescanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewScanInputDetails extends AppCompatActivity {

    TextView scan_number_input;
    EditText item_name_input;
    EditText brand_name_input;
    Button category_button;
    EditText amount_input;
    Button submit_button;


    private String scan_input_string;
    private String brand_input_string;
    private String item_name_input_string;
    private String category_input_string;
    private String amount_input_string;
    private String current_date;

    //the int for selected item on category list to remain after closing the list
    private int selected_int;


    //To hide keyboard when screen is pressed
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            //inits
            item_name_input = findViewById(R.id.scan_item_name);
            brand_name_input = findViewById(R.id.scan_brand_name);
            amount_input = findViewById(R.id.amount_input);


            item_name_input_string = item_name_input.getText().toString();
            brand_input_string = brand_name_input.getText().toString();
            amount_input_string = amount_input.getText().toString();

        }

        return super.dispatchTouchEvent(ev);
    }



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_scan_input_details);

        //default values necessary if things go wrong
        item_name_input_string = "";
        brand_input_string = "";
        scan_input_string = "";
        category_input_string = "";
        amount_input_string = "";
        current_date = "-101";

        //default selected int to display
        selected_int = 0;

        current_date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        //Data from Scan
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String numberScanned = extras.getString("numberScanned");
            scan_input_string = numberScanned;
        }

        //scan_input_int = Integer.parseInt(scan_input_string);

        //inits
        scan_number_input = findViewById(R.id.scan_number_input);
        item_name_input = findViewById(R.id.scan_item_name);
        brand_name_input = findViewById(R.id.scan_item_name);
        category_button = findViewById(R.id.category_button);
        amount_input = findViewById(R.id.amount_input);
        submit_button = findViewById(R.id.submit_button);



        scan_number_input.setText(scan_input_string);

        //important for selecting a category
        final int[] selected_category_int = {0};

        //item name
        item_name_input.setImeOptions(EditorInfo.IME_ACTION_DONE);
        item_name_input.setRawInputType(InputType.TYPE_CLASS_TEXT);

        TextView.OnEditorActionListener editorListener1 = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:

                        if (item_name_input.getText().toString().matches("")){
                            Toast.makeText(NewScanInputDetails.this,"No name inserted",Toast.LENGTH_LONG).show();
                            break;
                        }
                        else {
                            item_name_input_string = item_name_input.getText().toString();
                            Toast.makeText(NewScanInputDetails.this,"Item: " + item_name_input_string,Toast.LENGTH_LONG).show();

                            break;

                        }

                }
                return false;
            }
        };


        item_name_input.setOnEditorActionListener(editorListener1);

        //brand name
        brand_name_input.setImeOptions(EditorInfo.IME_ACTION_DONE);
        brand_name_input.setRawInputType(InputType.TYPE_CLASS_TEXT);

        TextView.OnEditorActionListener editorListener2 = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:

                        if (brand_name_input.getText().toString().matches("")){
                            Toast.makeText(NewScanInputDetails.this,"No brand inserted",Toast.LENGTH_LONG).show();
                            break;
                        }
                        else {
                            brand_input_string = brand_name_input.getText().toString();
                            Toast.makeText(NewScanInputDetails.this,"Brand: " + brand_input_string,Toast.LENGTH_LONG).show();

                            break;

                        }

                }
                return false;
            }
        };


        brand_name_input.setOnEditorActionListener(editorListener2);


        category_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(NewScanInputDetails.this);
                builder.setTitle("Choose a Category");

                //CATEGORY BUTTON
                // add a radio button list
                final String[] categories = {"Lacteos", "Despensa", "Frutas y verduras", "Limpieza", "Botilleria", "Bebidas Sin Alcohol", "Mascotas", "Papeleria", "Carnes Rojas", "Carnes Blancas"};
                int checkedItem = selected_int; // Botilleria
                builder.setSingleChoiceItems(categories, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user checked an item
                        selected_category_int[0] = which;

                    }
                });

                // add OK and Cancel buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user clicked OK
                        selected_int = selected_category_int[0];
                        String selected_category_string = categories[selected_category_int[0]];
                        Toast.makeText(NewScanInputDetails.this,"Selected category: " + selected_category_string,Toast.LENGTH_LONG).show();

                        category_button.setText(selected_category_string);

                        category_input_string = selected_category_string;


                    }
                }).setNegativeButton("Cancel", null);

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();


            }


        });



        amount_input.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
               amount_input.getText().clear();

               return false;

            }
        });

        //Remember that a hardware keyboard stills work, so be careful



        TextView.OnEditorActionListener editorListener3 = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:

                        Editable amount_input_string_local = amount_input.getText();

                        if (amount_input.getText().toString().matches("")){
                            Toast.makeText(NewScanInputDetails.this,"No amount inserted",Toast.LENGTH_LONG).show();
                            break;
                        }
                        else if (amount_input.getText().toString().matches("0")){
                            Toast.makeText(NewScanInputDetails.this,"No amount inserted",Toast.LENGTH_LONG).show();
                            break;
                        }
                        else {
                            Toast.makeText(NewScanInputDetails.this,"Amount: " + amount_input_string,Toast.LENGTH_LONG).show();
                            amount_input_string = amount_input_string_local.toString();
                            break;
                        }

                }
                return false;
            }
        };


        amount_input.setOnEditorActionListener(editorListener3);



        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(NewScanInputDetails.this);
                builder.setTitle("Please check if everything is correct!");                                //look for how to handle an  error on submision


                // set the custom layout
                View customLayout = getLayoutInflater().inflate(R.layout.alert_scan_details_layout, null);
                builder.setView(customLayout);

                TextView itemTextView = customLayout.findViewById(R.id.itemTextView);
                TextView amountTextView = customLayout.findViewById(R.id.amountTextView);
                TextView categoryTextView = customLayout.findViewById(R.id.categoryTextView);
                TextView brandNameTextView = customLayout.findViewById(R.id.brandNameTextView);



                itemTextView.setText(item_name_input_string);
                amountTextView.setText(amount_input_string);
                categoryTextView.setText(category_input_string);
                brandNameTextView.setText(brand_input_string);




                //so alert is not cancelled on touch outside the box
                builder.setCancelable(false);



                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        // setup the alert builder
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(NewScanInputDetails.this);
                        builder2.setTitle("");                                //look for how to handle an  error on submmision

                        // set the custom layout
                        final View customLayout = getLayoutInflater().inflate(R.layout.success_new_scan_alert, null);
                        builder2.setView(customLayout);
                        //so alert is not cancelled on touch outside the box
                        builder2.setCancelable(false);


                        //DB

                        //Create an Item Object
                        //ItemModel itemModel = new ItemModel(-1, brandNameTextView.getText().toString() ,Integer.parseInt(numberTextView.getText().toString()), categoryTextView.getText().toString(), Integer.parseInt(amountTextView.getText().toString()));
                        ItemModel itemModel;

                        try {

                            //initial ID = 1
                            itemModel = new ItemModel(1, item_name_input_string, brand_input_string, scan_input_string, category_input_string, amount_input_string, current_date);
                            Toast.makeText(NewScanInputDetails.this, itemModel.toString(), Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e) {
                            Toast.makeText(NewScanInputDetails.this, "Error creating Item", Toast.LENGTH_SHORT).show();
                            itemModel = new ItemModel(-1, "error", "error", "error", "error", "error", "error");
                        }


                        DatabaseHelper databaseHelper = new DatabaseHelper(NewScanInputDetails.this);

                        //insert item into database
                        boolean success = databaseHelper.addOne(itemModel);

                        Toast.makeText(NewScanInputDetails.this, "Success: " + success, Toast.LENGTH_SHORT).show();


                        builder2.setPositiveButton("Main menu", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // for URLs Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(scanResult));

                                Intent intent = new Intent(NewScanInputDetails.this, MainActivity.class);

                                startActivity(intent);

                                // send data from the AlertDialog to the Activity
                                TextView textView = customLayout.findViewById(R.id.txt1);

                                //do something with the text from text view
                                //sendDialogDataToActivity(textView.getText().toString());


                            }

                        });


                        builder2.setNeutralButton("Scan another", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog2, int which) {

                                scanCode();

                            }
                        });



                        // create and show the alert dialog
                        AlertDialog dialog2 = builder2.create();
                        dialog2.show();




                    }

                });


                builder.setNeutralButton("Fix", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //this function only disables the alert
                    }
                });


                /*

                builder.setNeutralButton("Scan another item", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(NewScanInputDetails.this, NewScanClass.class);
                        startActivity(intent);

                    }
                });


                 */

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();


            }


        });


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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(result.getContents());
                builder.setTitle("Scanning Result");

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                        Intent intent = new Intent(NewScanInputDetails.this, NewScanInputDetails.class);
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

            else {
                Toast.makeText(this, "No Results", Toast.LENGTH_LONG).show();
            }
        } else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }




    // do something with the data coming from the AlertDialog
    private void sendDialogDataToActivity(String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }





    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }



}


