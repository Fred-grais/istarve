package fr.utt.if26.istarve.interfaces;

import org.json.JSONArray;
import org.json.JSONObject;

public interface OnTaskCompleted {
    void onTaskCompleted(JSONObject json);
    void onTaskCompleted(JSONArray json);
    void onTaskCancelled();
    void onTaskFailed(JSONObject json);
    void onTaskFailed(JSONArray json);
}
