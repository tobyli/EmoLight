package edu.cmu.hcii.emolight;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

import java.util.Set;

/**
 * @author toby
 * @date 10/29/16
 * @time 10:53 AM
 */
public class EmoLightAccessibilityService extends AccessibilityService {

    Set<String> accessibilityEventToHandle;
    Set<String> appPackageNameToHandle;

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onServiceConnected(){
        super.onServiceConnected();
    }

}
