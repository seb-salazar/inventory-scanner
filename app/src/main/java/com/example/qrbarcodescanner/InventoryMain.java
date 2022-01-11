package com.example.qrbarcodescanner;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class InventoryMain extends AppCompatActivity {

    //RecyclerView inventory_list;
    ListView inventory_list;
    ArrayAdapter itemArrayAdapter;
    private ArrayList<String> data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_main);

        inventory_list = findViewById(R.id.inventory_listView);



        final DatabaseHelper databaseHelper = new DatabaseHelper(InventoryMain.this);
        List<String[]> everyItem = databaseHelper.getAllItems();



        //showToast(everyItem.toString());

        //to make data like better when displayed, use array adapter
        //  itemArrayAdapter = new ArrayAdapter<ItemModel>(InventoryMain.this, android.R.layout.simple_list_item_1, databaseHelper.getAllItems());
        //<> means is a list of what is inside
        //there are more ways, more complex ways to make an array adapter
        //  inventory_list.setAdapter(itemArrayAdapter);

        inventory_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //the position is which item is clicked

                ItemModel clickedItem = (ItemModel) parent.getItemAtPosition(position);
                //you casted it to ItemModel
                databaseHelper.deleteItem(clickedItem);
                //to update
                //itemArrayAdapter = new ArrayAdapter<ItemModel>(InventoryMain.this, android.R.layout.simple_list_item_1, databaseHelper.getAllItems());
                //inventory_list.setAdapter(itemArrayAdapter);

                toastMessage("Deleted " + clickedItem.toString());
            }
        });



        generateListContent();

        inventory_list.setAdapter(new MyListAdapter(this, R.layout.list_row_item, everyItem));
        inventory_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(InventoryMain.this, "List item was clicked at " + position, Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void generateListContent() {
        for(int i = 0; i < 55; i++) {
            data.add("This is row number " + i);
        }
    }



    private class MyListAdapter extends ArrayAdapter<String[]> {
        private int layout;
        private List<String[]> mObject;
        private MyListAdapter(Context context, int resource, List<String[]> objects) {
            super(context, resource, objects);
            mObject = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewholder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.list_row_item_thumbnail);
                viewHolder.itemName = (TextView) convertView.findViewById(R.id.list_row_item_name);
                viewHolder.scanNumber = (TextView) convertView.findViewById(R.id.list_row_item_number);
                viewHolder.amount = (TextView) convertView.findViewById(R.id.list_row_item_amount);
                viewHolder.button = (Button) convertView.findViewById(R.id.list_row_item_btn);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();
            mainViewholder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Button was clicked for list item " + position, Toast.LENGTH_SHORT).show();
                }
            });

            try {
                mainViewholder.itemName.setText("Nombre: " + getItem(position)[0]);
                mainViewholder.scanNumber.setText("ID: " + getItem(position)[1]);
                mainViewholder.amount.setText("Quedan: " + getItem(position)[2]);
                //if one of this is gone, the app will crash, maybe a try catch
            }
            catch(Exception e) {
                Toast.makeText(getContext(), "There was an error retrieving the data from db", Toast.LENGTH_SHORT).show();
            }

            return convertView;
        }
    }
    public class ViewHolder {

        ImageView thumbnail;
        TextView scanNumber;
        TextView itemName;
        TextView amount;
        Button button;
    }



    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }




}
