package com.example.flashcardapplication;

//import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
//import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper flashcardDB;
    EditText etTerm, etDefinition, etID;
    Button btnSave, btnViewData, btnUpdate, btnClear;

    //@SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        flashcardDB = new DatabaseHelper(this);

        etTerm = findViewById(R.id.etNewTerm);
        etDefinition = findViewById(R.id.etNewDefinition);
        etID = findViewById(R.id.etNewID);
        btnSave = findViewById(R.id.btnSave);
        btnViewData = findViewById(R.id.btnViewData);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnClear = findViewById(R.id.btnClear);

        SaveData();
        ViewData();
        UpdateData();
        DeleteData();

    }
    public void SaveData(){
        btnSave.setOnClickListener(v -> {
            String term = etTerm.getText().toString();
            String definition = etDefinition.getText().toString();

            boolean insertData = flashcardDB.addData(term, definition);

            if(insertData){
                Toast.makeText(MainActivity.this, "Flashcard Created!",
                        Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(MainActivity.this, "Error Occurred.",
                        Toast.LENGTH_LONG).show();

            }

        });
    }
    public void ViewData(){
        btnViewData.setOnClickListener(v -> {
            Cursor data = flashcardDB.showData();

            if(data.getCount() == 0){
                display("No flashcards found.");
            }

            StringBuilder buffer = new StringBuilder();
            while(data.moveToNext()){
                buffer.append("ID: ").append(data.getString(0)).append("\n");
                buffer.append("Term: ").append(data.getString(1)).append("\n");
                buffer.append("Definition: ").append(data.getString(2)).append("\n");

                display(buffer.toString());
            }
        });
    }

    public void display(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage(message);
        builder.show();
    }

    public void UpdateData(){
        btnUpdate.setOnClickListener(v -> {
            int temp = etID.getText().toString().length();
            if(temp > 0){
                boolean update  = flashcardDB.updateData(etID.getText().toString(),
                        etTerm.getText().toString(), etDefinition.getText().toString());
                if(update == true){
                    Toast.makeText(MainActivity.this, "Update Successful!",
                            Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this, "Error: update unsuccessful.",
                            Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(MainActivity.this, "Error: Invalid ID entered.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void DeleteData(){
        btnClear.setOnClickListener(v -> flashcardDB.deleteTableContents());
        Toast.makeText(MainActivity.this, "Flashcard deck cleared.",
                Toast.LENGTH_LONG).show();
    }

}
