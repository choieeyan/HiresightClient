package com.hiresight.hiresightclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ViewApplicantsActivity extends AppCompatActivity {

    private String postID;
    private FirebaseFirestore db;
    private CollectionReference ref;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_applicants);
        Intent intent = getIntent();
        postID = intent.getStringExtra("postID");

        db = FirebaseFirestore.getInstance();
        ref = db.collection("Client Posts").document(postID).collection("Applicants");
        recyclerView = findViewById(R.id.applicants_view);
        auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        recyclerview();
    }
    private void recyclerview(){
        Query query = ref.orderBy("dateTime", Query.Direction.ASCENDING);
        Log.d("atrecycler: ", postID);
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<User, Holder>(options) {
            @NonNull
            @Override
            public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.applicants_layout, parent, false);
                return new Holder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final Holder holder, int position, @NonNull final User model) {
                final String userID = getSnapshots().getSnapshot(holder.getAdapterPosition()).getId();
                final DocumentReference refer = db.collection("Users").document(userID);
                refer.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                model.setName(document.getString("name"));
                                model.setImageURL(document.getString("imageURL"));
                                Picasso.get().load(model.getImageURL()).into(holder.user_img);
                                holder.user_name.setText(model.getName());
                            } else
                                Log.d("TAG", "No such document");
                        } else
                            Log.d("TAG", "get failed with ", task.getException());
                    }
                });

                holder.chat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ClientMessageActivity.class);
                        intent.putExtra("UserID", userID);
                        startActivity(intent);
                    }
                });

                holder.accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DocumentReference reff = db.collection("Clients").document(auth.getUid()).collection("Hired").document(userID);
                        Map<String, Object> job = new HashMap<>();
                        job.put("postID", postID);
                        refer.collection("Jobs").document(auth.getUid()).set(job);
                        reff.set(job);
                        Toast.makeText(getApplicationContext(), "Hire Complete!", Toast.LENGTH_LONG).show();
                        ref.document(userID).delete();
                    }
                });

                holder.reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ref.document(userID).delete();
                        Toast.makeText(getApplicationContext(), "Rejected!", Toast.LENGTH_LONG).show();
                    }
                });

            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView user_img;
        TextView user_name;
        Button chat, accept, reject;


        public Holder(@NonNull View itemView) {
            super(itemView);
            user_img = itemView.findViewById(R.id.user_img);
            user_name =  itemView.findViewById(R.id.user_name);
            chat =  itemView.findViewById(R.id.chatBtn);
            accept =  itemView.findViewById(R.id.acceptBtn);
            reject =  itemView.findViewById(R.id.rejectBtn);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
