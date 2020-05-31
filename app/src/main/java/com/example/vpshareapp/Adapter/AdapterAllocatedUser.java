package com.example.vpshareapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vpshareapp.Model.ModelAllocatedUser;
import com.example.vpshareapp.R;
import com.example.vpshareapp.ThereProfileActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterAllocatedUser extends  RecyclerView.Adapter<AdapterAllocatedUser.MyHolder> {



    Context context;
    List<ModelAllocatedUser> userList;

    public AdapterAllocatedUser(Context context, List<ModelAllocatedUser> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_allocated_users,parent,false);
        return new AdapterAllocatedUser.MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {


        final String hisUID=userList.get(position).getBagid();

        String userEmail=userList.get(position).getEmail();
        String userName=userList.get(position).getName();
        String donatedStuff=userList.get(position).getDonated();


        //setdata
        holder.mNameTv.setText(userName);
        // Toast.makeText(context, ""+hisUID, Toast.LENGTH_SHORT).show();
        holder.mEmailTv.setText(userEmail);
        holder.mdonated.setText(donatedStuff);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle when click on user
                //goto posts pages
                Intent intent=new Intent(context, ThereProfileActivity.class);
                intent.putExtra("hisId", hisUID);
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView mNameTv, mEmailTv, mdonated;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            mdonated = itemView.findViewById(R.id.donated_stuff);
            mNameTv = itemView.findViewById(R.id.nametv);
            mEmailTv = itemView.findViewById(R.id.emailTv);

        }
    }
}
