package es.fempa.juanpomares.sockets;

import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class ClaseChat extends AppCompatActivity {
// NOTA IMPORTANTE: el servidor tiene que ser siempre un teléfono móvil, y se debe compartir su red
// de datos con compartir wifi, donde se conectará el ordenador y con un emulador se conectará como
// cliente

    // variables
    TextView textViewChat;
    Button btncliente, btnservidor;
    EditText ipServer;

    Socket socket;
    ServerSocket serverSocket;
    boolean ConectionEstablished;

    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    //Hilo para escuchar los mensajes que le lleguen por el socket
    GetMessagesThread HiloEscucha;

    /*Variable para el servidor*/
    WaitingClientThread HiloEspera;

    Bundle datos;
    String ipServidor;
    String nombreCliente;
    String nombreServidor;
    int puertoServidor;
    boolean esServidor;

    LinearLayout verticalLinearLayout;
    ScrollView scrollView;

    // funcion onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clase_chat);

        //textViewChat = (TextView) findViewById(R.id.tvSalida);

        datos = getIntent().getExtras();
        esServidor = datos.getBoolean("soy servidor");

        verticalLinearLayout = (LinearLayout) findViewById(R.id.linearLayoutChatVertical);
        scrollView = (ScrollView) findViewById(R.id.scrollViewChat);

        if (esServidor == true) { // es el servidor
            // Regogemos en variables los datos que nos pasa ClaseServidor
            nombreServidor = datos.getString("nombre del servidor");
            puertoServidor = datos.getInt("puerto del servidor");
            startServer();

        } else { // es el cliente
            // Recogemos en variables los datos que nos pasa ClaseCliente
            ipServidor = datos.getString("ip del servidor");
            puertoServidor = datos.getInt("puerto del servidor");
            nombreCliente = datos.getString("nombre del cliente");
            startClient();
        }

    }

    // funcion enviarMensaje
    // boton del chat ENVIAR donde se envia el mensaje e inicializa a cero el textview
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void enviarMensaje(View v) {
        EditText et = (EditText) findViewById(R.id.editTextMensajes); // editTextMensajes es el cuadro donde se escribe el mensaje
        sendMessage(et.getText().toString());

// TODO: mensajes escritos
/*
        TextView textViewNuevoTexto = new TextView(ClaseChat.this, null, 0, R.style.bocadilloEnviado);
        textViewNuevoTexto.setText(et.getText().toString());
        linearLayout.addView(textViewNuevoTexto, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
*/

        TextView textViewNuevoTexto = new TextView(ClaseChat.this, null, 0, R.style.bocadilloEnviado);
        textViewNuevoTexto.setText(et.getText().toString());
        LinearLayout horizontalLinearLayout = new LinearLayout(this);
        horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        //truco
        TextView invisibleTV = new TextView(ClaseChat.this);
        invisibleTV.setWidth(500);
        horizontalLinearLayout.addView(invisibleTV);
        horizontalLinearLayout.addView(textViewNuevoTexto);
        verticalLinearLayout.addView(horizontalLinearLayout);
        // Para que el texto esté a la derecha como mensaje enviado
        textViewNuevoTexto.setGravity(Gravity.END);

        // Para que el texto esté a la derecha como mensaje enviado
        textViewNuevoTexto.setGravity(Gravity.END);

        // Mostramos el último mensaje añadido como en Whatsapp
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        et.setText("");
    }

    public void startServer() {
        SetText("\nBienvenido a la app Chat Sockets. Let's go chat! \nComenzamos Servidor!");
        (HiloEspera = new WaitingClientThread()).start();
    }

    public void startClient() {
        (new ClientConnectToServer(ipServidor)).start();
        SetText("\nComenzamos Cliente!");
        AppenText("\nNos intentamos conectar al servidor: " + ipServidor);
    }

    public void AppenText(String text) {
        runOnUiThread(new appendUITextView(text));

    }

    public void SetText(String text) {
        runOnUiThread(new setUITextView(text));
    }

    private class WaitingClientThread extends Thread {
        public void run() {
            SetText("Esperando Usuario...");
            try {
                //Abrimos el socket
                serverSocket = new ServerSocket(puertoServidor);

                //Mostramos un mensaje para indicar que estamos esperando en la direccion ip y el puerto...
                AppenText("Creado el servidor\n Dirección: " + getIpAddress() + " Puerto: " + serverSocket.getLocalPort());

                //Creamos un socket que esta a la espera de una conexion de cliente
                // Espera, y si se conecta un cliente continua ejecutando el código
                socket = serverSocket.accept();

                //Una vez hay conexion con un cliente, creamos los streams de salida/entrada
                try {
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ConectionEstablished = true;

                //Iniciamos el hilo para la escucha y procesado de mensajes
                (HiloEscucha = new GetMessagesThread()).start();

                //Enviamos mensajes desde el servidor.

                // Comentamos esta línea que era la que enviaba automáticamente mensajes en este caso el servidor

                //(new EnvioMensajesServidor()).start();
                HiloEspera = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ClientConnectToServer extends Thread {
        String mIp;

        public ClientConnectToServer(String ip) {
            mIp = ip;
        }

        public void run() {

            try {
                SetText("Conectando con el servidor: " + mIp + ":" + puertoServidor + "...\n\n");//Mostramos por la interfaz que nos hemos conectado al servidor} catch (IOException e) {

                socket = new Socket(mIp, puertoServidor);//Creamos el socket

                try {
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ConectionEstablished = true;
                //Iniciamos el hilo para la escucha y procesado de mensajes
                (HiloEscucha = new GetMessagesThread()).start();

                // Comentamos esta línea que era la que enviaba automáticamente mensajes en este caso el cliente

                //new EnvioMensajesCliente().start();

            } catch (Exception e) {
                e.printStackTrace();
                AppenText("Error: " + e.getMessage());
            }
        }

    }

/*
    private class EnvioMensajesServidor extends Thread
    {
        public void run()
        {
            String messages[]={"Bienvenido usuario a mi chat", "¿Estás bien?", "Bueno, pues molt bé, pues adiós"};
            int sleeptime[]={1000, 2000, 2000};
            sendVariousMessages(messages, sleeptime);
            DisconnectSockets();
        }
    }


    private class EnvioMensajesCliente extends Thread {
        public void run() {
            String messages[] = {"Hola servidor", "No mucho, pero no te voy a contar mi vida", "Pues adiós :("};
            int sleeptime[] = {1000, 2000, 1000};
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendVariousMessages(messages, sleeptime);
            DisconnectSockets();
        }
    }*/

    private void DisconnectSockets() {
        if (ConectionEstablished) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btncliente.setEnabled(true);
                    btnservidor.setEnabled(true);
                    ipServer.setEnabled(true);
                }
            });
            ConectionEstablished = false;

            if (HiloEscucha != null) {
                HiloEscucha.setExecuting(false);
                HiloEscucha.interrupt();
                HiloEspera = null;
            }

            try {
                if (dataInputStream != null)
                    dataInputStream.close();
            } catch (Exception e) {
            } finally {
                dataInputStream = null;
                try {
                    if (dataOutputStream != null)
                        dataOutputStream.close();
                } catch (Exception e) {
                } finally {
                    dataOutputStream = null;
                    try {
                        if (socket != null)
                            socket.close();
                    } catch (Exception e) {
                    } finally {
                        socket = null;
                    }
                }
            }
        }
    }

    /*
        private void sendVariousMessages(String[] msgs, int[] time) {
            if (msgs != null && time != null && msgs.length == time.length)
                for (int i = 0; i < msgs.length; i++) {
                    sendMessage(msgs[i]);
                    try {
                        Thread.sleep(time[i]);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
        }
    */
    private void sendMessage(String txt) {
        new SendMessageSocketThread(txt).start();
    }

    private class SendMessageSocketThread extends Thread {
        private String msg;


        SendMessageSocketThread(String message) {
            msg = message;
        }

        @Override
        public void run() {
            try {
                dataOutputStream.writeUTF(msg);//Enviamos el mensaje
                //dataOutputStream.close();
                //AppenText("Enviado: " + msg);
            } catch (IOException e) {
                e.printStackTrace();
                //message += "¡Algo fue mal! " + e.toString() + "\n";
            }
        }
    }

    //Aqui obtenemos la IP de nuestro terminal
    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces.nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface.getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "IP de Servidor: " + inetAddress.getHostAddress() + "\n";
                    }

                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            ip += "¡Algo fue mal! " + e.toString() + "\n";
        }

        return ip;
    }

    private class GetMessagesThread extends Thread { // funcion que recibe el mensaje
        public boolean executing;
        private String line;


        public void run() {
            executing = true;

            while (executing) { // está escuchando todoo el tiempo
                line = "";
                line = ObtenerCadena();//Obtenemos la cadena del buffer
                if (!line.equals("") && line.length() != 0) {//Comprobamos que esa cadena tenga contenido
                    AppenText(line);//Procesamos la cadena recibida

                }
            }
        }

        public void setExecuting(boolean execute) {
            executing = execute;
        }


        private String ObtenerCadena() {
            String cadena = "";

            try {
                cadena = dataInputStream.readUTF();//Leemos del datainputStream una cadena UTF
                Log.d("ObtenerCadena", "Cadena reibida: " + cadena);

            } catch (Exception e) {
                e.printStackTrace();
                executing = false;
            }
            return cadena;
        }
    }

    protected class setUITextView implements Runnable {
        private String text;

        public setUITextView(String text) {
            this.text = text;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void run() {
            // TODO: mensajes enviados
            //textViewChat.setText(text);

            TextView textViewNuevoTexto = new TextView(ClaseChat.this, null, 0, R.style.bocadilloConsola);
            textViewNuevoTexto.setText(text);
            LinearLayout horizontalLinearLayout = new LinearLayout(ClaseChat.this);
            horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            horizontalLinearLayout.addView(textViewNuevoTexto);
            verticalLinearLayout.addView(horizontalLinearLayout);

            textViewNuevoTexto.setGravity(Gravity.CENTER);
// Mostramos el último mensaje añadido como en Whatsapp
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }

    protected class appendUITextView implements Runnable {
        private String text;

        public appendUITextView(String text) {
            this.text = text;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void run() {

            //textViewChat.append(text);
// TODO: mensajes recibidos

            TextView textViewNuevoTexto = new TextView(ClaseChat.this, null, 0, R.style.bocadilloRecibido);
            textViewNuevoTexto.setText(text);
            LinearLayout horizontalLinearLayout = new LinearLayout(ClaseChat.this);
            horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            horizontalLinearLayout.addView(textViewNuevoTexto);
            verticalLinearLayout.addView(horizontalLinearLayout);
            textViewNuevoTexto.setGravity(Gravity.START);

            // Mostramos el último mensaje añadido como en Whatsapp
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DisconnectSockets();
    }

}
