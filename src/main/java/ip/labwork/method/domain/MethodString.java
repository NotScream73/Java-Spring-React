package ip.labwork.method.domain;

import org.springframework.stereotype.Component;

@Component(value="string")
public class MethodString implements IMethod<String>{
    @Override
    public String Sum(String first, String second) {
        return first.concat(second);
    }

    @Override
    public String Multiply(String first, Integer second) {
        if (second != 0){
            String temp = "";
            for (int i = 0; i < second; i++){
                temp = temp.concat(first);
            }
            return temp;
        }
        else{
            return first;
        }
    }

    @Override
    public String Minus(String first, Integer second) {
        String temp = first;
        if(temp.length() >= second){
            return temp.substring(0, first.length() - second);
        }else{
            return first;
        }
    }

    @Override
    public String Div(String first, String second) {
        if (first.contains(second)){
            return "true";
        }else{
            return "false";
        }
    }
}