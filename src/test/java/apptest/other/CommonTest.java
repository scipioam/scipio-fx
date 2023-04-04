package apptest.other;

import org.junit.jupiter.api.Test;

/**
 * @since 2022/6/30
 */
public class CommonTest {

    @Test
    public void test0() {
        StringBuilder s = new StringBuilder("from sys where a=1 and b=2 and c=3 and ");
        s.delete(s.length() - 6, s.length() - 1);
        System.out.println(s);
    }

}
