package com.example.haipham.uberandroid;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.haipham.uberandroid.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.btnSignIn)
    Button btnSignIn;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.rootLayout)
    RelativeLayout rootLayout;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

//        btnSignIn = findViewById(R.id.btnSignIn);
//        btnRegister = findViewById(R.id.btnRegister);
//        btnRegister.setOnClickListener(v -> showRegisterDialog());
//        btnSignIn.setOnClickListener(v -> showSignInDialog());
    }
    @OnClick({R.id.btnSignIn, R.id.btnRegister})
    public void OnClick(View v){
        switch (v.getId()){
            case R.id.btnSignIn:
                showSignInDialog();
                break;
            case R.id.btnRegister:
                showRegisterDialog();
                break;
        }
    }

    private void showSignInDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("SIGN IN");
        dialog.setMessage("Please use email to sign in");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_login, null);

        MaterialEditText edtEmail = register_layout.findViewById(R.id.edtEmail);
        MaterialEditText edtPass = register_layout.findViewById(R.id.edtPassword);

        dialog.setView(register_layout);


        dialog.setPositiveButton("SIGN IN", (dialogInterface, i) -> {
            dialogInterface.dismiss();
            btnSignIn.setEnabled(false);
            if(TextUtils.isEmpty(edtEmail.getText().toString())){
                Snackbar.make(rootLayout, "Please enter email address", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(edtPass.getText().toString())){
                Snackbar.make(rootLayout, "Please enter password", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if(edtPass.getText().toString().length()<6){
                Snackbar.make(rootLayout, "Password too short !!!", Snackbar.LENGTH_SHORT).show();
                return;
            }
            AlertDialog waittingDialog = new SpotsDialog(MainActivity.this);
            waittingDialog.show();
            //Sign in
            auth.signInWithEmailAndPassword(edtEmail.getText().toString(),edtPass.getText().toString())
            .addOnSuccessListener(authResult -> {
                waittingDialog.dismiss();
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
                finish();
            })
            .addOnFailureListener(e -> {
                waittingDialog.dismiss();
                Snackbar.make(rootLayout, "Failed " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                btnSignIn.setEnabled(true);
            });

        });
        dialog.setNegativeButton("CANCEL", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        dialog.show();
    }

    private void showRegisterDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("REGISTER");
        dialog.setMessage("Please use email to register");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_register, null);

        MaterialEditText edtEmail = register_layout.findViewById(R.id.edtEmail);
        MaterialEditText edtPass = register_layout.findViewById(R.id.edtPassword);
        MaterialEditText edtName = register_layout.findViewById(R.id.edtName);
        MaterialEditText edtPhone = register_layout.findViewById(R.id.edtPhone);

        dialog.setView(register_layout);

        dialog.setPositiveButton("REGISTER", (dialogInterface, i) -> {
            dialogInterface.dismiss();
            if(TextUtils.isEmpty(edtEmail.getText().toString())){
                Snackbar.make(rootLayout, "Please enter email address", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(edtPass.getText().toString())){
                Snackbar.make(rootLayout, "Please enter password", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if(edtPass.getText().toString().length()<6){
                Snackbar.make(rootLayout, "Password too short !!!", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(edtPhone.getText().toString())){
                Snackbar.make(rootLayout, "Please enter phone number", Snackbar.LENGTH_SHORT).show();
                return;
            }
            //Register new user
            auth.createUserWithEmailAndPassword(edtEmail.getText().toString(),edtPass.getText().toString())
                    .addOnSuccessListener(authResult -> {
                        User user = new User();
                        user.setEmail(edtEmail.getText().toString());
                        user.setPassword(edtPass.getText().toString());
                        user.setName(edtName.getText().toString());
                        user.setPhone(edtPhone.getText().toString());

                        users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user)
                                .addOnSuccessListener(aVoid -> {
                                    Snackbar.make(rootLayout, "Register Successfully", Snackbar.LENGTH_SHORT).show();

                                })
                                .addOnFailureListener(e -> {
                                    Snackbar.make(rootLayout, "Failed " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Snackbar.make(rootLayout, "Failed " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    });
        });
        dialog.setNegativeButton("CANCEL", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        dialog.show();
    }
}
