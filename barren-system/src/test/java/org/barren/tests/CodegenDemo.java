package org.barren.tests;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * mybatis-plus 新代码生成
 *
 * @author cxs
 **/
public class CodegenDemo {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/barren?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useSSL=false";
        String username = "root";
        String password = "123456";
        String modulePath = "/barren-system";

        String projectPath = System.getProperty("user.dir") + modulePath;
        System.out.println("projectPath = " + projectPath);
        String outputDir = projectPath + "/src/main/java";
        String mapperOutputDir = projectPath + "/src/main/resources/mapper/";

        String[] tables = {"sys_user", "sys_role", "sys_menu","sys_user_role", "sys_role_menu"};
        String packageName = "org.barren.modules";
        String moduleName = "system";

        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("cxs") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(outputDir) // 指定输出目录
                            .disableOpenDir();//禁止打开输出目录;
                })
                .packageConfig(builder -> {
                    builder.parent(packageName) // 设置父包名
                            .moduleName(moduleName) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, mapperOutputDir)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tables) // 设置需要生成的表名
                            .addTablePrefix("t_", "c_") // 设置过滤表前缀
                            .mapperBuilder().enableBaseColumnList().enableBaseResultMap()
                            .controllerBuilder().enableRestStyle()
                            .entityBuilder().enableLombok();
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

    }
}
