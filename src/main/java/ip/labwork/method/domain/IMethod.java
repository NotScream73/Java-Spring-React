package ip.labwork.method.domain;

public interface IMethod<T> {
    T Sum(T first, T second);

    T Multiply(T first, Integer second);

    T Minus(T first, Integer second);

    T Div(T first, T second);
}
