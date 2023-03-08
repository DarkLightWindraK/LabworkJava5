import java.io.IOException;

public class MyClass {
    @AutoInjectable
    private MyDependency myDependency;

    public void doSomething() {
        myDependency.doWork();
    }
}