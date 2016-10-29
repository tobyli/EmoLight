package edu.cmu.hcii.emolight.tracking;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.bezirk.hardwareevents.HexColor;
import com.bezirk.hardwareevents.light.Light;
import com.bezirk.hardwareevents.light.SetLightColorEvent;
import com.bezirk.middleware.Bezirk;
import com.bezirk.middleware.android.BezirkMiddleware;

/**
 * Created by fanglinchen on 10/29/16.
 */

public class LightHandler {

    Context context;
    Bezirk bezirk;

    // hexColor could be new HexColor("#FFFF00")
    public void sendColorChangeEvent(HexColor hexColor){
        Light light = new Light("1", "philips.hue.bulb.color");

        bezirk.sendEvent(new SetLightColorEvent(light, hexColor));

        System.out.println("Send event");
    }

    public LightHandler(Context context){
        BezirkMiddleware.initialize(context,"EmoLight");

        //Register with BezirkMiddleware to get an instance of Bezirk API.
        //The parameter is any human-readable string for a name of your Zirk
        bezirk = BezirkMiddleware.registerZirk("Hello World Zirk");

        this.context = context;
    }
}
