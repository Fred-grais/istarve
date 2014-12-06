package fr.utt.if26.istarve.interfaces;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Fred-Dev on 28/11/2014.
 */
public interface OnTaskCompleted {
    void onTaskCompleted(JSONObject json);
    void onTaskCompleted(JSONArray json);
    void onTaskCancelled();
    void onTaskFailed(JSONObject json);
    void onTaskFailed(JSONArray json);
}
