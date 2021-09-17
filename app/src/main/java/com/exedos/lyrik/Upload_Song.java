package com.exedos.lyrik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Upload_Song extends AppCompatActivity {
    public EditText songTitle;
    public EditText songLyric;
    public String lang;
    public Button but1;
    final String TAG="L";
    private Toolbar toolbar;
    private FrameLayout frameLayout1;
    private String userID;

    public static boolean Fragment_telugu = false;
   // private String titlePattern = "[a-zA-Z]+[a-zA-Z]+";
    private String titlePattern = "[a-zA-Z ]+";





    private Spinner spin;


    private ImageView homeBtn, accountBtn;


    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_song);
        songTitle = findViewById(R.id.titleSong);
        songLyric =  findViewById(R.id.lyricSong);
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        but1 =  findViewById(R.id.uplSong);

        frameLayout1 = findViewById(R.id.Song_Lyric_fragment_Container);
        if (Fragment_telugu) {
            Fragment_telugu = false;
            setDefaultFragment(new Telugu_Lyrik());

        }

        toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);

        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.myToolBar);

        homeBtn = findViewById(R.id.Home_btn);

        accountBtn = findViewById(R.id.account_img);


        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent HomeIntent = new Intent(Upload_Song.this, MainActivity.class);
                startActivity(HomeIntent);

                //  frameLayout = findViewById(R.id.main_frame_layout);

            }
        });

        songTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        songLyric.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Spinner spin = (Spinner) findViewById(R.id.Spinner);

        ArrayAdapter<String> myAdapter= new ArrayAdapter<String>(Upload_Song.this,android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.Select_language));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(myAdapter);


        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                //Toast.makeText(Upload_Song.this, spin.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                if (spin.getSelectedItem().equals("Telugu") ){
                    lang= "Lyrik_T";
                    Toast.makeText(Upload_Song.this, "u r selected telugu", Toast.LENGTH_SHORT).show();
                }
                if (spin.getSelectedItem().equals("English") ){
                    lang= "Lyrik_E";
                    Toast.makeText(Upload_Song.this, "u r selected English", Toast.LENGTH_SHORT).show();
                }
//                if (spin.getSelectedItem().equals("Hindi") ){
//                    lang= "Lyrik_H";
//                    Toast.makeText(Upload_Song.this, "u r selected Hindi", Toast.LENGTH_SHORT).show();
//                }
                if(spin.getSelectedItem().equals("SelectLanguage")){
                   but1.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stu
              //  but1.setEnabled(false);
                Toast.makeText(Upload_Song.this, "please select language", Toast.LENGTH_SHORT).show();
            }
        });

        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent HomeIntent = new Intent(Upload_Song.this, MyAccountPage.class);
                startActivity(HomeIntent);

            }
        });


        //Appbar Code

        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spin.getSelectedItem().equals("SelectLanguage")){
                    Toast.makeText(Upload_Song.this, "Please Select Language", Toast.LENGTH_SHORT).show();
                }else{
                    String TiSong = songTitle.getText().toString();
                    String LySongT =songLyric.getText().toString();
                    String LySongE= songLyric.getText().toString();
                    String LySongH= songLyric.getText().toString();
                    uploadSong(TiSong,LySongT,LySongE,LySongH);


                    Toast.makeText(Upload_Song.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();

                }

                   }
        });

    }
    /// Set Errors
    @SuppressLint("ResourceAsColor")
    private void checkInputs() {

        if (!TextUtils.isEmpty(songTitle.getText()) && !songTitle.getText().toString().matches(titlePattern)) {
            if (!TextUtils.isEmpty(songLyric.getText())) {


                but1.setEnabled(true);
                but1.setTextColor(Color.rgb(255, 255, 255));

            }else {

                but1.setEnabled(false);
                but1.setTextColor(Color.argb(50, 255, 255, 255));

            }

        } else {

            songTitle.setError("Please Enter Telugu Title - \n" +
                    "దయచేసి తెలుగులో నమోదు చేయండి");
            but1.setEnabled(false);
            but1.setTextColor(Color.argb(50, 255, 255, 255));

        }

    }


    /// set Errors




