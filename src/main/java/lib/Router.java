package lib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Router {
    private String className = null;
    private String methodName = null;
    private String[] parameters = null;

    private String getFullClassName() {
        return "models." + this.className;
    }

    /**
     * Uses Reflection to create an object of
     * specified class and invoke specified method.
     *
     * @return It can return generic object
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public Object route() throws ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException,
            InstantiationException
    {

        Class controllerClass = Class.forName(this.getFullClassName());
        Object controllerInstance = controllerClass.getConstructor().newInstance();

        Method method = controllerInstance.getClass().getMethod(this.methodName, String[].class);
        return method.invoke(controllerInstance, (Object) this.parameters);

    }

    public Router setClassName(String className) {
        this.className = className;
        return this;
    }

    public Router setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public Router setParameters(String[] parameters) {
        this.parameters = parameters;
        return this;
    }
}
