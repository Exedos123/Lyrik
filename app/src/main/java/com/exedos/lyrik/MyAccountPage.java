package com.exedos.lyrik;

import static com.exedos.lyrik.MainActivity.homeListModelList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyAccountPage extends AppCompatActivity {
    private Button signOutBtn,UploadedListBtn;
    public String Fn;
    public String Em;
    private FirebaseUser currentUser;
    public TextView fullnameTextView;
    public TextView emailTextView;
    private HomeListAdapter homeListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account_page);
        signOutBtn =findViewById(R.id.sign_out_btn);


        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent registerIntent = new Intent(MyAccountPage.this,MainActivity.class);
                startActivity(registerIntent);
                finish();

            }
        });

        UploadedListBtn =findViewById(R.id.UploadBtnF);


        UploadedListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent UploadListIntent = new Intent(MyAccountPage.this,User_Upload_List.class);
                startActivity(UploadListIntent);
                finish();

            }
        });



        fullnameTextView = findViewById(R.id.fullname);
        emailTextView = findViewById(R.id.email);


        homeListAdapter = new HomeListAdapter(homeListModelList);
        homeListAdapter.notifyDataSetChanged();





        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "please Login", Toast.LENGTH_SHORT).show();
        }
        else {
            FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        Fn = task.getResult().getString("fullname");
                        Em = task.getResult().getString("email");

                        fullnameTextView.setText(Fn);
                        emailTextView.setText(Em);

                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(MyAccountPage.this, error, Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }


    }

}