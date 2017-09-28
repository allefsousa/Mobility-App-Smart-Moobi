package developer.allef.smartmobi.smartmobii.Presenter;

import android.content.Context;
import android.content.Intent;


import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import developer.allef.smartmobi.smartmobii.Interfaces.SplashInterface;
import developer.allef.smartmobi.smartmobii.Model.LocalVaga;
import developer.allef.smartmobi.smartmobii.View.Sing_in;

/**
 * Created by Allef on 07/05/2017.
 */

public class PresenterSplash implements SplashInterface.Presenter {

    private SplashInterface.View view;

    private DatabaseReference bd; // Objeto do Banco de dados
    private LocalVaga local;      // Objeto Vaga
    ArrayList<LocalVaga> arrayLocal = new ArrayList<>();


    public PresenterSplash(SplashInterface.View view) {
        this.view = view;
    }

    @Override
    public void BuscandoLocalizacao() {

       conexaoBD();
//
//        bd.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot dado : dataSnapshot.getChildren()) {
//                    local = dado.getValue(LocalVaga.class);
//                    // Toast.makeText(SplashInterface.this, "Mensagem" + local.getLatitude() + "" + local.getLongitude(), Toast.LENGTH_LONG).show();
//                    arrayLocal.add(local);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        conexaoBD();

//        bd.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot dado : dataSnapshot.getChildren()) {
//                    local = dado.getValue(LocalVaga.class);
//                    // Toast.makeText(SplashInterface.this, "Mensagem" + local.getLatitude() + "" + local.getLongitude(), Toast.LENGTH_LONG).show();
//                    arrayLocal.add(local);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }

//    @Override
   public void conexaoBD() {

//
//        bd = FirebaseDatabase.getInstance().getReference().child("eendereco"); // pegando a referencia do Banco de dados
   }


    @Override
    public void verificaArray() {
//        if(!arrayLocal.isEmpty()){
      Intent it = new Intent((Context) view, Sing_in.class);
//            it.putExtra("P", arrayLocal);
           view.StartProximaActivity(it);
//        }
    }
}
