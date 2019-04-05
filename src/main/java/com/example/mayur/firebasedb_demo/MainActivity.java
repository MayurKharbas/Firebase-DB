package com.example.mayur.firebasedb_demo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText edt_title, edt_content;
    Button btn_post;
    RecyclerView recyclerView;

    //Firebase objects
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseRecyclerOptions<Post> options;
    FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_title = (EditText) findViewById(R.id.edt_title);
        edt_content = (EditText) findViewById(R.id.edt_content);
        btn_post = (Button) findViewById(R.id.btn_post);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("FIREBASE_DEMO");

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });

        displayComment();
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
                    protected void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position, @NonNull Post model) {
                        //Bind the post object to the ViewHolder
                        holder.txt_title.setText(model.getTitle());
                        holder.txt_content.setText(model.getContent());
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
