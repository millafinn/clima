package org.example;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Timer;

public class Main extends JFrame {

    public static void main(String[] args) {
        new Main();
    }
    private JLabel temperarura;
    private JLabel umidade;
    private JLabel valorClima;
    private JLabel atualizado;

    private void realizarPesquisa(String cidade) {
        if (cidade != null && !cidade.isEmpty()) {
            try {
                String apiKey = "08a9f1f7f02c24b51000b32e1d8199f8";
                String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + cidade + "&appid=" + apiKey;

                URL url = new URL(apiUrl);
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
                conexao.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                JSONObject jsonObject = new JSONObject(response.toString());
                JSONObject main = jsonObject.getJSONObject("main");
                double temperatura = main.getDouble("temp") - 273.15;
                int umidade1 = main.getInt("humidity");

                temperarura.setText("Temperatura: " + String.format("%.2f", temperatura) + " °C");
                umidade.setText("Umidade: " + umidade1 + "%");
                valorClima.setText("Cidade: " + cidade);
                atualizarHora();

            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao buscar dados meteorológicos. Por favor, tente novamente.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor insira o nome de uma cidade.");
        }

        int delay = 30000;
        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarHora();
                realizarPesquisa(cidade);
            }
        });
        timer.start();
    }
    public Main() {
        setTitle("Temperatura e umidade");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        temperarura = new JLabel("Temperatura: ");
        umidade = new JLabel("Umidade: ");
        valorClima = new JLabel("Cidade:");
        atualizado = new JLabel("Atualizado às: ");

        JButton botao = new JButton("Obter clima");

        botao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cidade = JOptionPane.showInputDialog("Insira o nome da cidade:");
                realizarPesquisa(cidade);
            }
        });

        panel.add(botao);
        panel.add(temperarura);
        panel.add(umidade);
        panel.add(valorClima);
        panel.add(atualizado);

        add(panel);
        setVisible(true);
    }
    private void atualizarHora() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String hora = dateFormat.format(new Date());
        atualizado.setText("Atualizado às: " + hora);
    }
}