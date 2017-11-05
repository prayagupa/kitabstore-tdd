import com.beard.BeardDownload;
import groovy.lang.Closure;

public class JavaCanCallGroovy {

    public static void main(String[] args) {

        BeardDownload b = new BeardDownload();

        Integer mutableState = 0;
        Integer data = b.doSomething(new Closure<Integer>(null) {
            public Integer call() {
                return 100;
            }
        });


        System.out.println(data);
    }
}
