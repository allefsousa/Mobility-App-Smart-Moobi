package developer.allef.smartmobi.smartmobii.View;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import developer.allef.smartmobi.smartmobii.Adapters.FeedAdapter;
import developer.allef.smartmobi.smartmobii.R;

public class FeedActivity extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    FeedAdapter feedAdapter;
    FloatingActionButton fab;
    private Toolbar tool;
    @BindColor(R.color.primary_text)
    int corPreta;
    @BindDrawable(R.drawable.ic_voltar)
    Drawable Imagevoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ButterKnife.bind(this);

        tool = (Toolbar) findViewById(R.id.too);
        setSupportActionBar(tool);
        getSupportActionBar().setTitle("Linha do Tempo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(Imagevoltar);
        tool.setBackgroundColor(corPreta);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        feedAdapter = new FeedAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(FeedActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(feedAdapter);



        DatabaseReference db = FirebaseDatabase.getInstance().getReference("feed");
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
                feedAdapter.addItem(dataSnapshot);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildKey) {
                feedAdapter.addItem(dataSnapshot);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                feedAdapter.removeItem(dataSnapshot);
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildKey) {
                feedAdapter.addItem(dataSnapshot);
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
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//

                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy >0) {
                    // Scroll Down
                    if (fab.isShown()) {
                        fab.hide();
                    }
                }
                else if (dy <0) {
                    // Scroll Up
                    if (!fab.isShown()) {
                        fab.show();
                    }
                }
            }
        });

    }



}
