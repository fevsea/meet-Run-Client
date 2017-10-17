package edu.upc.fib.gps.meetnrun;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Javier on 14/10/2017.
 */

public class create_meeting extends Activity{
    private EditText name=(EditText) findViewById(R.id.name);
    private EditText date=(EditText) findViewById(R.id.date);
    private EditText hour=(EditText) findViewById(R.id.hour);
    private EditText level=(EditText) findViewById(R.id.level);
    private EditText description=(EditText) findViewById(R.id.description);
    private GoogleMap maps;

   /* private void updateLabel() {
        String myFormat = "DD/mm/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

        edittext.setText(sdf.format(myCalendar.getTime()));
    } */

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_meeting);
    }

    public void create (View view){
        String Name = name.getText().toString();
        String Date = date.getText().toString();
        int Level = Integer.parseInt(level.getText().toString());
        String Hour = hour.getText().toString();
        String Description = description.toString();
        if (Name.isEmpty() || Date.isEmpty() || Hour.isEmpty()){
            Toast.makeText(this, "@string/emptycreate", Toast.LENGTH_SHORT).show();
        }
        else if(Name.length()>=100) Toast.makeText(this,"@string/bigName", Toast.LENGTH_SHORT).show();
        else if(Description.length()>=500) Toast.makeText(this, "@string/bigDescription", Toast.LENGTH_SHORT).show();
        else{
        }
    }
/*  FUTURE ADD-ONS
    Calendar myCalendar = Calendar.getInstance();

    EditText edittext= (EditText) findViewById(R.id.date);
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

   edittext.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
            new DatePickerDialog(classname.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    });
    */
}