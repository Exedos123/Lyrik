package com.exedos.lyrik;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;


public class English_Lyrik extends Fragment {

    public English_Lyrik() {
        // Required empty public constructor
    }
    private Button NextBtn2;
    FrameLayout parentFrameLayout;

//    public static English_Lyrik newInstance(String param1, String param2) {
//        English_Lyrik fragment = new English_Lyrik();
//        Bundle args = new Bundle();
//
//        fragment.setArguments(args);
//        return fragment;
//    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_english__lyrik, container, false);

        NextBtn2 = view.findViewById(R.id.Next_2);
        parentFrameLayout = getActivity().findViewById(R.id.Song_Lyric_fragment_Container);


        NextBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { setFragment(new Hindi_Lyrik()); }
        });
        return view;
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();


    }
}