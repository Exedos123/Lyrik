package com.exedos.lyrik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class User_Upload_List extends AppCompatActivity {
    ////refresh
    public static SwipeRefreshLayout swipeRefreshLayoutUpl;
    private ConnectivityManager connectivityManagerUpl;
    private NetworkInfo networkInfoUpl;
    private ImageView noInternetConnectionUpl;
    private Button retryBtnUpl;
    private String userID;
    private FirebaseUser currentUser;
    private Toolbar toolbar;
    private ImageView homeBtn, uploadBtn, accountBtn;
    ///// refresh

    private RecyclerView uploadedrecyclerview;
    private LinearLayoutManager layoutManager;
    public static List<HomeListModel> homeListModelList = new ArrayList<>();
    private HomeListAdapter homeListAdapter;


    FirebaseFirestore db = FirebaseFirestore.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_upload_list);
        //////Refresher
        swipeRefreshLayoutUpl = findViewById(R.id.V1_refresh_layout_upl);
        noInternetConnectionUpl = findViewById(R.id.no_internet_connection_upl);
        retryBtnUpl = findViewById(R.id.retry_btn_upl);
        connectivityManagerUpl = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfoUpl = connectivityManagerUpl.getActiveNetworkInfo();
        ////refresher

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);

        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.myToolBar);

        // Adding App Bar Code
        homeBtn = findViewById(R.id.Home_btn);
        uploadBtn = findViewById(R.id.add_song_btn_app_bar);
        accountBtn = findViewById(R.id.account_img);

        accountBtn.setVisibility(View.GONE);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Homeintent = new Intent(User_Upload_List.this, MainActivity.class);
                startActivity(Homeintent);
                finish();


                //  frameLayout = findViewById(R.id.main_frame_layout);


            }
        });

        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
                Intent Homeintent = new Intent(User_Upload_List.this, MyAccountPage.class);
                startActivity(Homeintent);
                finish();
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {

                                             // gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
                                             Intent Uploadintent = new Intent(User_Upload_List.this, Upload_Song.class);
                                             startActivity(Uploadintent);
                                             finish();

                                         }
                                     });




        //Appbar Code







        uploadedrecyclerview = findViewById(R.id.uploaded_list_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        uploadedrecyclerview.setLayoutManager(layoutManager);

        if (networkInfoUpl != null && networkInfoUpl.isConnected() == true) {

            noInternetConnectionUpl.setVisibility(View.GONE);
            retryBtnUpl.setVisibility(View.GONE);


            /// getting the list of songs from the firebase
            if (homeListModelList.size() == 0) {
                loadSongsList();
            } else {
                homeListAdapter = new HomeListAdapter(homeListModelList);
                homeListAdapter.notifyDataSetChanged();
            }

            uploadedrecyclerview.setAdapter(homeListAdapter);

            /// getting the list of songs from the firebase
        } else {
            Glide.with(this).load(R.drawable.nointernet_image).into(noInternetConnectionUpl);
            noInternetConnectionUpl.setVisibility(View.VISIBLE);
            retryBtnUpl.setVisibility(View.VISIBLE);

        }

        swipeRefreshLayoutUpl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayoutUpl.setRefreshing(true);
                reloadPage();

            }
        });

/// retry btn
        retryBtnUpl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadPage();
            }
        });
        ///// retry btn
    }

    private void reloadPage(){
        networkInfoUpl = connectivityManagerUpl.getActiveNetworkInfo();
        homeListModelList.clear();


        if(networkInfoUpl != null && networkInfoUpl.isConnected() == true) {

            noInternetConnectionUpl.setVisibility(View.GONE);
            retryBtnUpl.setVisibility(View.GONE);

            uploadedrecyclerview.setVisibility(View.VISIBLE);

            homeListAdapter = new HomeListAdapter(homeListModelList);
            homeListAdapter.notifyDataSetChanged();

            uploadedrecyclerview.setAdapter(homeListAdapter);

            loadSongsList();
        }
        else{
            Toast.makeText(this,"Check Your Internet Connection..",Toast.LENGTH_SHORT).show();
            uploadedrecyclerview.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.nointernet_image).into(noInternetConnectionUpl);
            noInternetConnectionUpl.setVisibility(View.VISIBLE);
            retryBtnUpl.setVisibility(View.VISIBLE);

            swipeRefreshLayoutUpl.setRefreshing(false);

        }
    }
    public void loadSongsList() {

        db.collection("USERS").document(userID).collection("myList").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    //HomeListModel homeListModel = documentSnapshot.toObject(HomeListModel.class);
                    // homeListModel.setSongTitle(documentSnapshot.getId());
                    //homeListModelList.add(new HomeListModel(documentSnapshot.get("Title").toString()));
                    homeListModelList.add(new HomeListModel(documentSnapshot.getId()));
                }
                homeListAdapter = new HomeListAdapter(homeListModelList);
                uploadedrecyclerview.setAdapter(homeListAdapter);

                homeListAdapter.notifyDataSetChanged();


                swipeRefreshLayoutUpl.setRefreshing(false);

            }
        });




    }
    @Override
    protected void onStart() {
        super.onStart();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }


}
