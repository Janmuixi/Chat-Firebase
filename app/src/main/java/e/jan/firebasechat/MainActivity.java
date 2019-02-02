package e.jan.firebasechat;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, UserFragment.OnFragmentInteractionListener {

    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    FirebaseUser currentUser;
    List<User> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new LoginFragment();
        fm.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

    @Override
    public void login(String email, String psw) {
        mAuth.signInWithEmailAndPassword(email, psw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(MainActivity.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();

                            FragmentManager fm = getSupportFragmentManager();
                            Fragment fragment = new UserFragment();
                            fm.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
    public void loginFast(String email, String psw) {
        mAuth.signInWithEmailAndPassword(email, psw);
    }

    @Override
    public void register(final String email, final String psw) {
        final User user = new User(email, psw);
        mAuth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Log in becouse we need to save the UID from the actual user,
                            // and we cant take the UID if we are not logged in.
                            loginFast(user.email, user.password);
                            currentUser = mAuth.getCurrentUser();

                            // Write a message to the database
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference usersRef = database.getReference().child("users");
                            DatabaseReference childRef = usersRef.child(currentUser.getUid());
                            DatabaseReference emailField = childRef.child("email");
                            DatabaseReference pswField = childRef.child("password");
                            emailField.setValue(email);
                            pswField.setValue(psw);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
