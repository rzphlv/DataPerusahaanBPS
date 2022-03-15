package rzphlv.nmf.dataperusahaanbps;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        checkBox = (CheckBox) findViewById(R.id.checkBox2);

        findViewById (R.id.editTextPassword);
        findViewById(R.id.buttonLoginTamu).setOnClickListener(this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);
        findViewById(R.id.buttonForgot).setOnClickListener(this);

        checkBox.setOnCheckedChangeListener (new CompoundButton.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged ( CompoundButton buttonView, boolean isChecked ){
                if (isChecked)
                {
                    // Show Password
                    editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    // Hide Password
                    editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }


    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        boolean value = true;

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum length of password should be 6");
            editTextPassword.requestFocus();
            return;
        }

/*        if (value)
        {
            // Show Password
            editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        else
        {
            // Hide Password
            editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
*/
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);

                if (task.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this, HomeAdmin.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, HomeAdmin.class));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLoginTamu:
                finish();
                startActivity(new Intent(this, HomeAdmin.class));
                break;

            case R.id.buttonLogin:
                userLogin();
                break;

            case R.id.buttonForgot:
                startActivity(new Intent(this, ResetPasswordActivity.class));
                break;
        }
    }
}
