package edu.cmu.hcii.emolight.tracking;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;
import android.os.AsyncTask;

import com.bezirk.hardwareevents.HexColor;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentEmotion;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.hcii.emolight.Const;
import edu.cmu.hcii.emolight.emotion.EmotionUtils;
import edu.cmu.hcii.emolight.emotion.NLP;

/**
 * @author toby
 * @date 10/29/16
 * @time 11:11 AM
 */
public class EventHandler {

    public String currentTitle;
    Context context;
    LightHandler lightHandler;

    public EventHandler(Context context){
        currentTitle = new String("");
        this.context = context;
        lightHandler = new LightHandler(context);
    }

    /**
     * event handler should handle the event, extract the title if at the correct activity, and send the title to the semantics processor
     * @param event
     */
    public void handle(AccessibilityEvent event, AccessibilityNodeInfo rootNode){
        if(event == null || rootNode == null)
            return;

        List<AccessibilityNodeInfo> filteredList = filterList(preOrderTraverse(rootNode));
        if(filteredList.size() == 0)
            return;

        if(filteredList.size() > 1){
            //TODO: do something
            System.out.println("WARNING: GET A FILTERED LIST OF SIZE " + filteredList.size());
        }

        AccessibilityNodeInfo selectedNode = filteredList.get(0);
        String title = new String(selectedNode.getText().toString());
        Rect selectedNodeBoundingBox = new Rect();
        selectedNode.getBoundsInScreen(selectedNodeBoundingBox);


        if(!title.contentEquals(currentTitle)){
            //title updated
            //TODO: output bounding box + width
            currentTitle = title;
            /*
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Found Document Title!")
                    .setMessage(title + "\n"
                    + "bounding box: " + selectedNodeBoundingBox.toShortString() + "\n"
                    + "width: " + selectedNodeBoundingBox.width())
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            Dialog dialog = builder.create();
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
            dialog.show();
            */
            Toast.makeText(context, "EmoLight: " + title, Toast.LENGTH_SHORT).show();

            new LongOperation().execute(title);

        }

    }

    private class LongOperation extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String title = params[0];
            DocumentEmotion.Emotion emotion = NLP.v().getEmotion(title);
            if (emotion == null) return null;

            String color = EmotionUtils.emotion2Color2(emotion);
            if (color == null) return null;

            System.out.println(String.format("text: %s\nemotion:\n%s\ncolor: %s\n",
                    title,
                    EmotionUtils.emotion2String(emotion),
                    color));
            lightHandler.sendColorChangeEvent(new HexColor(color));
            return null;
        }


    }


    //the title should have id: com.google.android.youtube:id/title


    /**
     * traverse a tree from the root, and return all the notes in the tree
     * @param root
     * @return
     */
    private List<AccessibilityNodeInfo> preOrderTraverse(AccessibilityNodeInfo root){
        if(root == null)
            return null;
        List<AccessibilityNodeInfo> list = new ArrayList<>();
        list.add(root);
        int childCount = root.getChildCount();
        for(int i = 0; i < childCount; i ++){
            AccessibilityNodeInfo node = root.getChild(i);
            if(node != null)
                list.addAll(preOrderTraverse(node));
        }
        return list;
    }

    private List<AccessibilityNodeInfo> filterList(List<AccessibilityNodeInfo> originalList){
        List<AccessibilityNodeInfo> retList = new ArrayList<>();
        for(AccessibilityNodeInfo node : originalList){
            if(filter(node))
                retList.add(node);
        }
        return retList;
    }

    private boolean filter (AccessibilityNodeInfo node){
        //TODO: not hard code the string in the source code
        if(node.getText() == null)
            return false;

        Rect nodeBoundingBox = new Rect();
        node.getBoundsInScreen(nodeBoundingBox);

        //youtube
        if(node.getPackageName().toString().contentEquals("com.google.android.youtube")) {
            //youtube
            if (node.getViewIdResourceName() != null &&
                    node.getViewIdResourceName().contentEquals("com.google.android.youtube:id/title") &&
                    nodeBoundingBox.width() >= Const.YOUTUBE_MAIN_TITLE_THRESHOLD)
                return true;
        }

        else {
            if(node.getPackageName().toString().contentEquals("com.musixmatch.android.lyrify")){
                //lyrify
                if(nodeBoundingBox.width() > Const.MUSIXMATCH_WIDTH_THRESHOLD && nodeBoundingBox.height() > Const.MUSIXMATCH_HEIGHT_THRESHOLD)
                    return true;
            }
        }

        return false;
    }

}
