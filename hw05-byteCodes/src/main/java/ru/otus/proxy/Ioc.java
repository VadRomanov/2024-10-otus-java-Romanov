package ru.otus.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class Ioc {
  private static final Logger logger = LoggerFactory.getLogger(Ioc.class);

  private Ioc() {
  }

  static TestLoggingInterface createMyClass() {
    InvocationHandler handler = new DemoInvocationHandler(new TestLogging());
    return (TestLoggingInterface)
        Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[]{TestLoggingInterface.class}, handler);
  }

  static class DemoInvocationHandler implements InvocationHandler {
    private final Object myClass;
    private final Map<Method, Method> methods;

    DemoInvocationHandler(Object myClass) {
      this.myClass = myClass;
      this.methods = new HashMap<>();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      logMethodNameAndParamsIfNeeded(method, args);
      return method.invoke(myClass, args);
    }

    private void logMethodNameAndParamsIfNeeded(Method method, Object[] args) throws NoSuchMethodException {
      var methodImpl = getImplMethod(method);
      if (methodImpl.isAnnotationPresent(Log.class)) {
        logger.info("executed method: {}{}",
            method.getName(),
            args == null ? "" :
                ", %s: %s".formatted(args.length > 1 ? "params" : "param", Arrays.toString(args))
        );
      }
    }

    private Method getImplMethod(Method method) throws NoSuchMethodException {
      var methodImpl = methods.get(method);
      if (methodImpl == null) {
        methodImpl = myClass.getClass().getMethod(method.getName(), method.getParameterTypes());
        methods.put(method, methodImpl);
      }
      return methodImpl;
    }
  }
}
