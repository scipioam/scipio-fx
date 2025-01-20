package scipiofx.test;

import com.github.scipioam.scipiofx.framework.AppContext;
import com.github.scipioam.scipiofx.framework.JFXApplication;
import com.github.scipioam.scipiofx.mybatis.MybatisConfig;
import com.github.scipioam.scipiofx.mybatis.MybatisManager;
import com.github.scipioam.scipiofx.mybatis.ext.DBAppInitThread;

import java.util.Map;

/**
 * 2022/6/23
 */
public class TestInitThread extends DBAppInitThread {
    @Override
    protected void doAfterMybatisInit(JFXApplication application, AppContext context, MybatisConfig mybatisConfig, MybatisManager mybatisManager) {
        Map<String, Object> customs = context.getCustomProperties();
        if (customs != null) {
            for (Map.Entry<String, Object> entry : customs.entrySet()) {
                System.out.println("custom property: [" + entry.getKey() + "] = [" + entry.getValue() + "]");
            }
        } else {
            System.out.println("No custom properties");
        }
    }
}
