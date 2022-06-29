package org.barren.test;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.fill.Property;
import org.barren.generator.engine.EnhanceFreemarkerTemplateEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * mybatis-plus 新代码生成
 *
 * @author cxs
 **/
public class CodeGeneratorDemo {


    /**
     * @param args
     */
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/barren?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useSSL=false";
        String username = "root";
        String password = "123456";

        String modulePath = "/barren-generator";
        String projectPath = System.getProperty("user.dir") + modulePath;
        System.out.println("projectPath = " + projectPath);
        String outputDir = projectPath + "/src/test/java";
        String mapperOutputDir = projectPath + "/src/test/resources/mapper/";

        String[] tables = {"sys_user", "sys_role", "sys_menu","sys_user_role", "sys_role_menu"};
        String packageName = "org.barren.modules";
        String moduleName = "system";

        // 生成前端文件的包名，输出目录
        // String webDir = "api";
        String webPath = projectPath + "/src/test/resources/ui/" + moduleName;
        // 配置xml，other文件的输出路径
        Map<OutputFile, String> pathInfo = new HashMap<>(4);
        pathInfo.put(OutputFile.mapperXml, mapperOutputDir);
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
                            .pathInfo(pathInfo); // 设置mapperXml生成路径，其他文件生成目录
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tables) // 设置需要生成的表名
                            .addTablePrefix("t_", "c_") // 设置过滤表前缀
                            .mapperBuilder().enableBaseColumnList().enableBaseResultMap()
                            .controllerBuilder().enableRestStyle()
                            .entityBuilder()
                            // .addSuperEntityColumns("createUser", "updateUser", "createTime", "updateTime").superClass() // 设置基类
                            // 设置属性填充
                            .addTableFills(new Property("createUser", FieldFill.INSERT),
                                    new Property("updateUser", FieldFill.INSERT_UPDATE),
                                    new Property("createTime", FieldFill.INSERT),
                                    new Property("updateTime", FieldFill.INSERT_UPDATE))
                            .enableLombok().enableTableFieldAnnotation().enableChainModel();
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

        // 配置模板
        TemplateConfig.Builder builder = new TemplateConfig.Builder();
        // TemplateConfig templateConfig = new TemplateConfig();
        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();
        //        把已有的xml生成置空，失效

    }

}
