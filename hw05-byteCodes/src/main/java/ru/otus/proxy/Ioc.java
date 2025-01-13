package ru.otus.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

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
    private final TestLoggingInterface myClass;

    DemoInvocationHandler(TestLoggingInterface myClass) {
      this.myClass = myClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      logMethodNameAndParamsIfNeeded(method, args);
      return method.invoke(myClass, args);
    }

    private void logMethodNameAndParamsIfNeeded(Method method, Object[] args) throws NoSuchMethodException {
      var methodImpl = TestLogging.class.getMethod(method.getName(), method.getParameterTypes());
      if (methodImpl.isAnnotationPresent(Log.class)) {
        logger.info("executed method: {}{}",
            method.getName(),
            args == null ? "" :
                ", %s: %s".formatted(args.length > 1 ? "params" : "param", Arrays.toString(args))
        );
      }
    }
  }
}
