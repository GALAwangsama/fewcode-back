# FewCode(FC) Admin 中后台管理框架

<a href="https://github.com/continew-org/continew-admin/blob/dev/LICENSE" target="_blank">
<img src="https://img.shields.io/badge/License-Apache--2.0-blue.svg" alt="License" />
</a>
<a href="https://github.com/continew-org/continew-admin" target="_blank">
<img src="https://img.shields.io/badge/RELEASE-v3.4.1-%23ff3f59.svg" alt="Release" />
</a>
<a href="https://app.codacy.com/gh/continew-org/continew-admin/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade" target="_blank">
<img src="https://app.codacy.com/project/badge/Grade/19e3e2395d554efe902c3822e65db30e" alt="Codacy Badge" />
</a>
<a href="https://sonarcloud.io/summary/new_code?id=Charles7c_continew-admin" target="_blank">
<img src="https://sonarcloud.io/api/project_badges/measure?project=Charles7c_continew-admin&metric=alert_status" alt="Sonar Status" />
</a>
<a href="https://github.com/continew-org/continew-starter" target="_blank">
<img src="https://img.shields.io/badge/ContiNew Starter-2.7.5-%236CB52D.svg" alt="ContiNew Starter" />
</a>
<a href="https://spring.io/projects/spring-boot" target="_blank">
<img src="https://img.shields.io/badge/Spring Boot-3.2.10-%236CB52D.svg?logo=Spring-Boot" alt="Spring Boot" />
</a>

## 简介
FewCode (FC) 开发模板 开箱即用的前后端分离中后台管理系统，持续提供舒适的前、后端开发体验。

当前采用的技术栈：Spring Boot3（Java17）、Vue3 & Arco Design & TS & Vite、Sa-Token、MyBatis Plus、Redisson、JetCache、JustAuth、Crane4j、EasyExcel、Liquibase、Hutool 等。

## 项目源码

