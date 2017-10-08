package developer.allef.smartmobi.smartmobii.View;

import android.net.Uri;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import developer.allef.smartmobi.smartmobii.Model.Usuario;
import developer.allef.smartmobi.smartmobii.R;

/**
 * Created by allef on 28/08/2017.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    SortedList<DataSnapshot> dataset = new SortedList<DataSnapshot>(DataSnapshot.class, new SortedList.Callback<DataSnapshot>() {
        @Override
        public int compare(DataSnapshot data1, DataSnapshot data2) {
            return (int) (data2.child("createdAt").getValue(Long.class) - data1.child("createdAt").getValue(Long.class));
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(DataSnapshot oldItem, DataSnapshot newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(DataSnapshot item1, DataSnapshot item2) {
            return item1.getKey().equals(item2.getKey());
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }
    });

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("Recycler", "onCreateViewHolder");
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false));
    }

    @Override
    public void onBindViewHolder(MainAdapter.ViewHolder holder, int position) {
        holder.reset();
        holder.render(dataset.get(position));
    }

    public void addItem(DataSnapshot data) {
        dataset.add(data);
        notifyDataSetChanged();
    }

    public void removeItem(DataSnapshot data) {
        dataset.remove(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        DatabaseReference likedRef;
        ValueEventListener likeValueEventListener;
        DatabaseReference userRef;
        ValueEventListener userValueEventListener;
        Usuario uu = new Usuario();
        Usuario atualizauu = new Usuario();

        TextView textView;
        ImageView imageView;
        ImageView likeImageView;
        TextView likesCountTextView;
        CircleImageView fotoperfil;
        TextView nomeusurio;
        TextView dataUsuario;

        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.comment);
            imageView = (ImageView) v.findViewById(R.id.image);
            likeImageView = (ImageView) v.findViewById(R.id.like);
            likesCountTextView = (TextView) v.findViewById(R.id.likes_count);
            fotoperfil = (CircleImageView) v.findViewById(R.id.profile_image_feed);
            nomeusurio = (TextView) v.findViewById(R.id.feedNome);
            dataUsuario = (TextView) v.findViewById(R.id.datafeed);

        }

        public void reset() {
            if (likedRef != null) likedRef.removeEventListener(likeValueEventListener);
            textView.setText("");
            imageView.setImageResource(R.drawable.placeholder);
            likeImageView.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            likesCountTextView.setText("");
        }

        public void render(DataSnapshot dataSnapshot) {

            String userId = (dataSnapshot.child("userId").getValue(String.class));
            String photouri = (dataSnapshot.child("photoperfil").getValue(String.class));

            textView.setText(dataSnapshot.child("legenda").getValue(String.class));
            nomeusurio.setText(dataSnapshot.child("nomeUserPost").getValue(String.class));
            String dat = dataSnapshot.child("dataPost").getValue(String.class);
            String [] arraydat = dat.split("-");
            String nomeMes = null;
            int  mes = Integer.parseInt(arraydat[1]);
            switch (mes){
                case 1:
                    nomeMes = "Janeiro";
                    break;
                case 2:
                    nomeMes = "Fevereiro";
                    break;
                case 3:
                    nomeMes = "Março";
                    break;
                case 4:
                    nomeMes = "Abrir";
                    break;
                case 5:
                    nomeMes = "Maio";
                    break;
                case 6:
                    nomeMes = " Junho";
                    break;
                case 7:
                    nomeMes = " Julho";
                    break;
                case 8:
                    nomeMes = " Agosto";
                    break;
                case 9:
                    nomeMes = " Setembro";
                    break;
                case 10:
                    nomeMes = " Outubro";
                    break;
                case 11:
                    nomeMes = " Novembro";
                    break;
                case 12:
                    nomeMes = " Dezembro";
                    break;

            }
            String datafinal = arraydat[0]+nomeMes+" ,"+arraydat[2];
            dataUsuario.setText(datafinal);

            Picasso.with(itemView.getContext())
                    .load(dataSnapshot.child("imagem").getValue(String.class))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(imageView);

            Picasso.with(itemView.getContext())
                    .load(photouri)
                    .into(fotoperfil);


            int likesCount = dataSnapshot.child("contadorLikes").getValue(Integer.class);
            if (likesCount > 0) {
                likesCountTextView.setText("" + likesCount);
            }

            likedRef = FirebaseDatabase.getInstance().getReference("post_likes/" + userId + "/" + dataSnapshot.getKey());
            likeValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.getValue(Boolean.class)) {
                        likeImageView.setImageResource(R.drawable.ic_favorite_black_24dp);
                        likeImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final DatabaseReference postLikesCountRef = FirebaseDatabase.getInstance().getReference("feed/" + dataSnapshot.getKey() + "/contadorLikes");
                                postLikesCountRef.runTransaction(new Transaction.Handler() {
                                    @Override
                                    public Transaction.Result doTransaction(MutableData mutableData) {
                                        likedRef.setValue(false);
                                        postLikesCountRef.setValue(mutableData.getValue(Integer.class) - 1);
                                        return null;
                                    }

                                    @Override
                                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                                    }
                                });
                            }
                        });

                    } else {
                        likeImageView.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        likeImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final DatabaseReference postLikesCountRef = FirebaseDatabase.getInstance().getReference("feed/" + dataSnapshot.getKey() + "/contadorLikes");
                                postLikesCountRef.runTransaction(new Transaction.Handler() {
                                    @Override
                                    public Transaction.Result doTransaction(MutableData mutableData) {
                                        likedRef.setValue(true);
                                        postLikesCountRef.setValue(mutableData.getValue(Integer.class) + 1);
                                        return null;
                                    }

                                    @Override
                                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                    }
                                });
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            likedRef.addValueEventListener(likeValueEventListener);



        }

    }
}
