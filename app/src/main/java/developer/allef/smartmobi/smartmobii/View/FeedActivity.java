package developer.allef.smartmobi.smartmobii.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import developer.allef.smartmobi.smartmobii.R;

public class FeedActivity extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    FloatingActionButton fab;
    private Toolbar tool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        tool = (Toolbar) findViewById(R.id.too);
        setSupportActionBar(tool);
        getSupportActionBar().setTitle("Time Line");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_voltar);
        tool.setBackgroundColor(getResources().getColor(R.color.primaryDarkColor));

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mainAdapter = new MainAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(FeedActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mainAdapter);



        DatabaseReference db = FirebaseDatabase.getInstance().getReference("feed");
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
                mainAdapter.addItem(dataSnapshot);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildKey) {
                mainAdapter.addItem(dataSnapshot);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                mainAdapter.removeItem(dataSnapshot);
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildKey) {
                mainAdapter.addItem(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



         fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FeedActivity.this, NewPostActivity.class));
            }
        });

    }



}
