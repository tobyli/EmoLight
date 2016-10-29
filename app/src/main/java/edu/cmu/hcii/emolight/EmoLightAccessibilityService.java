package edu.cmu.hcii.emolight;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.bezirk.middleware.Bezirk;
import com.bezirk.middleware.android.BezirkMiddleware;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import edu.cmu.hcii.emolight.tracking.EventHandler;
/**
 * @author toby
 * @date 10/29/16
 * @time 10:53 AM
 */
public class EmoLightAccessibilityService extends AccessibilityService {


    EventHandler eventHandler;


    Set<Integer> accessibilityEventToHandle;
    Set<String> appPackageNameToHandle;
    StatusIconManager statusIconManager;

    @Override
    public void onCreate(){
        super.onCreate();

        //Initialize the Bezirk service


        eventHandler = new EventHandler(this);
//        lightHandler = new LightHandler(this,bezirk);

        statusIconManager = new StatusIconManager(this, eventHandler);

        try {
            Toast.makeText(this, "EmoLight Accessibility Service Started", Toast.LENGTH_SHORT).show();
            statusIconManager.addStatusIcon();
        }
        catch (Exception e){
            e.printStackTrace();
            //do nothing
        }

        accessibilityEventToHandle = new HashSet<>(Arrays.asList(Const.ACCESSIBILITY_EVENT_TO_HANDLE));
        appPackageNameToHandle = new HashSet<>(Arrays.asList(Const.APP_PACKAGE_NAME_TO_HANDLE));
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();

        //return if the event is not among the accessibilityEventArrayToHandle
        if(!accessibilityEventToHandle.contains(Integer.valueOf(event.getEventType()))) {
            return;
        }

        //return if the event is not among the apps we handle
        if(event.getPackageName() != null && (!appPackageNameToHandle.contains(event.getPackageName()))){
            return;
        }

        eventHandler.handle(event, rootNode);

    }

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onServiceConnected(){
        super.onServiceConnected();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Toast.makeText(this, "EmoLight Accessibility Service Stopped", Toast.LENGTH_SHORT).show();
        if(statusIconManager != null)
            try {
                statusIconManager.removeStatusIcon();
            }
            catch (Exception e){
                //failed to remove status icon
                e.printStackTrace();
            }

    }

}
