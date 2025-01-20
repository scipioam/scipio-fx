package scipiofx.test.bean;

import com.github.scipioam.scipiofx.utils.ReflectUtil;

import java.lang.reflect.Field;
import java.util.Comparator;

/**
 * 专门针对MaterialFX的{@link Person}里name字段的排序器
 *
 * @since 2022/6/14
 */
public class MFXPersonNameComparator implements Comparator<Person> {

    private final Field field;

    public MFXPersonNameComparator(Field field) {
        this.field = field;
    }

    @Override
    public int compare(Person o1, Person o2) {
        System.out.println("custom comparator called");
        try {
            Object v1 = ReflectUtil.doGetter(field, o1);
            Object v2 = ReflectUtil.doGetter(field, o2);
            String v1s = (String) v1;
            String v2s = (String) v2;
            return v1s.compareTo(v2s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
