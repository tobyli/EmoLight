package edu.cmu.hcii.emolight.tracking;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.bezirk.adapter.philips.hue.PhilipsHueAdapter;
import com.bezirk.hardwareevents.HexColor;
import com.bezirk.hardwareevents.light.CurrentLightStateEvent;
import com.bezirk.hardwareevents.light.GetLightStateEvent;
import com.bezirk.hardwareevents.light.Light;
import com.bezirk.hardwareevents.light.LightsDetectedEvent;
import com.bezirk.hardwareevents.light.SetLightBrightnessEvent;
import com.bezirk.hardwareevents.light.SetLightColorEvent;
import com.bezirk.hardwareevents.light.TurnLightOffEvent;
import com.bezirk.hardwareevents.light.TurnLightOnEvent;
import com.bezirk.middleware.Bezirk;
import com.bezirk.middleware.addressing.ZirkEndPoint;
import com.bezirk.middleware.android.BezirkMiddleware;
import com.bezirk.middleware.messages.Event;
import com.bezirk.middleware.messages.EventSet;

import java.net.MalformedURLException;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static okhttp3.internal.Internal.logger;

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
        init(context);
        this.context = context;
    }

    public void init(Context context) {
        BezirkMiddleware.initialize(context, "EmoLight");

        //Register with BezirkMiddleware to get an instance of Bezirk API.
        //The parameter is any human-readable string for a name of your Zirk
        bezirk = BezirkMiddleware.registerZirk("Hello World Zirk");

        final EventSet lightEvents;
        lightEvents = new EventSet(LightsDetectedEvent.class,
                CurrentLightStateEvent.class);

        lightEvents.setEventReceiver(new EventSet.EventReceiver() {
            @Override
            public void receiveEvent(Event event, ZirkEndPoint sender) {
                if (event instanceof LightsDetectedEvent) {
                    Set<Light> lights = ((LightsDetectedEvent) event).getLights();

                    for (final Light light : lights) {
                        System.out.println("Found light: " + light.toString());

                        bezirk.sendEvent(new GetLightStateEvent(light));
                        bezirk.sendEvent(new TurnLightOnEvent(light));
                        bezirk.sendEvent(new SetLightColorEvent(light, new HexColor("#00FF00")));
                        bezirk.sendEvent(new SetLightBrightnessEvent(light, 125));

                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                bezirk.sendEvent(new TurnLightOffEvent(light));
                            }
                        }, 2000);
                    }
                } else if (event instanceof CurrentLightStateEvent) {
                    CurrentLightStateEvent lightState = (CurrentLightStateEvent) event;
                    logger.info(lightState.toString());
                }
            }
        });

        bezirk.subscribe(lightEvents);

        try {
            Set<String> hueBridges = PhilipsHueAdapter.discoverHueBridges();
            new PhilipsHueAdapter(bezirk, hueBridges.toArray(new String[hueBridges.size()])[0],
                    "oFZsQakh9XzQiVhkIuuv83xsycRsmfgcEn5eBvjm");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
