package com.example.testmysql;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    TextView text, errortext;
    Button show;
    String textParam = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        text = (TextView)findViewById(R.id.text1);
        errortext = (TextView)findViewById(R.id.text2);

        show = (Button)findViewById(R.id.button1);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Task().execute();
            }
        });

        text.setMovementMethod(new ScrollingMovementMethod());

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();

        textParam = ""+i;
        new Task().execute();

        }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }

    class Task extends AsyncTask<Void, Void, Void>{
        String records="", error="";

        @Override
        protected Void doInBackground(Void... voids){
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.86:3306/sms", "RemoteUser", "1q2w3e4r");
                Statement statement = connection.createStatement();

                //String SelectString = "SELECT * FROM sms";;
                String SelectString = "SELECT * FROM sms where month = '" + textParam + "'";

                //error = SelectString;
                ResultSet resultSet = statement.executeQuery(SelectString);

                while (resultSet.next()){
                    records +=
                                    "подразделения - " +resultSet.getString(2) + "\n" +
                                    "физ.лица            \t\t\t\t\t\t\t\t\t\t\t\t        =\t" + resultSet.getString(3) + "\n" +
                                    "подписки          \t\t\t\t\t\t\t\t\t\t\t\t          =\t" +resultSet.getString(4) + "\n" +
                                    "физ.лиц/подписки   \t\t\t\t\t\t\t\t\t\t\t\t=\t" + resultSet.getString(5) + "%" + "\n" +
                                    "кол-во отправлений\t\t\t\t\t\t\t\t\t\t\t\t=\t" + resultSet.getString(6) + "\n" +
                                    "----------------------------------------------------------------------------------" + "\n"

                    ;

                }
            }
            catch (Exception ex){
                error = ex.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            text.setText(records);
            if (error != "")
                errortext.setText(error);
            super.onPostExecute(aVoid);
        }
    }
}