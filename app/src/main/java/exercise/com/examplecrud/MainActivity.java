package exercise.com.examplecrud;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dialogUpdatedatatype2(-1);
    }



    private void dialogUpdatedatatype2(long id) {
        final Dialog dialogAlternatif = new Dialog(MainActivity.this, R.style.Theme_Dialog_Fullscreen2);
        dialogAlternatif.setContentView(R.layout.dialog_input_update);
        Window window = dialogAlternatif.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        final Calendar myCalendar = Calendar.getInstance();
        dialogAlternatif.setCancelable(false);
        dialogAlternatif.setCanceledOnTouchOutside(false);
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnFlat =dialogAlternatif.findViewById(R.id.btnbatal);
        final MaterialEditText met_nama =dialogAlternatif.findViewById(R.id.met_nama);
        final MaterialEditText met_gender =dialogAlternatif.findViewById(R.id.met_gender);
        final MaterialEditText met_birthplace =dialogAlternatif.findViewById(R.id.met_birthplace);
        final MaterialEditText met_birthdate =dialogAlternatif.findViewById(R.id.met_birthdate);
        Button btnRSimpan =dialogAlternatif.findViewById(R.id.btnsimpan);

        met_gender.setOnClickListener(view -> {

            setSingleChoiceItems(this,"");
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                met_birthdate.setText(sdf.format(myCalendar.getTime()));
            }

        };


        met_birthdate.setOnClickListener(view -> {

            // TODO Auto-generated method stub
            new DatePickerDialog(MainActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });



        btnRSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (met_nama.getText().toString().isEmpty() ||
                        met_gender.getText().toString().isEmpty()||
                        met_birthplace.getText().toString().isEmpty()||
                        met_birthdate.getText().toString().isEmpty()
                        ){
                    Toast.makeText(MainActivity.this, "Nilai input masih belum diisi", Toast.LENGTH_SHORT).show();
                }else {




                }

            }
        });


        btnFlat.setOnClickListener(v -> dialogAlternatif.dismiss());


        dialogAlternatif.show();
    }


    private void setSingleChoiceItems(Context context, String choice){

        String[] valueinput = {""};
        List<String> dataset = new ArrayList<>();
        dataset.add("MALE");
        dataset.add("FEMALE");


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Gender");

        builder.setSingleChoiceItems( dataset.toArray(new String[dataset.size()]), 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                valueinput[0] =dataset.get(which);

            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                value.set(position,valueinput[0]);
//
//                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
