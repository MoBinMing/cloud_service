package com.mobinming.cloud_service.util;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MybatisPlusGenerator {

    //static final String TABLE_NAME = "t_order";
    static final String MODULE_NAME = "cloud_service";

    static final String PACKAGE_PATH = "com.mobinming";
    static final String AUTHOR = "mbm";

    //修改TABLE_NAME、MODULE_NAME、AUTHOR后运行main方法
    //注意，会覆盖MODULE_NAME下的所有代码
    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator autoGenerator = new AutoGenerator();

        //全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        //项目目录
        String projectPath = System.getProperty("user.dir");
        //生成文件的输出目录
        globalConfig.setOutputDir(projectPath + "/src/main/java");
        //生成后打开文件目录
        globalConfig.setOpen(false);
        //覆盖文件
        globalConfig.setFileOverride(true);
        // 开启 activeRecord 模式
        globalConfig.setActiveRecord(true);
        //生成BaseResultMap
        globalConfig.setBaseResultMap(true);
        //生成BaseColumnList
        globalConfig.setBaseColumnList(true);
        //是否在xml中添加二级缓存配置
        globalConfig.setEnableCache(false);
        //开启 Kotlin 模式
        globalConfig.setKotlin(false);
        //加上swagger注解
        globalConfig.setSwagger2(true);
        //自定义service接口名
        globalConfig.setServiceName("%sService");
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        //globalConfig.setEntityName("%sEntity");
        //globalConfig.setMapperName("%sDao");
        //globalConfig.setXmlName("%sDao");
        globalConfig.setServiceImplName("%sServiceImpl");
        //globalConfig.setControllerName("%sAction");
        //使用java.util.Date
        globalConfig.setDateType(DateType.ONLY_DATE);
        globalConfig.setAuthor(AUTHOR);
        autoGenerator.setGlobalConfig(globalConfig);

        //数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/my_cloud?useUnicode=true&useSSL=false&characterEncoding=utf8");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("Mo100200");
        autoGenerator.setDataSource(dsc);

        //包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(PACKAGE_PATH);
        pc.setModuleName(MODULE_NAME);
        //在service包下生成Impl
        pc.setServiceImpl("service.impl");
        //在mapper生成xml
        //pc.setXml("mapper/xml");
        autoGenerator.setPackageInfo(pc);

        //策略配置
        StrategyConfig strategy = new StrategyConfig();
        //数据库表映射到实体的命名策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        //数据库表字段映射到实体的命名策略, 未指定按照 naming 执行
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //是否为lombok模型
        strategy.setEntityLombokModel(true);
        //生成 @RestController 控制器
        strategy.setRestControllerStyle(true);
        //表前缀
        strategy.setTablePrefix("t");
        //需要包含的表名，允许正则表达式（与exclude二选一配置）
        String tables=scanner("\n生成全部表：1\n生成指定表：多个表名英文逗号分割");
        if (!tables.equals("1")){
            strategy.setInclude(tables.split(","));
        }
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(MODULE_NAME + "_");
        autoGenerator.setStrategy(strategy);
        autoGenerator.execute();

    }

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (!"".equals(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }
}