|         | 后端                                                                                                        | 前端                                                                                                          |
|:--------|:----------------------------------------------------------------------------------------------------------|:------------------------------------------------------------------------------------------------------------|
| 阿里云效    | [fewcode/fewcode-backend](https://codeup.aliyun.com/669777c19bc4550a6d341236/fewcode/fewcode-backend.git) | [fewcode/fewcode-frontend](https://codeup.aliyun.com/669777c19bc4550a6d341236/fewcode/fewcode-frontend.git) |
| GitHub  | 待定                                                                                                        | 待定                                                                                                          |


##  系统功能

- 仪表盘：提供工作台、分析页，工作台提供功能快捷导航入口、最新公告、动态；分析页提供全面数据可视化能力
- 个人中心：支持基础信息修改、密码修改、邮箱绑定、手机号绑定（并提供行为验证码、短信限流等安全处理）、第三方账号绑定/解绑、头像裁剪上传
- 消息中心：提供站内信消息统一查看、标记已读、全部已读、删除等功能（目前仅支持系统通知消息）
- 用户管理：管理系统用户，包含新增、修改、删除、导入、导出、重置密码、分配角色等功能
  
- 角色管理：管理系统用户的功能权限及数据权限，包含新增、修改、删除、分配角色等功能
  
- 菜单管理：管理系统菜单及按钮权限，支持多级菜单，动态路由，包含新增、修改、删除等功能
  
- 部门管理：管理系统组织架构，包含新增、修改、删除、导出等功能，以树形列表进行展示
  
- 字典管理：管理系统公用数据字典，例如：消息类型。支持字典标签背景色和排序等配置
  
- 通知公告：管理系统公告，支持设置公告的生效时间、终止时间、通知范围（所有人、指定用户）
  
- 文件管理：管理系统文件，支持上传、下载、预览（目前支持图片、音视频、PDF、Word、Excel、PPT）、重命名、切换视图（列表、网格）等功能
  
- 存储管理：管理文件存储配置，支持本地存储、兼容 S3 协议存储
  
- 系统配置：
  - 基础配置：提供修改系统标题、Logo、favicon、版权信息等基础配置功能，以方便用户系统与其自身品牌形象保持一致
  - 邮件配置：提供系统发件箱配置，也支持通过配置文件指定
  - 安全配置：提供密码策略修改，支持丰富的密码策略设定，包括但不限于 `密码有效期`、`密码重复次数`、`密码错误锁定账号次数、时间` 等
  
- 在线用户：管理当前登录用户，可一键踢除下线
  
- 日志管理：管理系统登录日志、操作日志，支持查看日志详情，包含请求头、响应头等报文信息
  
- 任务管理：管理系统定时任务，包含新增、修改、删除、执行功能，支持 Cron（可配置式生成 Cron 表达式） 和固定频率
  
- 任务日志：管理定时任务执行日志，包含停止、重试指定批次，查询集群各节点的详细输出日志等功能
  
- 应用管理：管理第三方系统应用 AK、SK，包含新增、修改、删除、查看密钥、重置密钥等功能，支持设置密钥有效期
  
- 代码生成：提供根据数据库表自动生成相应的前后端 CRUD 代码的功能，支持同步最新表结构及代码生成预览


## 核心技术栈

| 名称                                                         | 版本           | 简介                                                         |
| :----------------------------------------------------------- |:-------------| :----------------------------------------------------------- |
| <a href="https://cn.vuejs.org/" target="_blank">Vue</a>      | 3.4.21       | 渐进式 JavaScript 框架，易学易用，性能出色，适用场景丰富的 Web 前端框架。 |
| <a href="https://arco.design/vue/docs/start" target="_blank">Arco Design</a> | 2.56.0       | 字节跳动推出的前端 UI 框架，年轻化的色彩和组件设计。         |
| <a href="https://www.typescriptlang.org/zh/" target="_blank">TypeScript</a> | 5.0.4        | TypeScript 是微软开发的一个开源的编程语言，通过在 JavaScript 的基础上添加静态类型定义构建而成。 |
| <a href="https://cn.vitejs.dev/" target="_blank">Vite</a>    | 5.1.5        | 下一代的前端工具链，为开发提供极速响应。                     |
| [ContiNew Starter](https://github.com/continew-org/continew-starter) | 2.7.5        | ContiNew Starter 包含了一系列经过企业实践优化的依赖包（如 MyBatis-Plus、SaToken），可轻松集成到应用中，为开发人员减少手动引入依赖及配置的麻烦，为 Spring Boot Web 项目的灵活快速构建提供支持。 |
| <a href="https://spring.io/projects/spring-boot" target="_blank">Spring Boot</a> | 3.2.10       | 简化 Spring 应用的初始搭建和开发过程，基于“约定优于配置”的理念，使开发人员不再需要定义样板化的配置。（Spring Boot 3.0 开始，要求 Java 17 作为最低版本） |
| <a href="https://undertow.io/" target="_blank">Undertow</a>  | 2.3.13.Final | 采用 Java 开发的灵活的高性能 Web 服务器，提供包括阻塞和基于 NIO 的非堵塞机制。 |
| <a href="https://sa-token.dev33.cn/" target="_blank">Sa-Token + JWT</a> | 1.39.0       | 轻量级 Java 权限认证框架，让鉴权变得简单、优雅。             |
| <a href="https://baomidou.com/" target="_blank">MyBatis Plus</a> | 3.5.8        | MyBatis 的增强工具，在 MyBatis 的基础上只做增强不做改变，简化开发、提高效率。 |
| <a href="https://www.kancloud.cn/tracy5546/dynamic-datasource/2264611" target="_blank">dynamic-datasource-spring-boot-starter</a> | 4.3.1        | 基于 Spring Boot 的快速集成多数据源的启动器。                |
| Hikari                                                       | 5.0.1        | JDBC 连接池，号称 “史上最快连接池”，SpringBoot 在 2.0 之后，采用的默认数据库连接池就是 Hikari。 |
| <a href="https://dev.mysql.com/downloads/mysql/" target="_blank">MySQL</a> | 8.0.33       | 体积小、速度快、总体拥有成本低，是最流行的关系型数据库管理系统之一。 |
| <a href="https://dev.mysql.com/doc/connector-j/8.0/en/" target="_blank">mysql-connector-j</a> | 8.3.0        | MySQL Java 驱动。                                            |
| <a href="https://github.com/p6spy/p6spy" target="_blank">P6Spy</a> | 3.9.1        | SQL 性能分析组件。                                           |
| <a href="https://github.com/liquibase/liquibase" target="_blank">Liquibase</a> | 4.24.0       | 用于管理数据库版本，跟踪、管理和应用数据库变化。             |
| [JetCache](https://github.com/alibaba/jetcache/blob/master/docs/CN/Readme.md) | 2.7.6        | 一个基于 Java 的缓存系统封装，提供统一的 API 和注解来简化缓存的使用。提供了比 SpringCache 更加强大的注解，可以原生的支持 TTL、两级缓存、分布式自动刷新，还提供了 Cache 接口用于手工缓存操作。 |
| <a href="https://github.com/redisson/redisson/wiki/Redisson%E9%A1%B9%E7%9B%AE%E4%BB%8B%E7%BB%8D" target="_blank">Redisson</a> | 3.36.0       | 不仅仅是一个 Redis Java 客户端，Redisson 充分的利用了 Redis 键值数据库提供的一系列优势，为使用者提供了一系列具有分布式特性的常用工具：分布式锁、限流器等。 |
| <a href="https://redis.io/" target="_blank">Redis</a>        | 7.2.3        | 高性能的 key-value 数据库。                                  |
| [X File Storage](https://x-file-storage.xuyanwu.cn/#/)       | 2.2.1        | 一行代码将文件存储到本地、FTP、SFTP、WebDAV、阿里云 OSS、华为云 OBS...等其它兼容 S3 协议的存储平台。 |
| <a href="https://sms4j.com/" target="_blank">SMS4J</a>       | 3.3.3        | 短信聚合框架，轻松集成多家短信服务，解决接入多个短信 SDK 的繁琐流程。 |
| <a href="https://justauth.cn/" target="_blank">Just Auth</a> | 1.16.6       | 开箱即用的整合第三方登录的开源组件，脱离繁琐的第三方登录 SDK，让登录变得 So easy！ |
| <a href="https://easyexcel.opensource.alibaba.com/" target="_blank">Easy Excel</a> | 4.0.1        | 一个基于 Java 的、快速、简洁、解决大文件内存溢出的 Excel 处理工具。 |
| [AJ-Captcha](https://ajcaptcha.beliefteam.cn/captcha-doc/)   | 1.3.0        | Java 行为验证码，包含滑动拼图、文字点选两种方式，UI支持弹出和嵌入两种方式。 |
| Easy Captcha                                                 | 1.6.2        | Java 图形验证码，支持 gif、中文、算术等类型，可用于 Java Web、JavaSE 等项目。 |
| [Crane4j](https://createsequence.gitee.io/crane4j-doc/#/)    | 2.9.0        | 一个基于注解的，用于完成一切 “根据 A 的 key 值拿到 B，再把 B 的属性映射到 A” 这类需求的字段填充框架。 |
| [CosID](https://cosid.ahoo.me/guide/getting-started.html)    | 2.9.8        | 旨在提供通用、灵活、高性能的分布式 ID 生成器。               |
| [Graceful Response](https://doc.feiniaojin.com/graceful-response/home.html) | 5.0.0-boot3  | 一个Spring Boot技术栈下的优雅响应处理组件，可以帮助开发者完成响应数据封装、异常处理、错误码填充等过程，提高开发效率，提高代码质量。 |
| <a href="https://doc.xiaominfo.com/" target="_blank">Knife4j</a> | 4.5.0        | 前身是 swagger-bootstrap-ui，集 Swagger2 和 OpenAPI3 为一体的增强解决方案。 |
| <a href="https://www.hutool.cn/" target="_blank">Hutool</a>  | 5.8.32       | 小而全的 Java 工具类库，通过静态方法封装，降低相关 API 的学习成本，提高工作效率，使 Java 拥有函数式语言般的优雅，让 Java 语言也可以“甜甜的”。 |
| <a href="https://projectlombok.org/" target="_blank">Lombok</a> | 1.18.32      | 在 Java 开发过程中用注解的方式，简化了 JavaBean 的编写，避免了冗余和样板式代码，让编写的类更加简洁。 |


## 快速开始

```bash
# 1.在 IDE（IntelliJ IDEA/Eclipse）中打开本项目

# 2.修改配置文件中的数据源配置信息、Redis 配置信息、邮件配置信息等
# [2.也可以在 IntelliJ IDEA 中直接配置程序启动环境变量（DB_HOST、DB_PORT、DB_USER、DB_PWD、DB_NAME；REDIS_HOST、REDIS_PORT、REDIS_PWD、REDIS_DB）]

# 3.启动程序
# 3.1 启动成功：访问 http://localhost:8000/，页面输出：Xxx started successfully.
# 3.2 接口文档：http://localhost:8000/doc.html

# 4.部署
# 4.1 Docker 部署
#   4.1.1 服务器安装好 docker 及 docker-compose（参考：https://blog.charles7c.top/categories/fragments/2022/10/31/CentOS%E5%AE%89%E8%A3%85Docker）
#   4.1.2 执行 mvn package 进行项目打包，将 target/app 目录下的所有内容放到 /docker/continew-admin 目录下
#   4.1.3 将 docker 目录上传到服务器 / 目录下，并授权（chmod -R 777 /docker）
#   4.1.4 修改 docker-compose.yml 中的 MySQL 配置、Redis 配置、continew-admin-server 配置、Nginx 配置
#   4.1.5 执行 docker-compose up -d 创建并后台运行所有容器
# 4.2 其他方式部署
```

## 项目结构

> [!TIP]
> 后端采用按功能拆分模块的开发方式，下方项目目录结构是按照模块的层次顺序进行介绍的。

```
fewcode-admin
├─ fewcode-webapi（API 及打包部署模块）
│  ├─ src
│  │  ├─ main
│  │  │  ├─ java/top/continew/admin
│  │  │  │  ├─ config （配置）
│  │  │  │  ├─ controller
│  │  │  │  │  ├─ auth（系统认证相关 API）
│  │  │  │  │  ├─ common（通用相关 API）
│  │  │  │  │  ├─ monitor（系统监控相关 API）
│  │  │  │  │  ├─ system（系统管理相关 API）
│  │  │  │  │  └─ tool（系统工具相关 API）
│  │  │  │  └─ ContiNewAdminApplication.java（ContiNew Admin 启动程序）
│  │  │  └─ resources
│  │  │     ├─ config（核心配置目录）
│  │  │     │  ├─ application-dev.yml（开发环境配置文件）
│  │  │     │  ├─ application-prod.yml（生产环境配置文件）
│  │  │     │  └─ application.yml（通用配置文件）
│  │  │     ├─ db/changelog（Liquibase 数据脚本配置目录）
│  │  │     │  ├─ mysql（MySQL 数据库初始 SQL 脚本目录）
│  │  │     │  ├─ postgresql（PostgreSQL 数据库初始 SQL 脚本目录）
│  │  │     │  └─ db.changelog-master.yaml（Liquibase 变更记录文件）
│  │  │     ├─ templates（模板配置目录，例如：邮件模板）
│  │  │     ├─ banner.txt（Banner 配置文件）
│  │  │     └─ logback-spring.xml（日志配置文件）
│  │  └─ test（测试相关代码目录）
│  └─ pom.xml（包含打包相关配置）
├─ fewcode-module-system（系统管理模块，存放系统管理相关业务功能，例如：部门管理、角色管理、用户管理等）
│  ├─ src
│  │  ├─ main
│  │  │  ├─ java/top/continew/admin
│  │  │  │  ├─ auth（系统认证相关业务）
│  │  │  │  │  ├─ model（系统认证相关模型）
│  │  │  │  │  │  ├─ query（系统认证相关查询条件）
│  │  │  │  │  │  ├─ req（系统认证相关请求对象（Request））
│  │  │  │  │  │  └─ resp（系统认证相关响应对象（Response））
│  │  │  │  │  └─ service（系统认证相关业务接口及实现类）
│  │  │  │  └─ system（系统管理相关业务）
│  │  │  │     ├─ config（系统管理相关配置）
│  │  │  │     ├─ enums（系统管理相关枚举）
│  │  │  │     ├─ mapper（系统管理相关 Mapper）
│  │  │  │     ├─ model（系统管理相关模型）
│  │  │  │     │  ├─ entity（系统管理相关实体对象）
│  │  │  │     │  ├─ query（系统管理相关查询条件）
│  │  │  │     │  ├─ req（系统管理相关请求对象（Request））
│  │  │  │     │  └─ resp（系统管理相关响应对象（Response））
│  │  │  │     ├─ service（系统管理相关业务接口及实现类）
│  │  │  │     └─ util（系统管理相关工具类）
│  │  │  └─ resources
│  │  │     └─ mapper（系统管理相关 Mapper XML 文件目录）
│  │  └─ test（测试相关代码目录）
│  └─ pom.xml
├─ fewcode-plugin（插件模块，存放代码生成、任务调度等扩展模块，后续会进行插件化改造）
│  ├─ fewcode-plugin-open（能力开放插件模块）
│  │  ├─ src
│  │  │  ├─ main/java/top/continew/admin/open
│  │  │  │  ├─ mapper（代码生成器相关 Mapper）
│  │  │  │  ├─ model（能力开放相关模型）
│  │  │  │  │  ├─ entity（能力开放相关实体对象）
│  │  │  │  │  ├─ query（能力开放相关查询条件）
│  │  │  │  │  ├─ req（能力开放相关请求对象（Request））
│  │  │  │  │  └─ resp（能力开放相关响应对象（Response））
│  │  │  │  └─ service（能力开放相关业务接口及实现类）
│  │  │  └─ test（测试相关代码目录）
│  │  └─ pom.xml
│  ├─ fewcode-plugin-generator（代码生成器插件模块）
│  │  ├─ src
│  │  │  ├─ main
│  │  │  │  ├─ java/top/continew/admin/generator
│  │  │  │  │  ├─ config（代码生成器相关配置）
│  │  │  │  │  ├─ enums（代码生成器相关枚举）
│  │  │  │  │  ├─ mapper（代码生成器相关 Mapper）
│  │  │  │  │  ├─ model（代码生成器相关模型）
│  │  │  │  │  │  ├─ entity（代码生成器相关实体对象）
│  │  │  │  │  │  ├─ query（代码生成器相关查询条件）
│  │  │  │  │  │  ├─ req（代码生成器相关请求对象（Request））
│  │  │  │  │  │  └─ resp（代码生成器相关响应对象（Response））
│  │  │  │  │  └─ service（代码生成器相关业务接口及实现类）
│  │  │  │  └─ resources
│  │  │  │     ├─ templates/generator（代码生成相关模板目录）
│  │  │  │     ├─ application.yml（代码生成配置文件）
│  │  │  │     └─ generator.properties（代码生成类型映射配置文件）
│  │  │  └─ test（测试相关代码目录）
│  │  └─ pom.xml
│  └─ pom.xml
├─ fewcode-common（公共模块，存放公共工具类，公共配置等）
│  ├─ src
│  │  ├─ main/java/top/continew/admin/common
│  │  │  ├─ config（公共配置）
│  │  │  ├─ constant（公共常量）
│  │  │  ├─ enums（公共枚举）
│  │  │  ├─ model（公共模型）
│  │  │  │  ├─ dto（公共 DTO（Data Transfer Object））
│  │  │  │  ├─ req（公共请求对象（Request））
│  │  │  │  └─ resp（公共响应对象（Response））
│  │  │  └─ util（公共工具类）
│  │  └─ test（测试相关代码目录）
│  └─ pom.xml
├─ .github（GitHub 相关配置目录，实际开发时直接删除）
├─ .idea
│  └─ icon.png（IDEA 项目图标，实际开发时直接删除）
├─ .style（代码格式、License文件头相关配置目录，实际开发时根据需要取舍，删除时注意删除 spotless 插件配置）
├─ .gitignore（Git 忽略文件相关配置文件）
├─ docker（项目部署相关配置目录，实际开发时可备份后直接删除）
├─ LICENSE（开源协议文件）
├─ CHANGELOG.md（更新日志文件，实际开发时直接删除）
├─ README.md（项目 README 文件，实际开发时替换为真实内容）
├─ lombok.config（Lombok 全局配置文件）
└─ pom.xml（包含版本锁定及全局插件相关配置）
```


### 鸣谢

感谢参与贡献的每一位小伙伴🥰

### 特别鸣谢

- 感谢 <a href="https://www.jetbrains.com/" target="_blank">JetBrains</a> 提供的 <a href="https://jb.gg/OpenSourceSupport" target="_blank">非商业开源软件开发授权</a> 
- 感谢 <a href="https://github.com/baomidou/mybatis-plus" target="_blank">MyBatis Plus</a>、<a href="https://github.com/dromara/sa-token" target="_blank">Sa-Token</a> 、<a href="https://github.com/alibaba/jetcache" target="_blank">JetCache</a>、<a href="https://github.com/opengoofy/crane4j" target="_blank">Crane4j</a>、<a href="https://github.com/xiaoymin/knife4j" target="_blank">Knife4j</a>、<a href="https://github.com/dromara/hutool" target="_blank">Hutool</a> 等开源组件作者为国内开源世界作出的贡献
- 感谢项目使用或未使用到的每一款开源组件，致敬各位开源先驱 :fire:

## License

- 遵循 <a href="https://github.com/continew-org/continew-admin/blob/dev/LICENSE" target="_blank">Apache-2.0</a> 开源许可协议
- Copyright © 2025-present IPBD

