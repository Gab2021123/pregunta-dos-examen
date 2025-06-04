package File;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;

public class FormatFileService {

    public String readFileJson(String josnFilePath){
        try{
            String jsonContent = new String(Files.readAllBytes(Paths.get(josnFilePath)));
            JSONArray jsonArray = new JSONArray(jsonContent);

            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Boolean estado = jsonObject.getBoolean("estado");
                Integer nro_cuenta = jsonObject.getInt("nro_cuenta");
                Double saldo =jsonObject.getDouble("saldo");
                String banco = jsonObject.getString("banco");

                System.out.println("{"+
                        "Estado :" +estado+";"+
                        "Numero de Cuenta :"+ nro_cuenta + ";"+
                        "Saldo :"+ saldo +";"+
                        "Banco :" +banco +";"
                );
            }
            return jsonContent;
        } catch (Exception e) {
            return null;
        }
    }
}
