package com.app.uconect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterOptionjActivity extends ParentActivity implements View.OnClickListener {
    Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeroption);
        BindView(null,savedInstanceState);
    }

    @Override
    public void BindView(View view, Bundle savedInstanceState) {
        super.BindView(view, savedInstanceState);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        SetOnclicklistener();
    }

    @Override
    public void SetOnclicklistener() {
        super.SetOnclicklistener();
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                Intent intent = new Intent(RegisterOptionjActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.register:
                Intent intent1 = new Intent(RegisterOptionjActivity.this, RegisterActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
