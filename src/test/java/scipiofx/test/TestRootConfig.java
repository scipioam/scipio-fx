package scipiofx.test;

import com.github.scipioam.scipiofx.mybatis.ext.DBConfigProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Alan Scipio
 * created on 2023/1/13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TestRootConfig extends DBConfigProperties {

    private String testA;

}
