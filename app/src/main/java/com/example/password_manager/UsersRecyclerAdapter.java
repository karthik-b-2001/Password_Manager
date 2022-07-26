package com.example.password_manager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.password_manager.model.User;

import java.util.List;

public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.UserViewHolder> {
    private List<User> listUsers;
    public UsersRecyclerAdapter(List<User> listUsers) {
        this.listUsers = listUsers;
    }
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_recycler, parent, false);
        return new UserViewHolder(itemView);
    }




    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.textViewName.setText(listUsers.get(position).getName());
        holder.textViewEmail.setText(listUsers.get(position).getEmail());
        holder.textViewPassword.setText(listUsers.get(position).getPassword());
    }
    @Override
    public int getItemCount() {
        Log.v(UsersRecyclerAdapter.class.getSimpleName(),""+listUsers.size());
        return listUsers.size();
    }
    /**
     * ViewHolder class
     */
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewEmail;
        public TextView textViewPassword;
        public UserViewHolder(View view) {
            super(view);
            textViewName =  view.findViewById(R.id.textViewName);
            textViewEmail =  view.findViewById(R.id.textViewEmail);
            textViewPassword =  view.findViewById(R.id.textViewPassword);
        }
    }
}