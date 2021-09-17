package com.exedos.lyrik;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Hindi_Lyrik extends Fragment {
    public Hindi_Lyrik() {
        // Required empty public constructor
    }

    private Button NextBtn3;
    FrameLayout parentFrameLayout;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public EditText songTitle;
    public EditText songLyric;
    public String lang;
    final String TAG = "L";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hindi__lyrik, container, false);

        songTitle = (EditText) view.findViewById(R.id.titleSong);
        songLyric = (EditText) view.findViewById(R.id.lyricSong);

        NextBtn3 = view.findViewById(R.id.UploadBtnF);
        parentFrameLayout = getActivity().findViewById(R.id.Song_Lyric_fragment_Container);

        NextBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new Hindi_Lyrik());
                String TiSong = songTitle.getText().toString();
                String LySongT = songLyric.getText().toString();
                String LySongE = songLyric.getText().toString();
                String LySongH = songLyric.getText().toString();

                uploadSong(TiSong, LySongT, LySongE, LySongH);
                //Toast.makeText(Hindi_Lyrik.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Successfully Uploaded", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();

    }

    protected void uploadSong(String tS, String tL, String lySongE, String lySongH) {
        // Create a new user with a first, middle, and last name
        Map<String, Object> user = new HashMap<>();
        String TiSong = songTitle.getText().toString();
        String tS1 = tS;
        String tL1 = tL;
        String tL2 = lySongE;
        String tL3 = lySongH;
        user.put("Title", tS1);
        user.put("Lyrik_T", tL1);
        user.put("Lyrik_E", tL2);
        user.put("Lyrik_H", tL3);


// Add a new document with a generated ID

        db.collection("Songs")
                .document(TiSong)
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + TiSong);


                            Intent intent1 = new Intent(getActivity(), MainActivity.class);

                            startActivity(intent1);
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_hindi__lyrik, container, false);
//    }
//    package com.lyrika;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.ArrayDeque;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//    public class Upload_Song extends AppCompatActivity {
//
//        public EditText songTitle;
//        public EditText songLyric;
//        public String lang;
//        public Button but1;
//        final String TAG="L";
//        private Toolbar toolbar;
//
//        private Spinner spin;
//
//
//        private ImageView  homeBtn, accountBtn;
//
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.upload_song);
//            songTitle = (EditText) findViewById(R.id.titleSong);
//            songLyric = (EditText) findViewById(R.id.lyricSong);
//
//            but1 = (Button) findViewById(R.id.uplSong);
//
//
//            toolbar = findViewById(R.id.myToolBar);
//            setSupportActionBar(toolbar);
//
//
//            toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.myToolBar);
//
//            // calling fragment
//            getSupportFragmentManager().beginTransaction().add(R.id.song_Lyric_fragment_Container,new Telugu_Lyrik()).commit();
//
//
//
//
//            homeBtn = findViewById(R.id.Home_btn);
//
//            accountBtn = findViewById(R.id.account_img);
//
//
//            homeBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent HomeIntent = new Intent(com.lyrika.Upload_Song.this, MainActivity.class);
//                    startActivity(HomeIntent);
//
//
//                    //  frameLayout = findViewById(R.id.main_frame_layout);
//
//
//                }
//            });
//
//
//            Spinner spin = (Spinner) findViewById(R.id.Spinner);
//
//            ArrayAdapter<String> myAdapter= new ArrayAdapter<String>(com.lyrika.Upload_Song.this,android.R.layout.simple_list_item_1,
//                    getResources().getStringArray(R.array.Select_language));
//            myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spin.setAdapter(myAdapter);
//
//
//            spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
//
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view,
//                                           int position, long id) {
//
//                    //Toast.makeText(Upload_Song.this, spin.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
//                    if (spin.getSelectedItem().equals("Telugu") ){
//                        lang= "Lyrik_T";
//                        Toast.makeText(com.lyrika.Upload_Song.this, "u r selected telugu", Toast.LENGTH_SHORT).show();
//                    }
//                    if (spin.getSelectedItem().equals("English") ){
//                        lang= "Lyrik_E";
//                        Toast.makeText(com.lyrika.Upload_Song.this, "u r selected English", Toast.LENGTH_SHORT).show();
//                    }
//                    if (spin.getSelectedItem().equals("Hindi") ){
//                        lang= "Lyrik_H";
//                        Toast.makeText(com.lyrika.Upload_Song.this, "u r selected Hindi", Toast.LENGTH_SHORT).show();
//                    }else{
//                        Toast.makeText(com.lyrika.Upload_Song.this, "please select language", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//                    // TODO Auto-generated method stub
//
//                }
//            });
//            accountBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//                    Intent HomeIntent = new Intent(com.lyrika.Upload_Song.this, MyAccountPage.class);
//                    startActivity(HomeIntent);
//
//                }
//            });
//
//
//            //Appbar Code
//
//
//
//
//
//
//            but1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String TiSong = songTitle.getText().toString();
//                    String LySongT = songLyric.getText().toString();
//                    String LySongE= songLyric.getText().toString();
//                    String LySongH= songLyric.getText().toString();
//                    uploadSong(TiSong,LySongT,LySongE,LySongH);
//                    Toast.makeText(com.lyrika.Upload_Song.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
//
//                }
//            });
//
//            //Spinner
//
//
//
//            ///Spinner
//
//
//
//
//
//        }
//
//
//
////////////////////user upload songs
//   /*
// currentUser = FirebaseAuth.getInstance().getCurrentUser();
//
//         if (currentUser == null) {
//             navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(false);
//         }
//         else {
//             FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid())
//                     .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                 @Override
//                 public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                     if (task.isSuccessful()) {
//                         DBqueries.fullname = task.getResult().getString("fullname");
//                         DBqueries.email = task.getResult().getString("email");
//                         DBqueries.profile = task.getResult().getString("profile");
//
//                         fullName.setText(DBqueries.fullname);
//                         email.setText(DBqueries.email);
//
//                         //// code for profile image
//                        /* if(DBqueries.profile.equals("")){
//                             addProfileIcon.setVisibility(View.VISIBLE);
//                         }else {
//                             addProfileIcon.setVisibility(View.INVISIBLE);
//                             Glide.with(MainActivity.this).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.drawable.ic_baseline_person_24)).into(profileImage);
//                         }
//                        */
//
//        //////////////////user upload songs
//
//
//
//
//
//
//        protected void uploadSong(String tS, String tL, String lySongE, String lySongH) {
//            // Create a new user with a first, middle, and last name
//            Map<String, Object> user = new HashMap<>();
//            String TiSong = songTitle.getText().toString();
//            String tS1 = tS;
//            String tL1 = tL;
//            // String tL2 = lySongE;
//            //  String tL3 = lySongH;
//            user.put("Title", tS1);
//            user.put(lang, tL1);
//            //   user.put("Lyrik_E", tL2);
//            // user.put("Lyrik_H", tL3);
//
//
//
//
//// Add a new document with a generated ID
//
//            db.collection("Songs")
//                    .document(TiSong)
//                    .set(user)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//
//                            if (task.isSuccessful()) {
//                                Log.d(TAG, "DocumentSnapshot added with ID: " + TiSong);
//
//
//                                Intent intent1 = new Intent(com.lyrika.Upload_Song.this, MainActivity.class);
//
//                                startActivity(intent1);
//                            }
//                            else {
//                                String error = task.getException().getMessage();
//                                Toast.makeText(com.lyrika.Upload_Song.this, error, Toast.LENGTH_SHORT).show();
//
//                            }
//                        }
//                    });
//        }
//
//
//    /*    db.collection("users")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//
//
//
//                        Intent intent1= new Intent(Upload_Song.this,MainActivity.class);
//
//                        startActivity(intent1);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
//                    }
//                });
//        }   */
//
//
//
//    }


}