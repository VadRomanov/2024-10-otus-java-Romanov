package ru.otus.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final Object obj;
    private final Set<String> methodsToBeLogged;

    DemoInvocationHandler(Object obj) {
      this.obj = obj;
      this.methodsToBeLogged = getMethodsToBeLogged(obj.getClass());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      if (methodsToBeLogged.contains(method.getName() + Arrays.toString(method.getParameterTypes()))) {
        logMethodInvocation(method.getName(), args);
      }
      return method.invoke(obj, args);
    }

    private void logMethodInvocation(String methodName, Object[] args) {
      logger.info("executed method: {}{}",
          methodName,
          args == null ? "" :
              ", %s: %s".formatted(args.length > 1 ? "params" : "param", Arrays.toString(args))
      );
    }


    private Set<String> getMethodsToBeLogged(Class<?> clazz) {
      return Arrays.stream(clazz.getMethods())
          .filter(m -> m.isAnnotationPresent(Log.class))
          .map(m -> m.getName() + Arrays.toString(m.getParameterTypes()))
          .collect(Collectors.toSet());
    }
  }
}
