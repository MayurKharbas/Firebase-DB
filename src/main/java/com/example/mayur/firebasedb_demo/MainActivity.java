package com.example.mayur.firebasedb_demo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText edt_title, edt_content;
    Button btn_post, btn_update, btn_delete;
    RecyclerView recyclerView;

    //Firebase objects
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseRecyclerOptions<Post> options;
    FirebaseRecyclerAdapter adapter;

    Post selectedPost;
    String selectedKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_title = (EditText) findViewById(R.id.edt_title);
        edt_content = (EditText) findViewById(R.id.edt_content);
        btn_post = (Button) findViewById(R.id.btn_post);
        btn_update = (Button) findViewById(R.id.btn_update);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("FIREBASE_DEMO");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Called each time there is a new data snapshot.
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(selectedKey)
                        .setValue(new Post(edt_title.getText().toString(), edt_content.getText().toString()))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(selectedKey)
                        .removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        displayComment();
    }

    @Override
    protected void onStop() {
        if(adapter != null)
            adapter.stopListening();
        super.onStop();
    }

    private void postComment() {
        String title = edt_title.getText().toString();
        String content = edt_content.getText().toString();

        Post post = new Post(title, content);

        databaseReference.push().setValue(post);    //push() -  to create unique id for comment

        adapter.notifyDataSetChanged();
    }

    private void displayComment() {
        options =
                new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(databaseReference, Post.class)
                .build();

        adapter =
                new FirebaseRecyclerAdapter<Post, RecyclerViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position, @NonNull final Post model) {
                        //Bind the post object to the ViewHolder
                        holder.txt_title.setText(model.getTitle());
                        holder.txt_content.setText(model.getContent());


                        holder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                selectedPost = model;
                                selectedKey = getSnapshots().getSnapshot(position).getKey();
                                Log.d("Key item"," "+selectedKey);

                                //Bind data
                                edt_content.setText(model.getContent());
                                edt_title.setText(model.getTitle());
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        //Create a new instance of the ViewHolder
                        View itemView = LayoutInflater.from(getBaseContext()).inflate(R.layout.posts, viewGroup, false);
                        return new RecyclerViewHolder(itemView);
                    }
                };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
}
