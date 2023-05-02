package edu.unitbv.ratbv;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.graphics.Color;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {
    private Button b;
    private ImageView iv;
    private RelativeLayout l;
    private LinearLayout ll;
    private TextView tw, tw1;
    private HorizontalScrollView hsv;
    private Drawable d;private View v1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        getSupportActionBar().setTitle("R.A.T Brașov");getSupportActionBar().hide();
       // getSupportActionBar().setBackgroundDrawable((Drawable) "D:\\AndroidStudio\\RATBV\\app\\src\\main\\res\drawable\\band.jpg");
        tw = (TextView) findViewById(R.id.DateBottom);
        tw.setText("Call Center: 0368-600-800");
        tw.setFontFeatureSettings("sans-serif-condensed-medium");
        l = (RelativeLayout) findViewById(R.id.Full);
        l.setBackgroundColor(Color.parseColor("#d6fcff"));
        iv=(ImageView)findViewById(R.id.bground);iv.setBackgroundColor(Color.parseColor("#FFFFFF"));
        tw1=(TextView)findViewById(R.id.trmet);tw1.setText("Transport Metropolitan");
        tw1.setBackgroundColor(Color.parseColor("#FFFFFF"));
        tw1.setTextColor(Color.parseColor("#000000"));
        tw1.setFontFeatureSettings("sans-serif-condensed-medium");
        b = (Button) findViewById((R.id.StartButton));
      //  b.setFontFeatureSettings("sans-serif-condensed-medium");
        b.setBackgroundColor(Color.parseColor("#FFFFFF"));
        b.setTextColor(Color.parseColor("#3f818f"));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMapsActivity();
            }//apeleaza metoda openMapsActivity() de mai jos
        });

    }
    public void openMapsActivity() {//Comuta pe activitatea 2
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

}


