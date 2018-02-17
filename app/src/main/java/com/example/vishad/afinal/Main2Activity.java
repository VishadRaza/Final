package com.example.vishad.afinal;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Main2Activity extends AppCompatActivity {

    TextView tday;
    TextView yday;
    TextView thisweekday;
    TextView lastweekday;
    String number;
    String s="";
    String [] num;
    String []  datte;
    SharedPreferences prefs;
    ArrayAdapter arrayAdapter;
    TextView textView;
    int value;
    private static final String TAG = Main2Activity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
        String yesterdayAsString = dateFormat.format(cal.getTime());
        Log.v(TAG,"Value of yesterday date from onCreate is : "+yesterdayAsString);

        tday = (TextView) findViewById(R.id.tday);
        yday = (TextView) findViewById(R.id.yday);
        thisweekday = (TextView) findViewById(R.id.thisweekday);
        lastweekday = (TextView) findViewById(R.id.lastweekday);
        // textView = (TextView) findViewById(R.id.unit);

        handleUserInteraction();

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        s=prefs.getString("number",null);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        Log.v("savedNumber"," "+s);

        number = getIntent().getStringExtra("MyNumber");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("ddMMMyyyy");
        String strDate = "Current Date : " + mdformat.format(calendar.getTime());

        Log.v(TAG,"Current: "+strDate);

        Log.v( "friday" ,"" +number);
        refreshSmsInbox();

    }

    public void refreshSmsInbox() {
        int rows = 0;
        double sum = 0;
        double sumyesterday = 0;
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxcursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxcursor.getColumnIndex("body");
        int indexAddress = smsInboxcursor.getColumnIndex("address");
        String i = s;
        Log.v("hel" ,"msg" +i);
        //    String check = "00923362503565";

        if (indexBody < 0 || !smsInboxcursor.moveToFirst())
            return;
        do{
            String number = smsInboxcursor.getString(indexAddress);
            if (number.equals(i)) {

                int dateIndex = smsInboxcursor.getColumnIndex("date");
                Log.v("dateIndex", "" + dateIndex);

                String date = smsInboxcursor.getString(smsInboxcursor.getColumnIndex("date"));
                Long timestamp = Long.parseLong(date);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(timestamp);

                Date finaldate = calendar.getTime();
                String smsDate = finaldate.toString();
                Log.v("act", "" + smsDate);

                datte = smsDate.split(" ");
                Log.v("Day", "" + datte[0]);
                Log.v("Month", "" + datte[1]);
                Log.v("Date", "" + datte[2]);
                Log.v("Time", "" + datte[3]);

                String da = datte[2].concat(datte[1]);
                String datecomplete = da.concat(datte[5]);
                Log.v("da",""+datecomplete);
                // num = smsInboxcursor.getString(indexBody).split(": ");
                Log.v(TAG, smsInboxcursor.getString(indexBody));

                //   Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("ddMMMyyyy");
                String strDate = "Current Date : " + mdformat.format(calendar.getTime());

                Log.v(TAG,"Current: "+strDate);

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("ddMMMyyyy");
                String formattedDate = df.format(c);
                Log.v("c",""+formattedDate);

                calendar.add(Calendar.DATE , -1 );
                String z = mdformat.format(calendar.getTime());
                //  int k = Integer.parseInt(formattedDate);
                //int q= k-1;
                //String z= String.valueOf(k);
                Log.v("last",""+z);

                String[] s = strDate.split(" ");
                Log.v("s1",""+s[0]);
                Log.v("s2",""+s[1]);

               // Log.v("ds",""+formattedDate);
               String def = "07Feb2018";
                if(datecomplete.equals(formattedDate)) {

                    String[] value = smsInboxcursor.getString(indexBody).split(": ");
                    Log.v("value",""+value[1]);
                    sum += Double.parseDouble(value[1]);

                }

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE,-1);
                DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
                String yesterdayAsString = dateFormat.format(cal.getTime());
                Log.v("dq",""+yesterdayAsString);

                if(datecomplete.equals(yesterdayAsString)){
                    String[] value = smsInboxcursor.getString(indexBody).split(": ");
                    Log.v("yesterday",""+value[1]);
                    sumyesterday +=Double.parseDouble(value[1]);
                }
                   /* if(Integer.parseInt(formattedDate) - Integer.parseInt(datecomplete)   == 1 ){
                        String[] value = smsInboxcursor.getString(indexBody).split(": ");
                        Log.v("yesterday",""+value[1]);
                        sumyesterday +=Double.parseDouble(value[1]);
                    }*/

                /*  if(datecomplete.equals(z)){

                       String[] value = smsInboxcursor.getString(indexBody).split(": ");
                       Log.v("yesterday",""+value[1]);
                       sumyesterday +=Double.parseDouble(value[1]);
                   }*/
            }
            String sum1 = String.valueOf(sum);
            String ans = String.format("%.2f", sum);
            Log.v(TAG, sum1);
            tday.setText(ans);

            String sumyester = String.valueOf(sumyesterday);
            Log.v("yes",""+sumyester);
            String an = String.format("%.2f",sumyesterday);
            yday.setText(an);


        }
        while (smsInboxcursor.moveToNext() );

    }
    private void handleUserInteraction() {

    }

    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
}
