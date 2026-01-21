package org.barren.test;

import org.barren.generator.CodeGenerator;

/**
 * @author cxs
 */
public class CodeGeneratorTest {

    public static void main(String[] args) {
        // 剧本杀项目代码生成
        String url= "jdbc:mysql://47.100.177.60:3306/bss?serverTimezone=Asia/Shanghai&useLegacyDatetimeCode=false&useUnicode=true&characterEncoding=utf8&useSSL=false";
        String username = "readonly";
        String password = "Readonly_123";
        String[] tables = {"bss_content_examine"};

        String author = "cxs";
        String packageName = "com.bss.app";


        CodeGenerator codeGenerator = new CodeGenerator();
        codeGenerator.setAuthor(author);
        codeGenerator.setUrl(url);
        codeGenerator.setUsername(username);
        codeGenerator.setPassword(password);
        codeGenerator.setTable(tables);
        codeGenerator.setPackageName(packageName);
        codeGenerator.setModuleName("");

        codeGenerator.execute();
    }

}
