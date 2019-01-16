package exercise.com.examplecrud;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.common.ArrayListAccumulator;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import exercise.com.examplecrud.model.karyawan;
import exercise.com.examplecrud.network.Api;
import exercise.com.examplecrud.network.Constant;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private android.support.v7.widget.Toolbar toolbar;
    private MainActivity obj;

    private ArrayAdapter<String> adapter;
    private List<karyawan.Data> ListData=new ArrayList<>();;
    private List<String> arrayList=new ArrayList<>();;
    private ArrayList<String> arrayListID =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAdd =findViewById(R.id.btnAdd);
        ListView lsvw_data=findViewById(R.id.lsvw_data);
        arrayList = new ArrayList<String>();

        obj = this;

        // Adapter: You need three parameters 'the context, arrayListID of the layout (it will be where the data is shown),
        // and the array that contains the data
        adapter = adapter=new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, arrayList){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(android.R.id.text1);

                /*YOUR CHOICE OF COLOR*/
                textView.setTextColor(Color.BLACK);

                return view;
            }
        };
        // Here, you set the data in your ListView
        lsvw_data.setAdapter(adapter);



        btnAdd.setOnClickListener(view -> {
            dialogUpdatedatatype2(-1);
        });


        lsvw_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showDialogListOpsi(j -> {
                    if (j == 0) {
                        dialogUpdatedatatype2(position);
                    }else if (j==1){

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Apakah anda yakin ingin menghapus data ini ?");
                        builder.setPositiveButton("YA", (dialogInterface, i) -> {
                            delete(arrayListID.get(position));

                        });
                        builder.setNegativeButton("TIDAK", (dialogInterface, i) -> {
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();



                    }


                }, MainActivity.this);

            }

        });


        SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRecordUser();


                pullToRefresh.setRefreshing(false);
            }
        });


        getRecordUser();


    }


    public void showDialogListOpsi(ListenerDialog listener, Context context) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context, R.style.Theme_Dialog_Margin_4);
        List<String> where = new ArrayList<String>();

        where.add("Edit");
        where.add("Hapus");

        String[] strings = new String[where.size()];
        where.toArray(strings);

        builder.setItems(strings, (dialog, which) -> {
            switch (which) {
                case 0:
                    listener.onClick(0);
                    break;
                default:
                    listener.onClick(which);
            }
        });

        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width =toDIP(context, 360);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        alertDialog.getWindow().setAttributes(lp);
    }

    public static final int toDIP(Context pContext, float p_value) {
        return (int) ((p_value * pContext.getResources().getDisplayMetrics().density) + 0.5f);
    }


    MaterialEditText met_gender;

    private void dialogUpdatedatatype2(int position) {
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
        TextView htvw_title =dialogAlternatif.findViewById(R.id.htvw_title);
        final MaterialEditText met_nama =dialogAlternatif.findViewById(R.id.met_nama);
        met_gender =dialogAlternatif.findViewById(R.id.met_gender);
        final MaterialEditText met_birthplace =dialogAlternatif.findViewById(R.id.met_birthplace);
        final MaterialEditText met_birthdate =dialogAlternatif.findViewById(R.id.met_birthdate);
        Button btnRSimpan =dialogAlternatif.findViewById(R.id.btnsimpan);

        met_gender.setOnClickListener(view -> {

            setSingleChoiceItems(this,"");
        });


        htvw_title.setText("Create");


        if (position >-1){
            met_nama.setText(ListData.get(position).getName());
            met_gender.setText((ListData.get(position).getGender().equals("1")?"male":"female"));
            met_birthplace.setText(ListData.get(position).getBirthplace());
            met_birthdate.setText(ListData.get(position).getBirthdate());
            ;
            htvw_title.setText("Update");
        }




        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = Constant.default_simpledate2; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                met_birthdate.setText(sdf.format(myCalendar.getTime()));
            }

        };


        met_birthdate.setOnClickListener(view -> {

            // TODO Auto-generated method stub
            new DatePickerDialog(MainActivity.this, date,
                    Integer.valueOf(1990),
                    myCalendar.get(Calendar.MONTH),
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

                    if (position ==-1){
                        add(met_nama.getText().toString(),
                                met_gender.getText().toString().toLowerCase().equals("male")?"1":"0",
                                met_birthplace.getText().toString(),
                                met_birthdate.getText().toString(),dialogAlternatif );
                    }else {
                        update(arrayListID.get(position),met_nama.getText().toString(),
                                met_gender.getText().toString().toLowerCase().equals("male")?"1":"0",
                                met_birthplace.getText().toString(),
                                met_birthdate.getText().toString(),dialogAlternatif );
                    }



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
                String gender="male";
                if (which==1){
                    gender="female";
                }
                met_gender.setText(gender);
//                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    public boolean getRecordUser() {
        boolean[] a = {false};
        new AsyncTask<Void, Void, Boolean>() {
            Date dStart = null;
            Date dEnd = null;
            ProgressDialog dialog =new ProgressDialog(MainActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.setMessage("Loading get data karyawan...");
                dialog.setCancelable(false);
                obj.runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.show();
                    }
                });
                dStart = new Date();
                dEnd = new Date();

            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                boolean b=false;


                HttpUrl.Builder httpUrlBuilder = new HttpUrl.Builder();

//                httpUrlBuilder.addQueryParameter("arrayListID",arrayListID);

//                SimpleDateFormat ft = new SimpleDateFormat (Constant.default_simpledate2);


                try (Response response = new Api(MainActivity.this).
                        get(getString(R.string.api_user),httpUrlBuilder)) {
                    if (response == null || !response.isSuccessful())
                        throw new IOException("Unexpected code = " + response);

                    String responseBodyString = response.body().string();
                    Gson gson = new Gson();
                    karyawan user = gson.fromJson(responseBodyString, karyawan.class);

                    if (user.isStatus()&& user.getData().size()>0) {
                        ListData=user.getData();
                        obj.runOnUiThread(new Runnable() {

                            public void run() {

                                arrayList.clear();
                                arrayListID.clear();
                                for (int i = 0; i < user.getData().size(); i++) {

                                    arrayListID.add(user.getData().get(i).getId());
                                    arrayList.add(
                                            "id :" + user.getData().get(i).getId() + " \n" +
                                                "name :"+    user.getData().get(i).getName() + "\n"+
                                                   "gender :" +(user.getData().get(i).getGender().equals("1")?"male":"female")+ "\n"+
                                                    "birthdate :"+user.getData().get(i).getBirthdate()+ "\n"+
                                                    "birthplace : "+user.getData().get(i).getBirthplace());
                                }

                              adapter.notifyDataSetChanged();

                            }
                        });
                    }else {
                        obj.runOnUiThread(new Runnable() {
                            public void run() {

                                Toast.makeText(obj, "Tidak Ada data", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    obj.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(obj, "Tidak mendapat balasan dari server..", Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                }
                return  b;
            }

            @Override
            protected void onPostExecute(Boolean aVoid) {
                super.onPostExecute(aVoid);
                obj.runOnUiThread(new Runnable() {
                    public void run() {
                        if (dialog!=null&dialog.isShowing()){
                            dialog.dismiss();
                        }
                    }
                });


            }
        }.execute();


        return  a[0];

    }

    public boolean add(String name,String gender,String birthplace,String birthdate, Dialog dialogAlternatif) {

        boolean[] a = {false};
        new AsyncTask<Void, Void, Boolean>() {

            ProgressDialog dialog =new ProgressDialog(MainActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.setMessage("Loading insert data...");

                obj.runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.show();
                    }
                });
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                boolean b=false;

                FormBody.Builder formBody = new FormBody.Builder()
                        .add("name", name)
                        .add("gender", gender)
                        .add("birthplace", birthplace)
                        .add("birthdate", birthdate)
                        .add("action", "POST");
                try (Response response = new Api(MainActivity.this).
                        post(getString(R.string.api_user),formBody)) {

                    if (response == null || !response.isSuccessful())
                        throw new IOException("Unexpected code = " + response);

                    String responseBodyString = response.body().string();
                    JSONObject responseBodyObject = new JSONObject(responseBodyString);
                    if (responseBodyObject.getBoolean("status")) {

                        obj.runOnUiThread(new Runnable() {

                            public void run() {
                                Toast.makeText(obj, "Berhasil diupdate", Toast.LENGTH_SHORT).show();
//                                getRecordSchedule();
                                if (dialog!=null&dialog.isShowing()){
                                    dialog.dismiss();
                                }

                                dialogAlternatif.dismiss();

                                getRecordUser();
                            }
                        });
                    }else {
                        obj.runOnUiThread(new Runnable() {
                            public void run() {
                                if (responseBodyObject.has("message")){
                                    String message="";
                                    try {
                                        message=responseBodyObject.getString("message");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(obj, message, Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(obj, "Error Respon false", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    obj.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(obj, "Terjadi Respon error server", Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return  b;
            }

            @Override
            protected void onPostExecute(Boolean aVoid) {
                super.onPostExecute(aVoid);
                obj.runOnUiThread(new Runnable() {
                    public void run() {
                        if (dialog!=null&dialog.isShowing()){
                            dialog.dismiss();
                        }
                    }
                });


            }
        }.execute();


        return  a[0];

    }


    public boolean update(String id, String name,String gender,String birthplace,String birthdate, Dialog dialogAlternatif) {

        boolean[] a = {false};
        new AsyncTask<Void, Void, Boolean>() {

            ProgressDialog dialog =new ProgressDialog(MainActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.setMessage("Loading update data...");

                obj.runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.show();
                    }
                });
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                boolean b=false;

                FormBody.Builder formBody = new FormBody.Builder()
                        .add("id", id)
                        .add("name", name)
                        .add("gender", gender)
                        .add("birthplace", birthplace)
                        .add("birthdate", birthdate)
                        .add("action", "PUT");
                try (Response response = new Api(MainActivity.this).
                        post(getString(R.string.api_user),formBody)) {

                    if (response == null || !response.isSuccessful())
                        throw new IOException("Unexpected code = " + response);

                    String responseBodyString = response.body().string();
                    JSONObject responseBodyObject = new JSONObject(responseBodyString);
                    if (responseBodyObject.getBoolean("status")) {

                        obj.runOnUiThread(new Runnable() {

                            public void run() {
                                Toast.makeText(obj, "Berhasil diupdate", Toast.LENGTH_SHORT).show();
//                                getRecordSchedule();
                                if (dialog!=null&dialog.isShowing()){
                                    dialog.dismiss();
                                }

                                dialogAlternatif.dismiss();

                                getRecordUser();
                            }
                        });
                    }else {
                        obj.runOnUiThread(new Runnable() {
                            public void run() {
                                if (responseBodyObject.has("message")){
                                    String message="";
                                    try {
                                        message=responseBodyObject.getString("message");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(obj, message, Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(obj, "Error Respon false", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    obj.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(obj, "Terjadi Respon error server", Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return  b;
            }

            @Override
            protected void onPostExecute(Boolean aVoid) {
                super.onPostExecute(aVoid);
                obj.runOnUiThread(new Runnable() {
                    public void run() {
                        if (dialog!=null&dialog.isShowing()){
                            dialog.dismiss();
                        }
                    }
                });


            }
        }.execute();


        return  a[0];

    }

    public boolean delete(String id) {

        boolean[] a = {false};
        new AsyncTask<Void, Void, Boolean>() {

            ProgressDialog dialog =new ProgressDialog(MainActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.setMessage("Loading delete data...");

                obj.runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.show();
                    }
                });
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                boolean b=false;

                FormBody.Builder formBody = new FormBody.Builder()
                        .add("id", id)
                        .add("action", "DELETE");
                try (Response response = new Api(MainActivity.this).
                        post(getString(R.string.api_user),formBody)) {

                    if (response == null || !response.isSuccessful())
                        throw new IOException("Unexpected code = " + response);

                    String responseBodyString = response.body().string();
                    JSONObject responseBodyObject = new JSONObject(responseBodyString);
                    if (responseBodyObject.getBoolean("status")) {

                        obj.runOnUiThread(new Runnable() {

                            public void run() {
                                Toast.makeText(obj, "Berhasil dihapus", Toast.LENGTH_SHORT).show();
//                                getRecordSchedule();
                                if (dialog!=null&dialog.isShowing()){
                                    dialog.dismiss();
                                }

                                getRecordUser();
                            }
                        });
                    }else {
                        obj.runOnUiThread(new Runnable() {
                            public void run() {
                                if (responseBodyObject.has("message")){
                                    String message="";
                                    try {
                                        message=responseBodyObject.getString("message");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(obj, message, Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(obj, "Error Respon false", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    obj.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(obj, "Terjadi Respon error server", Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return  b;
            }

            @Override
            protected void onPostExecute(Boolean aVoid) {
                super.onPostExecute(aVoid);
                obj.runOnUiThread(new Runnable() {
                    public void run() {
                        if (dialog!=null&dialog.isShowing()){
                            dialog.dismiss();
                        }
                    }
                });


            }
        }.execute();


        return  a[0];

    }

    public interface ListenerDialog {
        void onClick(int i);
    }
}
