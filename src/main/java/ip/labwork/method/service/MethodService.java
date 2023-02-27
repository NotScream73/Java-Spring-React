package ip.labwork.method.service;

import ip.labwork.method.domain.IMethod;
import ip.labwork.method.domain.MethodString;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class MethodService {
    private final ApplicationContext applicationContext;

    public MethodService(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public String Sum(Object first, Object second, String type) {
        final IMethod speaker = (IMethod) applicationContext.getBean(type);
        if (speaker instanceof MethodString){
            return String.format("%s", speaker.Sum(first,second));
        }else{
            return String.format("%s", speaker.Sum(Integer.parseInt(first.toString()),Integer.parseInt(second.toString())));
        }
    }

    public String Ras(Object first, Object second, String type) {
        final IMethod speaker = (IMethod) applicationContext.getBean(type);
        if (speaker instanceof MethodString){
            return String.format("%s", speaker.Minus(first,Integer.parseInt(second.toString())));
        }else{
            return String.format("%s", speaker.Minus(Integer.parseInt(first.toString()),Integer.parseInt(second.toString())));
        }
    }

    public String Pros(Object first, Object second, String type) {
        final IMethod speaker = (IMethod) applicationContext.getBean(type);
        if (speaker instanceof MethodString){
            return String.format("%s", speaker.Multiply(first,Integer.parseInt(second.toString())));
        }else{
            return String.format("%s", speaker.Multiply(Integer.parseInt(first.toString()),Integer.parseInt(second.toString())));
        }
    }

    public String Del(Object first, Object second, String type) {
        final IMethod speaker = (IMethod) applicationContext.getBean(type);
        if (speaker instanceof MethodString){
            return String.format("%s", speaker.Div(first,second));
        }else {
            return String.format("%s", speaker.Div(Integer.parseInt(first.toString()), Integer.parseInt(second.toString())));
        }
    }
}
