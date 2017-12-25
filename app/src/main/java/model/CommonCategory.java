package model;

import android.app.Activity;
import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Hari Prahlad on 28-09-2016.
 */
public class CommonCategory {
    public List<AllCommonClass> arrayList = new ArrayList<AllCommonClass>();
    public Activity activity;
    private String URL= "http://walletbaba.com/assets/images/";
    public CommonCategory(List<AllCommonClass> arrayList){
    this.arrayList = arrayList;
    }
}
