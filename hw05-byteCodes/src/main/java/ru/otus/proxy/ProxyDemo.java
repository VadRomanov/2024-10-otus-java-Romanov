package ru.otus.proxy;

public class ProxyDemo {
  public static void main(String[] args) {
    TestLoggingInterface myClass = Ioc.createMyClass();
    myClass.calculation(10);
    myClass.calculation(10, 20);
    myClass.calculation(10, 20, "30");
    myClass.calculation(10, 20, 40);
    myClass.calculation();
  }
}
