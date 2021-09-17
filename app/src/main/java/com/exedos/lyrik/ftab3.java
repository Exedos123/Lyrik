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


public class ftab3 extends Fragment {

    public ftab3() {
        // Required empty public constructor
    }

    public TextView titleH, hindiL;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ftab3, container, false);


        titleH = (TextView) view.findViewById(R.id.hindi_Title);

        hindiL = (TextView) view.findViewById(R.id.hindi_lyric);

        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Songs").document(getActivity().getIntent().getStringExtra("Song_Id"))
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    if ((documentSnapshot.getString("Lyrik_H")!=null) && (documentSnapshot.getString("Title"))!=null){

                        titleH.setText(documentSnapshot.get("Title").toString());


                        hindiL.setText(documentSnapshot.get("Lyrik_H").toString());
                    }else{
                        titleH.setText("Lyric Not Updated ");


                        hindiL.setText("");
                    }

                } else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();

                }
            }
        });
        return view;
    }
}