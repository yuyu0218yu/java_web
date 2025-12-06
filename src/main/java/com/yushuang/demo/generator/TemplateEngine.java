package com.yushuang.demo.generator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Freemarker 模板引擎
 * 统一处理模板渲染
 *
 * @author yushuang
 */
@Slf4j
@Component
public class TemplateEngine {

    private final Configuration configuration;

    public TemplateEngine() {
        configuration = new Configuration(Configuration.VERSION_2_3_32);
        configuration.setClassLoaderForTemplateLoading(
            getClass().getClassLoader(), 
            "templates/generator"
        );
        configuration.setDefaultEncoding("UTF-8");
        
        // 模板缓存配置
        configuration.setCacheStorage(new freemarker.cache.MruCacheStorage(20, 250));
        configuration.setTemplateUpdateDelayMilliseconds(3600000); // 1小时检查一次更新
    }

    /**
     * 渲染模板为字符串
     *
     * @param templateName 模板名称（如 entity.ftl）
     * @param dataModel    数据模型
     * @return 渲染后的字符串
     */
    public String render(String templateName, Map<String, Object> dataModel) {
        try {
            Template template = configuration.getTemplate(templateName);
            StringWriter writer = new StringWriter();
            template.process(dataModel, writer);
            return writer.toString();
        } catch (IOException | TemplateException e) {
            throw GeneratorException.templateRenderFailed(templateName, e);
        }
    }

    /**
     * 渲染模板并写入文件
     *
     * @param templateName 模板名称
     * @param dataModel    数据模型
     * @param outputFile   输出文件路径
     */
    public void renderToFile(String templateName, Map<String, Object> dataModel, String outputFile) {
        renderToFile(templateName, dataModel, outputFile, true);
    }

    /**
     * 渲染模板并写入文件（带覆盖控制）
     *
     * @param templateName 模板名称
     * @param dataModel    数据模型
     * @param outputFile   输出文件路径
     * @param overwrite    是否覆盖已存在文件
     */
    public void renderToFile(String templateName, Map<String, Object> dataModel, 
                             String outputFile, boolean overwrite) {
        try {
            File file = new File(outputFile);
            
            // 检查文件是否存在
            if (file.exists() && !overwrite) {
                log.warn("文件已存在，跳过生成: {}", outputFile);
                return;
            }
            
            // 确保目录存在
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }

            // 渲染并写入
            Template template = configuration.getTemplate(templateName);
            try (Writer writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
                template.process(dataModel, writer);
            }
            
            log.info("生成文件: {}", outputFile);
        } catch (IOException | TemplateException e) {
            throw GeneratorException.writeFileFailed(outputFile, e);
        }
    }

    /**
     * 检查文件是否存在
     */
    public boolean fileExists(String filePath) {
        return new File(filePath).exists();
    }

    /**
     * 检查模板是否存在
     */
    public boolean templateExists(String templateName) {
        try {
            configuration.getTemplate(templateName);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
