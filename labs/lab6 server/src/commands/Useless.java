package commands;

import java.io.Serial;
import java.io.Serializable;

//public class Useless implements Serializable {
//    @Serial
//    private static final long serialVersionUID = 1L;
//    private String answer;
//    public Useless(String answer){
//        this.answer = answer;
//    }
//    public String getAnswer(){
//        return answer;
//    }
//}
import java.io.Serial;
import java.io.Serializable;

public class Useless implements Serializable {
    private String answer;
    private Integer userId;

    @Serial
    private static final long serialVersionUID = 1L;

    public Useless(String answer) {
        this.answer = answer;
    }


    public String getAnswer() {
        return answer;
    }

    public Integer getUserId() {
        return userId;
    }
}