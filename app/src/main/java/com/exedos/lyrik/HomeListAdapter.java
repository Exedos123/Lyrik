package com.exedos.lyrik;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> implements Filterable {

    private List<HomeListModel> homeListModelList;
    private List<HomeListModel>homeListModelListAll;

    public HomeListAdapter(List<HomeListModel> homeListModelList) {
        this.homeListModelList = homeListModelList;
        this.homeListModelListAll = new ArrayList<>(homeListModelList);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list_layout,parent,false);

        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        String sTitle = homeListModelList.get(position).getSongTitle();


        viewHolder.setData(sTitle);


    }

    @Override
    public int getItemCount() {
        return homeListModelList.size();
    }
////////////recycler view search view

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
     Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
           List<HomeListModel> filteredList = new ArrayList<>();
           if (constraint == null|| constraint.length() == 0 ){
               filteredList.addAll(homeListModelListAll);

           }else {
               String filterPattern = constraint.toString().toLowerCase().trim();


               for (HomeListModel item : homeListModelListAll){
                   if (item.getSongTitle().toLowerCase().contains(filterPattern)){
                       filteredList.add(item);

                   }
               }
           }

           FilterResults results = new FilterResults();
           results.values = filteredList;

           return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            homeListModelList.clear();
            homeListModelList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
    /////////// recycler search view


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView songTitle;

        public ViewHolder(@NonNull View itemView)  {
            super(itemView);

            songTitle = itemView.findViewById(R.id.layout_song_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent SongDetailsIntent = new Intent(itemView.getContext(), Song_View_page.class);
                    SongDetailsIntent.putExtra("Song_Id",homeListModelList.get(getAdapterPosition()).getSongTitle());

                    itemView.getContext().startActivity(SongDetailsIntent);
                }
            });








        }

        public void setData(String sTitle) {
            songTitle.setText(sTitle);
        }



    }

}


