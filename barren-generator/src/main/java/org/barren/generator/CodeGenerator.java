package org.barren.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.barren.generator.engine.EnhanceFreemarkerTemplateEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * 代码生成器，使用 mybatis-plus
 *
 * @author cxs
 **/
@Slf4j
@Data
public class CodeGenerator {

    private String url = "jdbc:mysql://localhost:3306/barren?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useSSL=false";
    private String username = "root";
    private String password = "123456";
    private String[] table;
    private String[] tablePrefix = new String[]{"t_", "c_"};

    private String author = "barren";
    private String outputDir;
    private String packageName = "org.barren.modules";
    private String moduleName = "demo";
    private String mapperPath;
    private String webPath;

    public void execute() {
        if (StringUtils.isBlank(outputDir)) {
            outputDir = System.getProperty("user.dir") + "/barren-generator/example";
        }
        if (StringUtils.isBlank(mapperPath)) {
            mapperPath = outputDir + "/mapper";
        }
        if (StringUtils.isBlank(webPath)) {
            webPath = outputDir + "/ui/" + moduleName;
        }

        // 配置mapper.xml ,other文件的输出路径
        Map<OutputFile, String> pathInfo = new HashMap<>(4);
        pathInfo.put(OutputFile.mapperXml, mapperPath);
        pathInfo.put(OutputFile.other, webPath);

        // 配置前端文件：api.js, index.vue
        Map<String, String> customFile = new HashMap<>(4);
        customFile.put("api.js", "template/generator/api.js.ftl");
        customFile.put("index.vue", "template/generator/index.vue.ftl");

        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("cxs") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(outputDir) // 指定输出目录
                            .disableOpenDir();// 禁止打开输出目录;
                })
                .packageConfig(builder -> {
                    builder.parent(packageName) // 设置父包名
                            .moduleName(moduleName) // 设置父包模块名
                            //.other(webDir)
                            .pathInfo(pathInfo); // 其他文件生成目录，设置mapperXml生成路径，
                })
                .strategyConfig(builder -> {
                    builder.addInclude(table) // 设置需要生成的表名
                            .addTablePrefix(tablePrefix) // 设置过滤表前缀
                            .mapperBuilder().enableBaseColumnList().enableBaseResultMap()
                            .controllerBuilder().enableRestStyle()
                            .entityBuilder().enableLombok().enableTableFieldAnnotation().enableChainModel();
                })
                .templateEngine(new EnhanceFreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateConfig(builder ->
                        builder.controller("template/generator/controller.java")) // 自定义模板
                // 自定义其他配置模板文件
                .injectionConfig(builder -> {
                    builder.
                            beforeOutputFile((tableInfo, objectMap) -> { // 输出文件之前消费者
                                System.out.println("tableInfo: " + tableInfo.getEntityName() + " objectMap: " + objectMap.size());
                            })
                            //.customMap(Collections.singletonMap("test", "baomidou")) // 自定义配置 Map 对象
                            .customFile(customFile); // 自定义配置模板文件
                })
                .execute();
    }

    public static void main(String[] args) {
        String modulePath = "/barren-generator";
        String projectPath = System.getProperty("user.dir") + modulePath;
        CodeGenerator codeGenerator = new CodeGenerator();
        codeGenerator.setTable(new String[]{"sys_user"});
        codeGenerator.setModuleName("system");
        // codeGenerator.setOutputDir(projectPath + "/src/test/java");
        // codeGenerator.setMapperPath(projectPath + "/src/test/resources/mapper/");
        // codeGenerator.setWebPath(projectPath + "/src/test/resources/ui/" + "system");

        codeGenerator.execute();
    }
}
