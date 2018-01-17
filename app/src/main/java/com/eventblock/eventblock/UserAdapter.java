package com.eventblock.eventblock;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import static android.app.Activity.*;

/**
 * Created by oscar on 19/12/17.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context mCtx;
    private List<User> userList;

    public UserAdapter(Context mCtx, List<User> userList) {
        this.mCtx = mCtx;
        this.userList = userList;
    }

    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.friend_list_item, null);


        /*Display display = ((WindowManager) mCtx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        int width = display.getWidth(); // ((display.getWidth()*20)/100)
        int height = display.getHeight();// ((display.getHeight()*30)/100)

        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
        view.setLayoutParams(parms);*/

        UserViewHolder holder = new UserViewHolder(view);
        return holder;


    }

    @Override
    public void onBindViewHolder(UserAdapter.UserViewHolder holder, int position) {

        User user = userList.get(position);

        holder.textViewTitle.setText(user.getUsername());
        holder.textViewDesc.setText(user.getEmail());
        holder.textViewRating.setText(String.valueOf(user.getRating()));
        holder.textViewPrice.setText(String.valueOf(user.getPrice()));

        if(user.getFb_id() != 0) {


            Glide.with(mCtx)
                    .load( "https://graph.facebook.com/" + user.getFb_id()+ "/picture?type=large")
                    .into(holder.imageView);

        } else {

            Glide.with(mCtx)
                    .load(user.getProfilePic())
                    .into(holder.imageView);
        }




    }

    @Override
    public int getItemCount() {
        return userList.size();
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
