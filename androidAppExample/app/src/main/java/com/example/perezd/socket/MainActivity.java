package com.example.perezd.socket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {
    private Socket msocket;
    private EditText msg;
    private TextView textRes;
    private EditText txtres;
    {
        try{
            msocket= IO.socket("http://172.16.3.234:90");
        }catch(URISyntaxException e){ }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        msg=(EditText)findViewById(R.id.txtmsg);
        textRes=(TextView)findViewById(R.id.lbres);
        txtres=(EditText)findViewById(R.id.txtres);
        msocket.on("mensaje",nuevoMensaje);
        msocket.connect();
    }

    public void enviar(View v){
        String mensaje=msg.getText().toString();

        Mensaje ms=new Mensaje();
        ms.nombre="Daniel";
        ms.origen="Celular";
        ms.texto=mensaje;
        ms.clienteId= txtres.getText().toString().equals("")?"Todos":txtres.getText().toString();

        Gson gson=new Gson();
        msocket.emit("mensaje",gson.toJson(ms));
        msg.setText("");
        Toast.makeText(this,"mensaje enviado",Toast.LENGTH_LONG).show();
    }

    private Emitter.Listener nuevoMensaje=new Emitter.Listener(){
        public void call(final Object... args){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textRes.setText("");

                    Gson gson=new Gson();
                    Mensaje ms=gson.fromJson(args[0].toString(),Mensaje.class);
                    textRes.setText(ms.nombre+": "+ms.texto);
                }
            });
        }
    };
}
