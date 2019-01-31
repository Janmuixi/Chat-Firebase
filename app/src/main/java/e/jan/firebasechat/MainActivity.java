package e.jan.firebasechat;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    Button login;
    Button register;
    String correo;
    String psw;
    ProgressBar progressBar;
    FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.editText4);
        password = findViewById(R.id.editText3);
        login = findViewById(R.id.button3);
        register = findViewById(R.id.button4);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Register(true, email.getText().toString().trim(), password.getText().toString());

                // Transform variables into Strings
                correo = email.getText().toString();
                psw = password.getText().toString();
                // Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("users");
                DatabaseReference childRef = myRef.child(currentUser.getUid());
                DatabaseReference emailField = childRef.child("email");
                DatabaseReference pswField = childRef.child("password");
                emailField.setValue(correo);
                pswField.setValue(psw);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Login(email.getText().toString().trim(), password.getText().toString());
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();

    }

    public void Register(Boolean isLogin, final String email, final String password) {
        if(isLogin) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                // Log in becouse we need to save the UID from the actual user,
                                // and we cant take the UID if we are not logged in.
                                Login(email, password);
                                currentUser = mAuth.getCurrentUser();

                                //Display a message to the user if the login success.
                                Toast.makeText(MainActivity.this, "Account created successfully.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(MainActivity.this, "Registration failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });
        }
    }
    public void Login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(MainActivity.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                            currentUser = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}
