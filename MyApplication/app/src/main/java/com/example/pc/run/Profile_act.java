package com.example.pc.run;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.FriendTabs.Review_learner;
import com.example.pc.run.FriendTabs.Review_teacher;
import com.example.pc.run.Global.GlobalBitmap;
import com.example.pc.run.Global.GlobalProfile;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.Objects.Profile;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profile_act extends AppCompatActivity {

    TextView name;
    TextView interests;
    TextView languagesKnown;
    TextView languagesLearning;
    ImageView profileImage;
    Bitmap bitmap;
    String email;
    String url = "http://t-simkus.com/run/pullProfile.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_act);

        email = getIntent().getStringExtra("email");
        Log.d("PROFILE EMAIL:", email);

        name = (TextView) findViewById(R.id.nameView);
        interests = (TextView) findViewById(R.id.interestsView);
        languagesKnown = (TextView) findViewById(R.id.languagesKnownView);
        languagesLearning = (TextView) findViewById(R.id.languagesLearningView);
        profileImage = (ImageView) findViewById(R.id.profileImageView);

        getProfileInfo();
    }


    public void getProfileInfo(){

        Map<String, String> parameters = new HashMap<>();
        parameters.put("email", email);

        Requests jsObjRequest = new Requests(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println(response.toString());
                    processResult(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
            }
        });
        ApplicationSingleton.getInstance().addToRequestQueue(jsObjRequest);
    }

    private void processResult(JSONObject input) throws JSONException {

        JSONArray userInfo = input.getJSONArray("result");
        JSONObject current = userInfo.getJSONObject(0);

        Log.d("NAME:", current.getString("name"));
        Log.d("Interests::", current.getString("interests"));
        Log.d("Languages Known:", current.getString("languagesKnown"));
        Log.d("Languages Learning:", current.getString("languagesLearning"));
        Log.d("BITMAP STRING:", current.getString("photo"));

        name.setText("Name: " + current.getString("name"));
        interests.setText("Interests:" + current.getString("interests"));
        languagesKnown.setText("Languages known:" + current.getString("languagesKnown"));
        languagesLearning.setText("Languages learning: " + current.getString("languagesLearning"));

        if(current.getString("photo") != null) {
            Log.d("PROFILE BITMAP:", current.getString("photo"));
            byte[] decodedByte = Base64.decode(current.getString("photo"), 0);
            bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
            profileImage.setImageBitmap(bitmap);
        }

        if(current.getString("name") != null) {
            System.out.println("SUCCESSFUL");
        }else{
            System.out.println("UNSUCCESSFUL");
        }
    }

    public void seeReviews(View view){
        Intent intent = new Intent(Profile_act.this, ReviewList_act.class);
        intent.putExtra("email", email);
//        Bundle b = new Bundle();
//        b.putString("email", email);
//        Review_learner rl = new Review_learner();
//        Review_teacher rt = new Review_teacher();
//        rl.setArguments(b);rt.setArguments(b);
        startActivity(intent);
    }

}
