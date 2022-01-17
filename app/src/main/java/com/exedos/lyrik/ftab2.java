package com.exedos.lyrik;

import static com.exedos.lyrik.DBqueries.firebaseFirestore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ftab2 extends Fragment {
    public ftab2() {
        // Required empty public constructor
    }

    public TextView titleE, englishL;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ftab2, container, false);


        titleE = (TextView) view.findViewById(R.id.english_Title);

        englishL = (TextView) view.findViewById(R.id.english_lyric);

        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Songs").document(getActivity().getIntent().getStringExtra("Song_Id"))
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    if ((documentSnapshot.getString("Lyrik_E")!=null) && (documentSnapshot.getString("Title_E"))!=null){
                        titleE.setText(documentSnapshot.get("Title_E").toString());

                        englishL.setText(documentSnapshot.get("Lyrik_E").toString());
                    }else{


                        titleE.setText("Lyric Not Updated ");


                        englishL.setText("");
                    }



                } else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}