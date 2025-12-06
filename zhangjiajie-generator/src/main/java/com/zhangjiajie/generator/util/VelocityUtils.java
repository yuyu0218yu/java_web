package com.zhangjiajie.generator.util;

import com.zhangjiajie.generator.entity.GenTable;
import com.zhangjiajie.generator.entity.GenTableColumn;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Velocity模板工具类
 *
 * @author yushuang
 * @since 2025-12-06
 */
public class VelocityUtils {

    /** 项目空间路径 */
    private static final String PROJECT_PATH = "main/java";

    /** mybatis空间路径 */
    private static final String MYBATIS_PATH = "main/resources/mapper";

    /** vue空间路径 */
    private static final String VUE_PATH = "vue";

    /**
     * 初始化Velocity
     */
    public static void initVelocity() {
        Properties p = new Properties();
        try {
            // 加载classpath目录下的vm文件
            p.setProperty("resource.loader.file.class",
                    "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
            Velocity.init(p);
        } catch (Exception e) {
            throw new RuntimeException("初始化Velocity引擎失败", e);
        }
    }

    /**
     * 设置模板变量
     */
    public static VelocityContext prepareContext(GenTable genTable) {
        String moduleName = genTable.getModuleName();
        String businessName = genTable.getBusinessName();
        String packageName = genTable.getPackageName();
        String functionName = genTable.getFunctionName();

        VelocityContext context = new VelocityContext();
        context.put("tplCategory", genTable.getTplCategory());
        context.put("tableName", genTable.getTableName());
        context.put("functionName", functionName != null ? functionName : businessName);
        context.put("ClassName", genTable.getClassName());
        context.put("className", uncapitalize(genTable.getClassName()));
        context.put("moduleName", moduleName);
        context.put("BusinessName", capitalize(businessName));
        context.put("businessName", businessName);
        context.put("basePackage", getPackagePrefix(packageName));
        context.put("packageName", packageName);
        context.put("author", genTable.getFunctionAuthor());
        context.put("datetime", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        context.put("pkColumn", genTable.getPkColumn());
        context.put("columns", genTable.getColumns());
        context.put("table", genTable);

        // 设置需要导入的包
        setImportList(context, genTable.getColumns());

        // 设置列类型判断
        setColumnFlags(context, genTable.getColumns());

        return context;
    }

    /**
     * 设置需要导入的包
     */
    private static void setImportList(VelocityContext context, List<GenTableColumn> columns) {
        Set<String> importList = new TreeSet<>();
        for (GenTableColumn column : columns) {
            if (!column.isSuperColumn()) {
                String javaType = column.getJavaType();
                if ("LocalDateTime".equals(javaType)) {
                    importList.add("java.time.LocalDateTime");
                } else if ("LocalDate".equals(javaType)) {
                    importList.add("java.time.LocalDate");
                } else if ("BigDecimal".equals(javaType)) {
                    importList.add("java.math.BigDecimal");
                }
            }
        }
        context.put("importList", importList);
    }

    /**
     * 设置列标志
     */
    private static void setColumnFlags(VelocityContext context, List<GenTableColumn> columns) {
        List<GenTableColumn> insertColumns = new ArrayList<>();
        List<GenTableColumn> editColumns = new ArrayList<>();
        List<GenTableColumn> listColumns = new ArrayList<>();
        List<GenTableColumn> queryColumns = new ArrayList<>();

        for (GenTableColumn column : columns) {
            if (column.isInsert()) {
                insertColumns.add(column);
            }
            if (column.isEdit()) {
                editColumns.add(column);
            }
            if (column.isList()) {
                listColumns.add(column);
            }
            if (column.isQuery()) {
                queryColumns.add(column);
            }
        }

        context.put("insertColumns", insertColumns);
        context.put("editColumns", editColumns);
        context.put("listColumns", listColumns);
        context.put("queryColumns", queryColumns);
    }

    /**
     * 获取模板列表
     */
    public static List<String> getTemplateList(String tplCategory) {
        List<String> templates = new ArrayList<>();
        templates.add("vm/java/entity.java.vm");
        templates.add("vm/java/mapper.java.vm");
        templates.add("vm/java/service.java.vm");
        templates.add("vm/java/serviceImpl.java.vm");
        templates.add("vm/java/controller.java.vm");
        templates.add("vm/xml/mapper.xml.vm");

        // DTO
        templates.add("vm/java/dto/createRequest.java.vm");
        templates.add("vm/java/dto/updateRequest.java.vm");
        templates.add("vm/java/dto/response.java.vm");

        // Vue前端
        templates.add("vm/vue/index.vue.vm");
        templates.add("vm/vue/api.js.vm");

        return templates;
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, GenTable genTable) {
        String className = genTable.getClassName();
        String businessName = genTable.getBusinessName();
        String packageName = genTable.getPackageName();
        String moduleName = genTable.getModuleName();

        String javaPath = PROJECT_PATH + "/" + packageName.replace(".", "/");
        String vuePath = VUE_PATH + "/views/" + moduleName + "/" + businessName;
        String vueApiPath = VUE_PATH + "/api/" + moduleName;

        if (template.contains("entity.java.vm")) {
            return javaPath + "/entity/" + className + ".java";
        }
        if (template.contains("mapper.java.vm")) {
            return javaPath + "/mapper/" + className + "Mapper.java";
        }
        if (template.contains("service.java.vm")) {
            return javaPath + "/service/" + className + "Service.java";
        }
        if (template.contains("serviceImpl.java.vm")) {
            return javaPath + "/service/impl/" + className + "ServiceImpl.java";
        }
        if (template.contains("controller.java.vm")) {
            return javaPath + "/controller/" + className + "Controller.java";
        }
        if (template.contains("mapper.xml.vm")) {
            return MYBATIS_PATH + "/" + className + "Mapper.xml";
        }
        if (template.contains("createRequest.java.vm")) {
            return javaPath + "/dto/Create" + className + "Request.java";
        }
        if (template.contains("updateRequest.java.vm")) {
            return javaPath + "/dto/Update" + className + "Request.java";
        }
        if (template.contains("response.java.vm")) {
            return javaPath + "/dto/" + className + "Response.java";
        }
        if (template.contains("index.vue.vm")) {
            return vuePath + "/index.vue";
        }
        if (template.contains("api.js.vm")) {
            return vueApiPath + "/" + businessName + ".js";
        }

        return null;
    }

    /**
     * 获取包前缀
     */
    private static String getPackagePrefix(String packageName) {
        int lastIndex = packageName.lastIndexOf(".");
        return lastIndex > 0 ? packageName.substring(0, lastIndex) : packageName;
    }

    /**
     * 首字母大写
     */
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 首字母小写
     */
    private static String uncapitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}
