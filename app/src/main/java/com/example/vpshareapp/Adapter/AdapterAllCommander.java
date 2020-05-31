package com.example.vpshareapp.Adapter;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vpshareapp.Admin.UserUnderCommander;
import com.example.vpshareapp.MainActivity;
import com.example.vpshareapp.Model.ModelCommander;
import com.example.vpshareapp.R;
import com.example.vpshareapp.School.SchoolRegister;
import com.example.vpshareapp.commanderacivty.CommanderLogin;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterAllCommander extends RecyclerView.Adapter<AdapterAllCommander.MyHolder> {


    Context context;
    List<ModelCommander> commanderList;



    public AdapterAllCommander(Context context, List<ModelCommander> commanderList) {
        this.context = context;
        this.commanderList = commanderList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_allcommander,parent,false);
        return new AdapterAllCommander.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {


        final String hisUID=commanderList.get(position).getUid();

        final String userEmail=commanderList.get(position).getEmail();
        final String userName=commanderList.get(position).getName();
        String bagsAllocated=commanderList.get(position).getAllocated_Bags();
        String isauth=commanderList.get(position).getIsAuthorized();


        //setdata
        holder.mNameTv.setText(userName);
        // Toast.makeText(context, ""+hisUID, Toast.LENGTH_SHORT).show();
        holder.mEmailTv.setText(userEmail);
        holder.bagsAllocated.setText(bagsAllocated);

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
        //set auth button
        if(isauth.equals("0")){
            //authrity  is not given
            holder.giveAuth.setVisibility(View.VISIBLE);
            holder.authText.setText("Give Authority");
        }
        else {
            //authrity is given
            holder.giveAuth.setVisibility(View.GONE);
            holder.authText.setText("Authorized");

        }
        //handle if auth button  clicks
        holder.giveAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.builder.setMessage("Authorized Commander"+userName);

                // Set Alert Title
                holder.builder.setTitle("Alert !");
                // the Dialog Box then it will remain show
                holder.builder.setCancelable(true);
                holder.builder
                        .setPositiveButton(
                                "give authority",
                                new DialogInterface
                                        .OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which)
                                    {
                                        //give authrity to that user
                                        Query query= FirebaseDatabase.getInstance().getReference("Commanders").orderByChild("Email").equalTo(userEmail);
                                        query.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot  ds:dataSnapshot.getChildren()){
                                                    ds.getRef().child("isAuthorized").setValue("1");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }).setNegativeButton(
                        "Cancel",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                Toast.makeText(context, "Authority Cancel", Toast.LENGTH_SHORT).show();
                            }
                        });
                holder.builder.create().show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return commanderList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView mNameTv, mEmailTv, bagsAllocated,authText;
        Button giveAuth;

        AlertDialog.Builder builder;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            builder = new AlertDialog.Builder(context,4);
            bagsAllocated = itemView.findViewById(R.id.allocate_bags);
            mNameTv = itemView.findViewById(R.id.nametv);
            mEmailTv = itemView.findViewById(R.id.emailTv);
            authText=itemView.findViewById(R.id.authurity_text);
            giveAuth=itemView.findViewById(R.id.giveAtuh);



        }
    }
}
