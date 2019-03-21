package proxy;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 10:01 2019/3/21
 */
public class RealSubject implements Subject {

    @Override
    public void doSomething() {
        System.out.println("doSomething");
    }

}

