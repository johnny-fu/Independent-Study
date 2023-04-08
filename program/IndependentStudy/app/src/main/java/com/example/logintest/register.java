package com.example.logintest;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    private Button b_new, back;
    private EditText acc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retail_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViews();
    }

    private void findViews() {
        b_new = (Button) findViewById(R.id.b_new);
        back = (Button) findViewById(R.id.button9);
        acc = (EditText) findViewById(R.id.acc);
        b_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (acc.getText().toString().length() == 0) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(register.this);
                    alertDialog.setTitle("燈燈");
                    alertDialog.setMessage("帳號輸入為空");
                    alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(register.this);
                    alertDialog.setTitle("警告!");
                    alertDialog.setMessage("確定要創建新顧客嗎?!");
                    alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String url = php_con.login_url+"check_creat_account.php";
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                if (response.substring(0, 3).equals("<br")) {
                                                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                                    requestQueue.add(php_con.new_account(acc.getText().toString()));
                                                    Intent intent = new Intent();
                                                    intent.setClass(register.this, retail.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                } else {
                                                    JSONArray jsonArray = new JSONArray(String.valueOf(response));
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            AlertDialog.Builder alertDialog =
                                                                    new AlertDialog.Builder(register.this);
                                                            alertDialog.setTitle("燈燈");
                                                            alertDialog.setMessage("此帳號重複囉~!!");
                                                            alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                }
                                                            });
                                                            alertDialog.setCancelable(false);
                                                            alertDialog.show();
                                                        }
                                                    });
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(register.this, "error", Toast.LENGTH_LONG).show();
                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> data = new HashMap<>();
                                            data.put("name", acc.getText().toString());
                                            return data;
                                        }
                                    };
                                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                    requestQueue.add(stringRequest);
                                }
                            }).start();
                        }
                    });
                    alertDialog.setNegativeButton("取消!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(register.this, retail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
