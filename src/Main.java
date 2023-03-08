import java.io.IOException;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, IOException, IllegalAccessException, InstantiationException {
        Injector injector = new Injector("dependencies.properties");

        MyClass myClass = new MyClass();
        injector.inject(myClass);
        myClass.doSomething(); // Output: "MyDependency doWork"
    }
}
