import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Scanner;
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException, MalformedURLException {
        String menuPrincipal;
        int opcaoMenu = 0;
        double valorInformado;

        String endereco_api = "https://v6.exchangerate-api.com/v6/";
        String chave_api = "_INSIRA_AQUI_SUA_CHAVE_";

        String url_completo = endereco_api + chave_api + "/pair";

        Scanner leitura = new Scanner(System.in);

        String[][] matriz = new String[6][4];
        menuPrincipal = """
                -----------------------------------------
                Seja  bem vindo(a) ao Conversor de Moedas
                -----------------------------------------
                --> Menu Principal
                -----------------------------------------
                """;


        // Dólar -=-> Peso Argentino
        matriz[0][0] = "USD";
        matriz[0][1] = "Dólar";
        matriz[0][2] = "ARS";
        matriz[0][3] = "Peso Argentino";


        // Peso argentino -=-> Dólar
        matriz[1][0] = "ARS";
        matriz[1][1] = "Peso Argentino";
        matriz[1][2] = "USD";
        matriz[1][3] = "Dóllar";

        // Dólar -=-> Real Brasileiro
        matriz[2][0] = "USD";
        matriz[2][1] = "Dóllar";
        matriz[2][2] = "BRL";
        matriz[2][3] = "Real Brasileiro";

        // Real Brasileiro -=-> Dólar
        matriz[3][0] = "BRL";
        matriz[3][1] = "Real Brasileiro";
        matriz[3][2] = "USD";
        matriz[3][3] = "Dóllar";

        // Dólar -=-> Peso colombiano
        matriz[4][0] = "USD";
        matriz[4][1] = "Dóllar";
        matriz[4][2] = "COP";
        matriz[4][3] = "Peso colombiano";

        // Peso colombiano -=-> Dólar
        matriz[5][0] = "COP";
        matriz[5][1] = "Peso colombiano";
        matriz[5][2] = "USD";
        matriz[5][3] = "Dóllar";



        while (opcaoMenu != matriz.length ) {

            valorInformado = 0.0;

            while (true) {

                System.out.println(menuPrincipal);

                // Imprime todos os elementos da matriz
                for (int contador = 0; contador < matriz.length; contador++) {
                    System.out.println(contador + ") " + matriz[contador][1] + " ---> " + matriz[contador][3]);
                }
                System.out.println("\n-----------------------------------------\n" +
                        matriz.length +
                        ") Sair\n-----------------------------------------");




                try {
                    opcaoMenu =  Integer.parseInt(leitura.nextLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Por favor, insira um número inteiro válido.");
                    Thread.sleep(2000);

                }


            }



            if (opcaoMenu < matriz.length ) {
                while (true) {
                    System.out.println("\n******\nSerá convertido para \n" +
                            opcaoMenu + ") de "+
                            matriz[opcaoMenu][1] + " para " + matriz[opcaoMenu][3] +"\n******\n\nInforme o Valor para ser convertido");

                    try {
                        valorInformado = Double.parseDouble(leitura.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("\n****\nPor favor, insira um número válido.");
                        continue;
                    }

                    if (valorInformado > 0.0) {
                        break;
                    }

                }

                System.out.println("\n****\n");

                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat formato = new DecimalFormat("#.00", symbols);
                String valorFormatado = formato.format(valorInformado);

                String url_final = url_completo + "/" +matriz[opcaoMenu][0] + "/" + matriz[opcaoMenu][2] + "/" + valorFormatado;

                // Fazendo a requisição
                URL url = new URL(url_final);
                HttpURLConnection request = null;
                try {
                    request = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    request.connect();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Convertendo para JSON
                JsonParser jp = new JsonParser();
                JsonElement root = null;
                try {
                    root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                JsonObject jsonobj = root.getAsJsonObject();

                // Acessando o objeto
                double conversion_result = jsonobj.get("conversion_result").getAsDouble();

                if ( !root.isJsonNull() ) {
                    System.out.println( " de " + valorFormatado + " " + matriz[opcaoMenu][1]  +
                            " convertido para " +  matriz[opcaoMenu][3] + " " + conversion_result);

                    System.out.println("\n****");
                    Thread.sleep(8000);

                }



            }
            else {
                if (opcaoMenu > matriz.length) {

                    System.out.println("\n******\nOPÇÃO INVÁLIDA\n******");

                    Thread.sleep(2000);

                }

            }



        }


    }
}