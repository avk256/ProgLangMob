package com.example.lab6;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AuthFragment extends Fragment {
    private FirebaseAuth mAuth;
    private Button login_btn;
    private Button register_btn;
    private EditText email;
    private EditText name;
    private EditText password;
    private String nameV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences mySharedPreferences = this.getActivity().getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE);
        nameV = mySharedPreferences.getString("name", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_auth, container, false);

        mAuth = FirebaseAuth.getInstance();
        login_btn = (Button) v.findViewById(R.id.login_btn);
        register_btn = (Button) v.findViewById(R.id.register_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = (EditText)v.findViewById(R.id.email);
                password = (EditText)v.findViewById(R.id.password);

                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Login complete.", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://lab4-1d1e9-default-rtdb.europe-west1.firebasedatabase.app");
                                    String emailChild = email.getText().toString().substring(0, email.getText().toString().indexOf("."));
                                    DatabaseReference myRef = database.getReference(emailChild);

                                    myRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            nameV = dataSnapshot.child("name").getValue(String.class);
                                            updateUI(user, nameV);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError error) {
                                        }
                                    });
                                    updateUI(user, nameV);
                                } else {
                                    Toast.makeText(getActivity(), "Login failed.", Toast.LENGTH_SHORT).show();
                                    updateUI(null, "");
                                }
                            }
                        });
            }
        });
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = (EditText) getActivity().findViewById(R.id.email);
                password = (EditText) getActivity().findViewById(R.id.password);
                name = (EditText)v.findViewById(R.id.name);
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Registration complete.", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user, name.getText().toString());
                                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://lab4-1d1e9-default-rtdb.europe-west1.firebasedatabase.app");
                                    String emailChild = email.getText().toString().substring(0, email.getText().toString().indexOf("."));
                                    DatabaseReference myRef = database.getReference(emailChild);
                                    myRef.child("name").setValue(name.getText().toString());
                                } else {
                                    // If sign in fails, display a message to the user.
                                    // Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(getActivity(), "Registration failed.", Toast.LENGTH_SHORT).show();
                                    updateUI(null, "");
                                }

                            }
                        });
            }
        });

        return v;
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser, nameV);

    }

    private void updateUI(FirebaseUser user, String name) {
        if(user != null){
            SharedPreferences mySharedPreferences = this.getActivity().getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putString("email", user.getEmail());

            editor.putString("name", name);
            editor.apply();
        }
    }
}