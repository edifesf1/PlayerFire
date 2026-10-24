package br.com.edifesf.iptv;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.content.Intent;
import android.net.Uri;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Gravity;
import android.graphics.Color;
import android.graphics.Typeface;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    private static final String LINK_TXT =
        "https://raw.githubusercontent.com/edifesf1/r/refs/heads/main/link.txt";
    private final Handler handler = new Handler(Looper.getMainLooper());
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mostrarTelaInicial();
        carregarLink();
    }

    private void mostrarTelaInicial() {
        getWindow().getDecorView().setSystemUiVisibility(
            android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
            | android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setBackgroundColor(Color.BLACK);

        TextView logoText = new TextView(this);
        logoText.setText("⚡\nIPTV");
        logoText.setTextColor(Color.WHITE);
        logoText.setTextSize(48);
        logoText.setTypeface(Typeface.DEFAULT_BOLD);
        logoText.setGravity(Gravity.CENTER);

        status = new TextView(this);
        status.setText("EDIFESF - IPTV\nConectando...");
        status.setTextColor(Color.YELLOW);
        status.setTextSize(18);
        status.setGravity(Gravity.CENTER);
        status.setPadding(20, 20, 20, 20);

        layout.addView(logoText);
        layout.addView(status);
        setContentView(layout);
    }

    private void carregarLink() {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(LINK_TXT);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(12000);
                connection.setReadTimeout(12000);
                connection.setRequestMethod("GET");
                connection.setInstanceFollowRedirects(true);

                int code = connection.getResponseCode();
                if (code < 200 || code >= 300) {
                    throw new Exception("GitHub respondeu HTTP " + code);
                }

                StringBuilder content = new StringBuilder();
                try (InputStream in = connection.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String trimmed = line.trim();
                        if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
                            content.append(trimmed);
                            break;
                        }
                    }
                }

                String address = content.toString().trim();
                if (!(address.startsWith("https://") || address.startsWith("http://"))) {
                    throw new Exception("O link.txt precisa conter um endereço http:// ou https:// válido.");
                }

                final String finalAddress = address;
                handler.postDelayed(() -> abrirNoSilk(finalAddress), 700);
            } catch (Exception e) {
                String message = e.getMessage() == null ? "Erro ao consultar link.txt" : e.getMessage();
                handler.post(() -> status.setText("Não foi possível abrir o endereço.\n" + message
                    + "\n\nConfira o conteúdo do link.txt e a conexão."));
            } finally {
                if (connection != null) connection.disconnect();
            }
        }).start();
    }

    private void abrirNoSilk(String address) {
        Uri uri = Uri.parse(address);
        try {
            Intent silkIntent = new Intent(Intent.ACTION_VIEW, uri);
            silkIntent.setPackage("com.amazon.cloud9");
            silkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(silkIntent);
        } catch (Exception silkError) {
            try {
                Intent genericIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(genericIntent);
            } catch (Exception e) {
                status.setText("Não encontrei um navegador para abrir o link.\nInstale/ative o Amazon Silk.");
            }
        }
    }
}
