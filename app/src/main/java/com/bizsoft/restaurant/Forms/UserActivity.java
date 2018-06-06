package com.bizsoft.restaurant.Forms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bizsoft.restaurant.R;
import com.bizsoft.restaurant.adapters.CustomSpinnerAdapter;
import com.bizsoft.restaurant.dataobjects.Store;
import com.bizsoft.restaurant.dataobjects.User;
import com.bizsoft.restaurant.service.BizUtils;

import java.util.ArrayList;
import java.util.HashMap;

import me.anwarshahriar.calligrapher.Calligrapher;

import static com.bizsoft.restaurant.service.BizUtils.println;

public class UserActivity extends AppCompatActivity {

    AutoCompleteTextView keyword;
    EditText username,passowrd,Rpassword,phoneNumber,mail,name;
    private User.OnTaskCompleted onTaskCompleted_u;
    private ArrayAdapter<String> userListAdaptor;
    private User user;
    Long user_id = Long.valueOf(0);
    private CustomSpinnerAdapter roleListAdapter;
    private String role;
    private Spinner roleS;
    private HashMap<String, String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        keyword = findViewById(R.id.keyword);
        username = findViewById(R.id.username);
        passowrd = findViewById(R.id.password);
        Rpassword = findViewById(R.id.re_password);
        phoneNumber = findViewById(R.id.phone_number);
        mail = findViewById(R.id.mail);
        name = findViewById(R.id.name);
        roleS = findViewById(R.id.role);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "fonts/Quicksand-BoldItalic.otf", true);
        getSupportActionBar().setTitle("User Create/Update");
        BizUtils.addCustomActionBar(UserActivity.this,"User Create/Update");


        onTaskCompleted_u = new User.OnTaskCompleted() {
            @Override
            public void onTaskCompleted() {




                println("getting user list");
                setRoleList(roleS,R.layout.dropdown_style_left);
                setUserList(Store.getInstance().userList,keyword,Store.getInstance().dropDownLayout);


            }
        };
        User.loadData(onTaskCompleted_u);

    }

    private void setUserList(final ArrayList<User> userList, AutoCompleteTextView keyword, int layout) {

        String[] listViewAdapterContent = new String[userList.size()];
        for(int i=0;i<userList.size();i++)
        {
            listViewAdapterContent[i]= userList.get(i).getName();
        }
        userListAdaptor = new ArrayAdapter<>(UserActivity.this, layout, listViewAdapterContent);
        keyword.setAdapter(userListAdaptor );

        keyword.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selection = (String)adapterView.getItemAtPosition(i);
                for(int x=0;x<userList.size();x++)
                {
                    if(selection.equals(userList.get(x).getName()))
                    {
                        user = userList.get(x);
                        user_id = userList.get(x).getId();
                        // Toast.makeText(CreateCategoryActivity.this, "cat Id ---"+cat.getId(), Toast.LENGTH_SHORT).show();
                        setFields(user);
                    }
                }
            }
        });
    }

    private void setRoleList(Spinner keyword, int layout) {


        final ArrayList<String> list = new ArrayList<String>();
        list.add("User");
        list.add("Admin");

        roleListAdapter = new CustomSpinnerAdapter(UserActivity.this, list, R.layout.dropdown_style_left);
        roleS.setAdapter(roleListAdapter );

        roleS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                role = list.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setFields(User user) {


        name.setText(String.valueOf(user.getName()));
        username.setText(String.valueOf(user.getUsername()));
        passowrd.setText(String.valueOf(user.getPassword()));
        Rpassword.setText(String.valueOf(user.getPassword()));
        mail.setText(String.valueOf(user.getEmailAddress()));
        phoneNumber.setText(String.valueOf(user.getPhoneNumber()));

        if(user.getRole().toLowerCase().equals("user"))
        {
            roleS.setSelection(0);;

        }
        else
        {
            roleS.setSelection(1);
        }
    }

    public void setSaveUpdate(View v)
    {
        if(validate())
        {

            println("passed");

            if (user != null) {
                params.put("to", "update");
                params.put("id", String.valueOf(user.getId()));

            } else {
                params.put("to", "save");

            }

            User.OnTaskCompleted onTaskCompleted = new User.OnTaskCompleted() {
                @Override
                public void onTaskCompleted() {

                    println("User saved");

                    Toast.makeText(UserActivity.this, "Saved...", Toast.LENGTH_SHORT).show();
                    User.loadData(onTaskCompleted_u);


                    clear(null);
                }
            };

            User.saveUpdate(onTaskCompleted,params);


        }
        else
        {
            println("failed");
        }
    }

    public void clear(View v) {

        keyword.setText("");
        name.setText("");
        username.setText("");
        passowrd.setText("");
        Rpassword.setText("");
        mail.setText("");
        phoneNumber.setText("");

        name.setError(null);
        username.setError(null);
        passowrd.setError(null);
        Rpassword.setError(null);
        mail.setError(null);
        phoneNumber.setError(null);

        user = null;
        user_id = Long.valueOf(0);
    }

    private boolean validate() {
        boolean status = true;
        params = new HashMap<String,String>();

        if(TextUtils.isEmpty(name.getText()))
        {
            name.setError("Please fill");
            status =false;
        }
        else
        {
         params.put("name",name.getText().toString());
        }

        if(TextUtils.isEmpty(username.getText()))
        {
            username.setError("Please fill");
            status =false;
        }
        else
        {
            params.put("username",username.getText().toString());
        }
        if(TextUtils.isEmpty(passowrd.getText()))
        {
            passowrd.setError("Please fill");
            status =false;
        }
        else
        {
            params.put("passowrd",passowrd.getText().toString());
        }
        if(TextUtils.isEmpty(Rpassword.getText()))
        {
            Rpassword.setError("Please fill");
            status =false;
        }
        else
        {
            params.put("password",Rpassword.getText().toString());
        }
        if(TextUtils.isEmpty(mail.getText()))
        {
            mail.setError("Please fill");
            status =false;
        }
        else
        {
            params.put("mail",mail.getText().toString());
        }
        if(TextUtils.isEmpty(phoneNumber.getText()))
        {
            phoneNumber.setError("Please fill");
            status =false;
        }
        else
        {
            params.put("phoneNumber",phoneNumber.getText().toString());
        }

        try {
            if (! (Rpassword.getText().toString().equals(passowrd.getText().toString()))) {

                Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
                Rpassword.setError("Password does not match");
            }
        }catch (Exception e)
        {

            status = false;
        }

        params.put("role",role);

        return status;
    }


}