//////////////////user upload songs
   /*
 currentUser = FirebaseAuth.getInstance().getCurrentUser();

         if (currentUser == null) {
             navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(false);
         }
         else {
             FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid())
                     .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                 @Override
                 public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                     if (task.isSuccessful()) {
                         DBqueries.fullname = task.getResult().getString("fullname");
                         DBqueries.email = task.getResult().getString("email");
                         DBqueries.profile = task.getResult().getString("profile");

                         fullName.setText(DBqueries.fullname);
                         email.setText(DBqueries.email);

                         //// code for profile image
                        /* if(DBqueries.profile.equals("")){
                             addProfileIcon.setVisibility(View.VISIBLE);
                         }else {
                             addProfileIcon.setVisibility(View.INVISIBLE);
                             Glide.with(MainActivity.this).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.drawable.ic_baseline_person_24)).into(profileImage);
                         }
                        */



    //////////////////user upload songs


    protected void uploadSong(String tS, String tL, String eL, String hL) {
        // Create a new user with a first, middle, and last name
        Map<String, Object> user = new HashMap<>();
        Map<String, Object> user1 = new HashMap<>();
        Map<String, Object> user2 = new HashMap<>();
        String TiSong = songTitle.getText().toString();
        String tS1 = tS;
        String tL1 = tL;
        String tL2 = eL;
        String tL3 = hL;
//    String tL2 = lySongE;
//    String tL3 = lySongH;

        // String tL2 = lySongE;
        //  String tL3 = lySongH;
        user.put("Title", tS1);
        user1.put("Title", tS);
        user2.put("Title", tS1);
        user.put("Lyrik_T", tL1);
        user1.put("Lyrik_E", tL2);
        //user2.put("Lyrik_H", tL3);


       db.collection("USERS").document(userID).collection("myList")
               .document(TiSong).set(user)
               .addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {

                       if (task.isSuccessful()) {
                           Log.d(TAG, "DocumentSnapshot added with ID: " + TiSong);


                           Intent intent1 = new Intent(Upload_Song.this, MainActivity.class);

                           startActivity(intent1);
                       } else {
                           String error = task.getException().getMessage();
                           Toast.makeText(Upload_Song.this, error, Toast.LENGTH_SHORT).show();

                       }
                   }
               });
//       db.collection("Songs")
//               .document(TiSong).set(user)
//               .addOnCompleteListener(new OnCompleteListener<Void>() {
//                   @Override
//                   public void onComplete(@NonNull Task<Void> task) {
//
//                       if (task.isSuccessful()) {
//                           Log.d(TAG, "DocumentSnapshot added with ID: " + TiSong);
//
//
////                           Intent intent1 = new Intent(Upload_Song.this, MainActivity.class);
////
////                           startActivity(intent1);
//                       } else {
//                           String error = task.getException().getMessage();
//                           Toast.makeText(Upload_Song.this, error, Toast.LENGTH_SHORT).show();
//
//                       }
//                   }
//               });

// Add a new document with a generated ID


      if(lang == "Lyrik_T") {

            // Checking The document is existing or not
            db.collection("Songs")
                    .document(TiSong).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()){
                            /// If it is existing update that
                            db.collection("Songs")
                                    .document(TiSong)
                                    .update(user)

                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "DocumentSnapshot added with ID: " + TiSong);
                                                Toast.makeText(Upload_Song.this, "Song Updated Successfully ", Toast.LENGTH_SHORT).show();
                                                Intent intent1 = new Intent(Upload_Song.this, MainActivity.class);
                                                startActivity(intent1);
                                            } else {
                                                String error = task.getException().getMessage();
                                                Toast.makeText(Upload_Song.this, error, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
// if the document not existing then setting new document
                        }else{
                            db.collection("Songs").document(TiSong).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "DocumentSnapshot added with ID: " + TiSong);
                                        Intent intent1 = new Intent(Upload_Song.this, MainActivity.class);
                                        startActivity(intent1);
                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(Upload_Song.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }

                }
            });

