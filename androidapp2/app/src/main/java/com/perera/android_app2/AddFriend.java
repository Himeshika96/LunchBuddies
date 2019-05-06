package com.perera.android_app2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.Serializable;

import cz.msebera.android.httpclient.Header;

public class AddFriend extends AppCompatActivity {

    private Button addButton;
    private EditText fName;
    private EditText sName;
    private EditText phone;

    FriendModel friendObj;

    private String URL = "https://peaceful-mountain-19289.herokuapp.com/friend/";
    AsyncHttpClient client;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_add_layout);

        friendObj= (FriendModel) getIntent().getSerializableExtra("id");


        widgetInitialize();

        if(!friendObj.get_id().equals("0")){
            setValues(friendObj);
        }
        buttonOnClickListners();




    }

    private void setValues(FriendModel fObj) {

        fName.setText(fObj.getFirstName());
        sName.setText(fObj.getSecondName());
        phone.setText(fObj.getPhone());




    }


    public void widgetInitialize() {
        addButton = findViewById(R.id.addButton);
        fName = findViewById(R.id.firstName);
        sName = findViewById(R.id.secondName);
        phone = findViewById(R.id.phone);
    }



    public void buttonOnClickListners() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               String fname = fName.getText().toString();
               String sname = sName.getText().toString();
               String tel = phone.getText().toString();

                RequestParams params = new RequestParams();
                params.put("firstName",fname);
                params.put("secondName",sname);
                params.put("phone",tel);

             //  friendObj = new FriendModel(fname,sname,tel);
                if(!friendObj.get_id().equals("0")){

                    updateFriend(params,friendObj.get_id());
                }
                else
                    addFriend(params);

                stopService(new Intent(AddFriend.this, MainActivity.class));
                finish();

            }
        });
    }

    private void updateFriend(RequestParams params,String id) {

        Log.d("friend", "inside in the UPDATE method");
        client = new AsyncHttpClient();
        client.put(URL+id, params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("friend", errorResponse.toString());
            }
        });

    }


    public void  addFriend(RequestParams params){

        Log.d("friend", "inside in the add friend method");
        client = new AsyncHttpClient();
        client.post(URL, params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("friend", errorResponse.toString());
            }
        });


    }


}



