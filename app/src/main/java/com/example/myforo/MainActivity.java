package com.example.myforo;

import static android.content.Intent.ACTION_GET_CONTENT;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import javax.net.ssl.SSLSessionBindingEvent;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private CircleImageView fotoPerfil;
    private TextView nombre;
    private RecyclerView rvMensajes;
    private EditText txtMensaje;
    private Button btnEnviar;
    private ImageButton btnEnviarFoto;

    public static int RC_PHOTO_PICKER = 0;


    private AdaptadorMensaje adaptador;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage store;
    private StorageReference referencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fotoPerfil = (CircleImageView) findViewById(R.id.ftoPerfil);
        nombre = (TextView) findViewById(R.id.nombre);
        rvMensajes = (RecyclerView) findViewById(R.id.rvMensajes);
        txtMensaje = (EditText) findViewById(R.id.txtMensajes);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        btnEnviarFoto = (ImageButton) findViewById(R.id.btnEnviarFoto);



        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("chat"); //Sala de chat
        store = FirebaseStorage.getInstance();

        adaptador = new AdaptadorMensaje(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(l);
        rvMensajes.setAdapter(adaptador);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.push().setValue(new Mensaje(txtMensaje.getText().toString(), nombre.getText().toString(),"","1","00:00"));
                txtMensaje.setText("");
            }
        });

        btnEnviarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirAlgunaActividadPorResultado(view);
            }
        });

        adaptador.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Mensaje m = snapshot.getValue(Mensaje.class);
                adaptador.addMensaje(m);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void abriendo(){


    }

    public void abrirAlgunaActividadPorResultado(View view){
        ActivityResultLauncher<Intent> algunaActividadPorResultado = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.P)
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            //no hay codigo de respuesta
                            Intent i = result.getData();
                            Uri u = i.getData();
                            referencia = store.getReference(""); // imagenes del chat...
                            final StorageReference fotoRef = referencia.child(u.getLastPathSegment());
                            fotoRef.putFile(u).addOnSuccessListener(getApplicationContext().getMainExecutor(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                                    Mensaje m = new Mensaje("Lucas te ha enviado una foto",uri.toString(), nombre.getText().toString(), "", "2", "00:00");
                                    databaseReference.push().setValue(m);
                                }
                            });
                           // btnEnviarFoto = (ImageButton) findViewById(R.id.btnEnviarFoto);
                           // btnEnviarFoto.setImageURI(u);
                        }
                    }
                }
        );
        Intent i = new Intent(ACTION_GET_CONTENT); //Obtengo acceso a todo el contenido de la galeria
        i.setType("image/jpeg");
        i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        algunaActividadPorResultado.launch(i);

    }

    private void setScrollbar(){
        rvMensajes.scrollToPosition(adaptador.getItemCount()-1);
    }
}