package edu.cmu.hcii.emolight;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

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

    @Override
    public void onCreate(){
        super.onCreate();

        eventHandler = new EventHandler(this);

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
        if(!appPackageNameToHandle.contains(event.getPackageName())){
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

}
