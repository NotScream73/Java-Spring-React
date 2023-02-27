package ip.labwork.method.domain;

import org.springframework.stereotype.Component;

@Component(value="int")
public class MethodInt implements IMethod<Integer>{
    public Integer Sum(Integer first, Integer second) {
        return Integer.parseInt(first.toString()) + Integer.parseInt(second.toString());
    }

    public Integer Multiply(Integer first, Integer second) {
        return Integer.parseInt(first.toString()) * Integer.parseInt(second.toString());
    }

    public Integer Minus(Integer first, Integer second) {
        return Integer.parseInt(first.toString()) - Integer.parseInt(second.toString());
    }

    public Integer Div(Integer first, Integer second) {
        int num = Integer.parseInt(second.toString());
        if (num == 0){
            return null;
        }else{
            return Integer.parseInt(first.toString()) / num;
        }
    }
}
