package com.github.scipioam.scipiofx.framework.config;

import com.github.scipioam.scipiofx.framework.JFXApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Alan Scipio
 * created on 2025-04-28
 */
public class AppConfigWriter {

    private static final Logger log = LoggerFactory.getLogger(AppConfigWriter.class);

    /**
     * 将修改过后的配置项写回外部配置文件
     *
     * @param configFile 外部配置文件（jar包内置的配置文件无法写回）
     * @param properties 修改过后的配置项
     * @return true：写回成功，  false：写回失败（不是外部配置文件
     * @throws IOException 意外的异常
     */
    public static boolean rewrite(File configFile, ConfigRootProperties properties) throws IOException {
        if (properties == null) {
            throw new IllegalArgumentException("properties cannot be null");
        }
        if (configFile == null) {
            log.warn("Config file can not be overwritten.");
            return false;
        }

        // 配置输出选项
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // 使用块样式
        options.setIndent(2); // 缩进2个空格
        options.setPrettyFlow(true); // 美化输出

        // 4.创建新的Yaml实例并写入文件
        Yaml outputYaml = new Yaml(options);
        try (FileWriter writer = new FileWriter(configFile)) {
            outputYaml.dump(properties, writer);
            return true;
        }
    }

    public static boolean rewrite(ConfigRootProperties properties) throws IOException {
        File externalConfigFile = JFXApplication.getContext().getExternalConfigFile();
        return rewrite(externalConfigFile, properties);
    }

    public static boolean rewrite() throws IOException {
        File externalConfigFile = JFXApplication.getContext().getExternalConfigFile();
        ConfigRootProperties properties = JFXApplication.getContext().getConfigRootProperties();
        return rewrite(externalConfigFile, properties);
    }

}
