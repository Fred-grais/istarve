package fr.utt.if26.istarve.interfaces;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Interface used to place the apporpriate callbacks on Activity performing Asynchronous calls
 */
public interface OnTaskCompleted {
    void onTaskCompleted(JSONObject json);
    void onTaskCompleted(JSONArray json);
    void onTaskCancelled();
    void onTaskFailed(JSONObject json);
    void onTaskFailed(JSONArray json);
}
