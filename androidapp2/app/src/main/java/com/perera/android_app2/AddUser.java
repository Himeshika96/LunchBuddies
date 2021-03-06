/**
* Who's Hungry android application
* Authors - IT16067134 & IT16058910
* CTSE pair project
* Android Project
*/

package com.perera.android_app2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;

public class AddUser extends AppCompatActivity {


    EditText uname;
    EditText uemail;
    EditText ucontactNo;
    Button usave;
    Button uremove;
    TextView toolbartxt;
    Animation fromBottom;
    UserModel userobj;
    AsyncHttpClient client;
    private String URL = "https://peaceful-mountain-19289.herokuapp.com/user/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofile);

        toolbartxt = findViewById(R.id.textView);
        toolbartxt.setText("User Details");

        uname = (EditText) findViewById(R.id.userprofilename);
        uemail = (EditText) findViewById(R.id.userprofileemail);
        ucontactNo = (EditText) findViewById(R.id.userprofilecontactNo);
        usave = (Button) findViewById(R.id.userprofilesave);
        uremove = (Button) findViewById(R.id.userprofileremoveaccount);

        fromBottom = AnimationUtils.loadAnimation(this,R.anim.from_bottom);

        usave.setAnimation(fromBottom);
        uremove.setAnimation(fromBottom);

        Log.d("user", "calling get");
        getUser();

        usave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    Log.d("user", userobj.getName());
                    Log.d("user", userobj.getEmail());
                    Log.d("user", userobj.getContactNo());

                    String name = userobj.getName();
                    String email = userobj.getEmail();
                    String phone = userobj.getContactNo();
                    String id = userobj.get_id();

                    Log.d("user", name);
                    Log.d("user", email);
                    Log.d("user", phone);
                    Log.d("user",id);

                    RequestParams params = new RequestParams();
                    params.put("name", name);
                    params.put("email", email);
                    params.put("phone", phone);

                    Log.d("user", "calling update");



                    updateUser(params, id);
                    savemsg();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Log.d("user", e.toString());
                }


            }
        });

        uremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removealert();

            }
        });

    }


    /**
     * toast to appear when clicking the save button
     */
    public void savemsg(){

        Toast.makeText(this,"Successfully Updated!",Toast.LENGTH_LONG).show();
    }


    /**
     * toast to appear when giving a wrong email
     */
    public void emailmsg(){

        Toast.makeText(this,"Incorrect Email!",Toast.LENGTH_LONG).show();
    }

    /**
     * toast to appear when the user removed successfully
     */
    public void removemsg(){
        Toast.makeText(this,"Successfully Removed!",Toast.LENGTH_LONG).show();
    }



    /**
     *method to display an alert when the user wants to remove the account
     */
    public void removealert(){


        AlertDialog.Builder builder = new AlertDialog.Builder(AddUser.this);
        builder.setMessage("Do you want to delete account?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                try {
                    Log.d("user", userobj.getName());
                    Log.d("user", userobj.getEmail());
                    Log.d("user", userobj.getContactNo());

                    String name = userobj.getName();
                    String email = userobj.getEmail();
                    String phone = userobj.getContactNo();
                    String id = userobj.get_id();

                    Log.d("user", name);
                    Log.d("user", email);
                    Log.d("user", phone);
                    Log.d("user",id);

                    RequestParams params = new RequestParams();
                    params.put("name", name);
                    params.put("email", email);
                    params.put("phone", phone);

                    Log.d("user", "calling remove");

                    removeUser(params,id);

                    removemsg();

                    Intent intent = new Intent(AddUser.this, Login.class);
                    startActivity(intent);



                }
                catch (Exception e) {
                    e.printStackTrace();
                    Log.d("user", e.toString());
                }


            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    /**
     *method to get user
     */
    private void getUser(){
        Log.d("user", "inside in the get data");
        client = new AsyncHttpClient();

        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("user", response.toString());
                Log.d("user", "onSuccess");
                super.onSuccess(statusCode, headers, response);
                try{
                    JSONArray jArray = response.getJSONArray("data");
                    Log.d("user", "inside array");

                    if(jArray.isNull(0)){
                        stopService(new Intent(AddUser.this, Register.class));
                        finish();
                    }

                    for(int i=0;i<jArray.length();i++)
                        try {

                            JSONObject obj = jArray.getJSONObject(i);
                            userobj= new UserModel(
                                    obj.getString("_id"),
                                    obj.getString("name"),
                                    obj.getString("email"),
                                    obj.getString("phone"));

                            Log.d("user", userobj.getName());
                            Log.d("user", userobj.getEmail());
                            Log.d("user", userobj.getContactNo());

                            uname.setText(userobj.getName());
                            uemail.setText(userobj.getEmail());
                            ucontactNo.setText(userobj.getContactNo());


                           //userList.add(userobj);

                           //Log.d("user", userList.toString() );

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("user", e.toString());
                        }




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("friend", "fail to get request");
            }
        });
    }



    /**
     *
     * @param params
     * @param id
     * method to update the user
     */
    private void updateUser(RequestParams params, String id){

        Log.d("user", "inside in the update data");
        Log.d("user",id);

        try {
            client = new AsyncHttpClient();
            client.put(URL + id, params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("user", "onSuccess");
                    super.onSuccess(statusCode, headers, response);


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("user", "onFailure");
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.d("user", errorResponse.toString());
                }

            });
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }



    /**
     *
     * @param params
     * @param id
     * method to remove user
     */
    private void removeUser(RequestParams params, String id){

        Log.d("user", "inside in the remove data");
        Log.d("user",id);

        client = new AsyncHttpClient();
        client.delete(URL+id,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                getUser();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }



}
