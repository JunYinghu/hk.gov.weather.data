package request.binary;

import java.util.Locale;

public class BinaryExecution {

    public void getCurrentRunOS(){
        if (System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("win")){
            System.out.println("u r running on win");
        };

    }

    public void binaryExec(){

    }




}
