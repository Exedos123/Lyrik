package com.exedos.lyrik;

import static com.exedos.lyrik.DBqueries.firebaseFirestore;
import static com.exedos.lyrik.RegisterActivity.setSignUpFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    SearchView mySearchView;
    private Dialog signInDialog;
    private FirebaseUser currentUser;
    private FrameLayout frameLayout;
    private int currentFragment = -1;
    private Toolbar toolbar;
    final String TAG = "L";
    ////refresh
    public static SwipeRefreshLayout swipeRefreshLayoutV1;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    private ImageView noInternetConnection;
    private Button retryBtn;

    ///// refresh


    private RecyclerView songListRecyclerView;
    private LinearLayoutManager layoutManager;
    public static List<HomeListModel> homeListModelList = new ArrayList<>();
    private HomeListAdapter homeListAdapter;


    FirebaseFirestore db = FirebaseFirestore.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the details of the user


        signInDialog = new Dialog(MainActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);

        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignInBtn = signInDialog.findViewById(R.id.signIn_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.signUp_btn);
        Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
//////Refresher
        swipeRefreshLayoutV1 = findViewById(R.id.V1_refresh_layout);
        noInternetConnection = findViewById(R.id.no_internet_connection);
        retryBtn = findViewById(R.id.retry_btn);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();





        ////refresher

        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        songListRecyclerView = findViewById(R.id.home_list_recyclerview);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        songListRecyclerView.setLayoutManager(layoutManager);

        if(networkInfo != null && networkInfo.isConnected() == true) {

            noInternetConnection.setVisibility(View.GONE);
            retryBtn.setVisibility(View.GONE);


            /// getting the list of songs from the firebase
            if (homeListModelList.size() == 0) {
                loadSongsList();
            } else {
                homeListAdapter = new HomeListAdapter(homeListModelList);
                homeListAdapter.notifyDataSetChanged();
            }

            songListRecyclerView.setAdapter(homeListAdapter);

            /// getting the list of songs from the firebase
        }
        else{
            Glide.with(this).load(R.drawable.nointernet_image).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);

        }


        dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SignUpFragment.disableCloseBtn = true;
                SignInFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });


        dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment.disableCloseBtn = true;
                SignInFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);
            }
        });

        swipeRefreshLayoutV1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayoutV1.setRefreshing(true);
                reloadPage();

            }
        });





        //get the details of the user
// Adding App Bar Code
        /*
        homeBtn = findViewById(R.id.Home_btn);
        uploadBtn = findViewById(R.id.add_song_btn_app_bar);
        accountBtn = findViewById(R.id.account_img);


        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Homeintent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(Homeintent);


                frameLayout = findViewById(R.id.main_frame_layout);


            }
        });

        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent Homeintent = new Intent(MainActivity.this, MyAccountPage.class);
                startActivity(Homeintent);

            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Uploadintent = new Intent(MainActivity.this, Upload_Song.class);
                startActivity(Uploadintent);


            }
        });

         */


        //Appbar Code

        mySearchView = (SearchView) findViewById(R.id.search_view);

        ArrayList<String> list = new ArrayList<>();


        //from dtabase list of songs


        db.collection("Songs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


        //end from database



        /// retry btn
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadPage();
            }
        });
        ///// retry btn




        mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s1) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });
    }

    //////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchbar_menu_main, menu);
        MenuItem item1 = menu.findItem(R.id.searchBtn);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) item1.getActionView();

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                homeListAdapter.getFilter().filter(newText);
                return false;
            }
        });




        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.uploadBtn) {
            if(currentUser == null) {
                signInDialog.show();
            }else {
                // gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
                Intent UploadIntent = new Intent(MainActivity.this, Upload_Song.class);
                startActivity(UploadIntent);
            }



            return true;

        }
        else if (id == R.id.AccountBtn) {
            if(currentUser == null) {
                signInDialog.show();
            }else {
                // gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
                Intent UploadIntent = new Intent(MainActivity.this, MyAccountPage.class);
                startActivity(UploadIntent);
            }




        }


        return super.onOptionsItemSelected(item);
    }





    /////////////////

    private void setFragment(Fragment fragment, int fragmentNo) {
        if (fragmentNo != currentFragment) {
            currentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            //fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.commit();
        }

    }



    private void reloadPage(){
        networkInfo = connectivityManager.getActiveNetworkInfo();
        homeListModelList.clear();


        if(networkInfo != null && networkInfo.isConnected() == true) {

            noInternetConnection.setVisibility(View.GONE);
            retryBtn.setVisibility(View.GONE);

            songListRecyclerView.setVisibility(View.VISIBLE);

            homeListAdapter = new HomeListAdapter(homeListModelList);
            homeListAdapter.notifyDataSetChanged();

            songListRecyclerView.setAdapter(homeListAdapter);

            loadSongsList();
        }
        else{
            Toast.makeText(this,"Check Your Internet Connection..",Toast.LENGTH_SHORT).show();
            songListRecyclerView.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.nointernet_image).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);

            swipeRefreshLayoutV1.setRefreshing(false);

        }
    }
    public void loadSongsList() {

        firebaseFirestore.collection("Songs").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    //HomeListModel homeListModel = documentSnapshot.toObject(HomeListModel.class);
                    // homeListModel.setSongTitle(documentSnapshot.getId());
                    //homeListModelList.add(new HomeListModel(documentSnapshot.get("Title").toString()));
                    homeListModelList.add(new HomeListModel(documentSnapshot.getId()));
                }
                homeListAdapter = new HomeListAdapter(homeListModelList);
                songListRecyclerView.setAdapter(homeListAdapter);

                homeListAdapter.notifyDataSetChanged();


                swipeRefreshLayoutV1.setRefreshing(false);

            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

}


