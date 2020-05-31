package com.example.vpshareapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.vpshareapp.Admin.UserUnderCommander;
import com.example.vpshareapp.Model.ModelCommander;
import com.example.vpshareapp.Model.ModelSchool;
import com.example.vpshareapp.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterSchool extends RecyclerView.Adapter<AdapterSchool.MyHolder>  {




    Context context;
    List<ModelSchool> schoolList;

    public AdapterSchool(Context context, List<ModelSchool> schoolList) {
        this.context = context;
        this.schoolList = schoolList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_allschool,parent,false);
        return new AdapterSchool.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final String hisUID=schoolList.get(position).getUid();

        final String userEmail=schoolList.get(position).getEmail();
        final String userName=schoolList.get(position).getName();
        final String requiredStuff=schoolList.get(position).getRequiredStuff();


        //setdata
        holder.mNameTv.setText(userName);
        // Toast.makeText(context, ""+hisUID, Toast.LENGTH_SHORT).show();
        holder.mEmailTv.setText(userEmail);
        holder.requiredStuff.setText(requiredStuff);
        //handle click
        /*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle when click on user
                //goto posts pages

                Intent intent=new Intent(context, UserUnderCommander.class);
                intent.putExtra("commanderId",hisUID);
                context.startActivity(intent);

            }
        });

         */
    }

    @Override
    public int getItemCount() {
        return schoolList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView mNameTv, mEmailTv, requiredStuff;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            mNameTv=itemView.findViewById(R.id.schoolnametv);
            mEmailTv=itemView.findViewById(R.id.emailTv);
            requiredStuff=itemView.findViewById(R.id.required_stuff);



        }
    }

}
