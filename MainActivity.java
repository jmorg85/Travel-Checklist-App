package com.justice.travelchecklist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button addAnItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Button addItem = findViewById(R.id.addItemButton);
        Button deleteItem = findViewById(R.id.deleteItemButton);
        Button clearItem = findViewById(R.id.clearItemButton);
        LinearLayout val_layout = findViewById(R.id.root_layout);

        ListItemDbHelper dbHelper = new ListItemDbHelper(this);

        createChecklist(val_layout,dbHelper);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCheckbox(val_layout, dbHelper);
            }
        });

        deleteItem.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteChecklistItems(val_layout, dbHelper);
            }
        }));

        clearItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearChecklistItems(val_layout);
            }
        });
    }

    public void makeCheckbox(LinearLayout linearLayout, ListItemDbHelper dbHelper) {
        EditText editText = findViewById(R.id.inputText);
        String inputText = editText.getText().toString().trim();

        if(inputText.isEmpty()) {
            Toast.makeText(MainActivity.this, "The text is empty. Please write something before pressing again.", Toast.LENGTH_SHORT).show();
        }

        else {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(inputText);
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));


            boolean result = dbHelper.addChecklistItem(inputText);

            if(result == true) {
                linearLayout.addView(checkBox);
                editText.setText("");
            }

            else {
                Toast.makeText(this,"Something went wrong with adding this item or this is a duplicate item.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void deleteChecklistItems(LinearLayout linearLayout, ListItemDbHelper dbHelper) {
        int deletedItemCount = 0;

        for(int i = linearLayout.getChildCount() - 1; i>= 0; i--) {
            CheckBox box = (CheckBox)linearLayout.getChildAt(i);

            if(box.isChecked()) {
                dbHelper.deleteChecklistItem((String) box.getText());
                deletedItemCount++;
            }
        }

        if(deletedItemCount == 0) {
            Toast.makeText(MainActivity.this, "Nothing was selected. Please select something and try again.", Toast.LENGTH_SHORT).show();
        }

        else {
            linearLayout.removeAllViews();
            createChecklist(linearLayout, dbHelper);
        }
    }

    public void clearChecklistItems(LinearLayout linearLayout) {
        int uncheckedBoxes = 0;

        if(linearLayout.getChildCount() == 0)
            Toast.makeText(MainActivity.this, "There are no checkboxes. Please add something and try again.", Toast.LENGTH_SHORT).show();

        else {
            for(int i = 0; i < linearLayout.getChildCount(); i++) {
                CheckBox box = (CheckBox)linearLayout.getChildAt(i);

                if(box.isChecked()) {
                    box.toggle();
                    uncheckedBoxes++;
                }
            }

            if(uncheckedBoxes == 0)
                Toast.makeText(MainActivity.this, "There are no checked boxes. Please check some and try again.", Toast.LENGTH_SHORT).show();
        }
    }

    public void createChecklist(LinearLayout linearLayout, ListItemDbHelper dbHelper) {
        Cursor results = dbHelper.getListContents();

        while(results.moveToNext()) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(results.getString(1));
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(checkBox);
        }
    }
}