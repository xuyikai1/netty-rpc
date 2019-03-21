package proxy;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 10:02 2019/3/21
 */
public class Test {

    public static void main(String[] args) {
        ProxyHandler proxy = new ProxyHandler();
        //绑定该类实现的所有接口
        Subject sub = (Subject) proxy.bind(new RealSubject());
        sub.doSomething();

    }
}
