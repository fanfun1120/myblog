个人博客项目

- 如何启动？
  1. 创建数据库名为：blog的数据库，然后项目目录中有个sql文件夹，进入后找到blog.sql并运行在blog数据库
  2. 修改application.yml中数据库连接信息
  3. 启动redis
  4. 运行FanBlogApplication中main方法。
  5. 运行成功后，浏览器打开地址localhost:7777
  6. 后台管理模块的路由地址是localhost:7777/admin
- 注意点
  1. **如果想清空数据库内容后使用，记得一定要在blog数据库中的t_user表中插入一条数据，填写全。t_user表中avatar字段要填写的内容例如：/images/myavatar.jpg。在myblog\src\main\resources\static\images这个目录中有张图片名字是myavatar.jpg**