//           db.collection("Songs")
//                   .document(TiSong)
//                   .set(user)
//
//                   .addOnCompleteListener(new OnCompleteListener<Void>() {
//                       @Override
//                       public void onComplete(@NonNull Task<Void> task) {
//
//                           if (task.isSuccessful()) {
//                               Log.d(TAG, "DocumentSnapshot added with ID: " + TiSong);
//
//
//                               Intent intent1 = new Intent(Upload_Song.this, MainActivity.class);
//
//                               startActivity(intent1);
//                           } else {
//                               String error = task.getException().getMessage();
//                               Toast.makeText(Upload_Song.this, error, Toast.LENGTH_SHORT).show();
//
//                           }
//                       }
//                   });
//
        }

      else if(lang == "Lyrik_E") {

            // Checking The document is existing or not
            db.collection("Songs")
                    .document(TiSong).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()){
                            /// If it is existing update that
                            db.collection("Songs")
                                    .document(TiSong)
                                    .update(user1)

                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "DocumentSnapshot added with ID: " + TiSong);
                                                Toast.makeText(Upload_Song.this, "Song Updated Successfully ", Toast.LENGTH_SHORT).show();
                                                Intent intent1 = new Intent(Upload_Song.this, MainActivity.class);
                                                startActivity(intent1);
                                            } else {
                                                String error = task.getException().getMessage();
                                                Toast.makeText(Upload_Song.this, error, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
// if the document not existing then setting new document
                        }else{
                            db.collection("Songs").document(TiSong).set(user1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "DocumentSnapshot added with ID: " + TiSong);
                                        Intent intent1 = new Intent(Upload_Song.this, MainActivity.class);
                                        startActivity(intent1);
                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(Upload_Song.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }

                }
            });


//           db.collection("Songs")
//                   .document(TiSong)
//                   .set(user1)
//                   .addOnCompleteListener(new OnCompleteListener<Void>() {
//                       @Override
//                       public void onComplete(@NonNull Task<Void> task) {
//
//                           if (task.isSuccessful()) {
//                               Log.d(TAG, "DocumentSnapshot added with ID: " + TiSong);
//
//
//                               Intent intent1 = new Intent(Upload_Song.this, MainActivity.class);
//
//                               startActivity(intent1);
//                           } else {
//                               String error = task.getException().getMessage();
//                               Toast.makeText(Upload_Song.this, error, Toast.LENGTH_SHORT).show();
//
//                           }
//                       }
//                   });
        }
//
//       else if(lang == "Lyrik_H") {
//
//
//
//            // Checking The document is existing or not
//            db.collection("Songs")
//                    .document(TiSong).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()){
//                        DocumentSnapshot documentSnapshot = task.getResult();
//                        if (documentSnapshot.exists()){
//                            /// If it is existing update that
//                            db.collection("Songs")
//                                    .document(TiSong)
//                                    .update(user2)
//
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()) {
//                                                Log.d(TAG, "DocumentSnapshot added with ID: " + TiSong);
//                                                Toast.makeText(Upload_Song.this, "Song Updated Successfully ", Toast.LENGTH_SHORT).show();
//                                                Intent intent1 = new Intent(Upload_Song.this, MainActivity.class);
//                                                startActivity(intent1);
//                                            } else {
//                                                String error = task.getException().getMessage();
//                                                Toast.makeText(Upload_Song.this, error, Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    });
//// if the document not existing then setting new document
//                        }else{
//                            db.collection("Songs").document(TiSong).set(user2).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Log.d(TAG, "DocumentSnapshot added with ID: " + TiSong);
//                                        Intent intent1 = new Intent(Upload_Song.this, MainActivity.class);
//                                        startActivity(intent1);
//                                    } else {
//                                        String error = task.getException().getMessage();
//                                        Toast.makeText(Upload_Song.this, error, Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                        }
//                    }
//
//                }
//            });
//
//
//
//
////            db.collection("Songs")
////                    .document(TiSong)
////                    .set(user2)
////                    .addOnCompleteListener(new OnCompleteListener<Void>() {
////                        @Override
////                        public void onComplete(@NonNull Task<Void> task) {
////
////                            if (task.isSuccessful()) {
////                                Log.d(TAG, "DocumentSnapshot added with ID: " + TiSong);
////
////
////                                Intent intent1 = new Intent(Upload_Song.this, MainActivity.class);
////
////                                startActivity(intent1);
////                            } else {
////                                String error = task.getException().getMessage();
////                                Toast.makeText(Upload_Song.this, error, Toast.LENGTH_SHORT).show();
////
////                            }
////                        }
////                    });
//        }
    }




    /*    db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());



                        Intent intent1= new Intent(Upload_Song.this,MainActivity.class);

                        startActivity(intent1);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        }   */

    private void setDefaultFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout1.getId(),fragment);
        fragmentTransaction.commit();
    }

}


