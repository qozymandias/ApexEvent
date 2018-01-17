package com.eventblock.eventblock;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by oscar on 19/12/17.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.UserViewHolder> {

    private Context mCtx;
    private List<Event> eventList;

    public EventAdapter(Context mCtx, List<Event> eventList) {
        this.mCtx = mCtx;
        this.eventList = eventList;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.friend_list_item, null);
        UserViewHolder holder = new UserViewHolder(view);
        return holder;


    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {

        Event event = eventList.get(position);

        holder.textViewTitle.setText(event.getEvent_name());
        holder.textViewDesc.setText(event.getDescription());
        holder.textViewRating.setText(String.valueOf(event.getCapacity()));
        holder.textViewPrice.setText(String.valueOf(event.getCapacity()));

        /*Glide.with(mCtx)
                .load(event.getProfilePic())
                .into(holder.imageView);*/

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewTitle, textViewDesc, textViewRating, textViewPrice;

        public UserViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDesc = itemView.findViewById(R.id.textViewShortDesc);
            textViewRating = itemView.findViewById(R.id.textViewRating);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
        }
    }
}
