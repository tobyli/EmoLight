package edu.cmu.hcii.emolight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.bezirk.middleware.*;

import android.view.View;
import android.widget.TextView;
import com.bezirk.hardwareevents.light.SetLightColorEvent;
import com.bezirk.hardwareevents.light.Light;
import com.bezirk.hardwareevents.HexColor;
import com.bezirk.middleware.android.BezirkMiddleware;

public class MainActivity extends AppCompatActivity {
    private Bezirk bezirk;
    private TextView statusTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statusTextView = (TextView) findViewById(R.id.statusTextView);

//        //Initialize the Bezirk service
//        BezirkMiddleware.initialize(this,"EmoLight");
//
//        //Register with BezirkMiddleware to get an instance of Bezirk API.
//        //The parameter is any human-readable string for a name of your Zirk
//        bezirk = BezirkMiddleware.registerZirk("Hello World Zirk");

        statusTextView.setText("BezirkMiddleware has been initialized");
    }

    // hexColor could be new HexColor("#FFFF00")
    void sendColorChangeEvent(HexColor  hexColor){
        Light light = new Light("1", "philips.hue.bulb.color");

        bezirk.sendEvent(new SetLightColorEvent(light, hexColor));

        System.out.println("Send event");
    }

}
