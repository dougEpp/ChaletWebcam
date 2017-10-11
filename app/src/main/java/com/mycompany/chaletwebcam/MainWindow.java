package com.mycompany.chaletwebcam;

import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;


public class MainWindow extends Activity {

    Button load_img_front;
    Button load_img_hottub;
    Button load_img_snow;
    ImageView img;
    Bitmap bitmap;
    ProgressDialog pDialog;
    String dateString;
    int year;
    int month;
    int day;
    int hour;
    int minute;
    String photoURL;
    TextView dateBox;
    Calendar date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);

        //get elements of current date and time
        date = Calendar.getInstance();
        year = date.get(Calendar.YEAR);
        month = date.get(Calendar.MONTH) + 1;
        day = date.get(Calendar.DAY_OF_MONTH);
        hour = date.get(Calendar.HOUR_OF_DAY);
        minute = date.get(Calendar.MINUTE);

        //generate current photo URL
        photoURL = "http://www.neudoerffer.com/chaletwebcam/pictures/" + year;
        if (month < 10)
        {
            photoURL += "0" + month;
        }
        else
        {
            photoURL += month;
        }
        if (day < 10 )
        {
            photoURL += "0" + day;
        }
        else
        {
            photoURL += day;
        }

        photoURL += "_";

        if (hour < 10)
        {
            photoURL += "0" + hour;
        }
        else
        {
            photoURL += hour;
        }
        photoURL += "00_";

        //display current date/time
        //dateString = "Year: " + year + "\nMonth: " + month + "\nDay: " + day + "\nHour: " + hour;
        //dateString += "\n" + photoURL;
        //dateString = "";
        if (hour < 10)
        {
            if (minute < 10) {
                dateString = year + "/" + month + "/" + day + ", 0" + hour + ":0" + minute;
            } else {
                dateString = year + "/" + month + "/" + day + ", 0" + hour + ":" + minute;
            }
        }
        else
        {
            if (minute < 10) {
                dateString = year + "/" + month + "/" + day + ", " + hour + ":0" + minute;
            } else {
                dateString = year + "/" + month + "/" + day + ", " + hour + ":" + minute;
            }

        }
        //dateString += "\n" + photoURL;
        dateBox = (TextView)findViewById(R.id.dateBox);
        dateBox.setText(dateString);

        //find buttons and ImageView
        load_img_front = (Button)findViewById(R.id.loadFront);
        load_img_hottub = (Button)findViewById(R.id.loadHottub);
        load_img_snow = (Button)findViewById(R.id.loadStake);
        img = (ImageView)findViewById(R.id.imgFront);

        //Set the image to "Front" by default
        new LoadImage().execute(photoURL + "front.jpg");

        load_img_front.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                //TODO Auto-generated method stub
                new LoadImage().execute(photoURL + "front.jpg");
            }
        });
        load_img_hottub.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0) {
                new LoadImage().execute(photoURL + "hottub.jpg");
            }
        });
        load_img_snow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0) {
                new LoadImage().execute(photoURL + "snow.jpg");
            }
        });
    }
    private class LoadImage extends AsyncTask<String, String, Bitmap>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainWindow.this);
            pDialog.setMessage("Loading Image ....");
            pDialog.show();
        }
        protected Bitmap doInBackground(String... args)
        {
            try
            {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return bitmap;
        }
        protected void onPostExecute(Bitmap image)
        {
            if(image !=null)
            {
                img.setImageBitmap(image);
                pDialog.dismiss();
            }
            else
            {
                pDialog.dismiss();
                Toast.makeText(MainWindow.this, "Image does not exist or network error", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_window, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
//http://www.learn2crack.com/2014/06/android-load-image-from-internet.html