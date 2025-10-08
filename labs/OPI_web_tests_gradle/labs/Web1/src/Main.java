import com.fastcgi.FCGIInterface;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException {

//        Reader reader = new Reader();
//        System.out.println(reader.hello());
        FCGIInterface fcgiInterface = new FCGIInterface();
        while (fcgiInterface.FCGIaccept() >= 0){
//            Reader reader = new Reader();

            float paramX = 0;
            float paramY = 0;
            float paramR = 0;
            String str = FCGIInterface.request.params.getProperty("QUERY_STRING");
            String[] params = str.split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                String key = keyValue[0];
                float value = Float.parseFloat(keyValue[1]);
                switch (key) {
                    case "param1":
                        paramX = value;
                        break;
                    case "param2":
                        paramY = value;
                        break;
                    case "param3":
                        paramR = value;
                        break;
                }
            }
            String ans;
            if (paramX > 0 && Math.abs(paramY) < paramR && paramY < 0 && paramX < paramR) {
                ans = "Yes";
            } else if (paramX < 0 && (Math.abs(paramY) < paramR / 2) && paramY < 0 && Math.abs(paramX) < paramR) {
                ans = "Yes";
            } else if ((paramY >= -0.5 * paramX - paramR/2) && paramY < 0 && paramX < 0 ) {
                ans = "Yes";
            } else if ((paramX * paramX + paramY * paramY <= (0.5 * paramR) * (0.5 * paramR)) && paramY > 0 && paramX < 0 ) {
                ans = "Yes";
            } else {
                ans = "No";
            }

            var httpResponse = """
            HTTP/1.1 200 OK
            Content-Type: text/html
            Access-Control-Allow-Origin: *
            Content-Length: %d

            %s
            """.formatted(ans.getBytes(StandardCharsets.UTF_8).length, ans);

            System.out.println(httpResponse);

        }


    }


}