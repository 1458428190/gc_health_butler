# health_butler
  毕业设计-健康管家小程序(后端)

### 技术
  - MySQL
  - SpringBoot
  - Mybatis,MybatisPlus
  - Redis
  - HttpClient5
  - @Schedule
  - Minio
  - Jsoup
  - Druid
  - Transactional
  - Docker 
   
### 知识总结
  - 使用Docker搭建Minio服务以及配置
    - https://docs.minio.io/cn/minio-docker-quickstart-guide.html
  - SpringBoot配置Https访问
    - https://blog.csdn.net/qq_24894159/article/details/79936212

### 数据来源
  - 食物
      - 160趣健康
  - 健康资讯
      - 香山健康
  - 步数
      - 微信

### 接口定义
  - 查询食物分类
      - /food/category
  - 查询食物列表
      - /food/list?keyword=&cat_name=
  - 查询食物详请
      - /food/detail?food_id=
  - 查询文章列表(指定分类)
      - /article/list?cat_name=
  - 查询打卡和提醒记录
      - /user/record/clock
  - 查询任务列表(完成情况)
      - /task/list
  - 查询兑换记录
      - /user/record/conversion
  - 查询可兑换商品
      - /goods/list
  - 查询主页信息(封面, 总步数,总健康币,排名,历史步数, 个人信息)
      - /user/info?uid=
  - 查询健康币明细
      - /user/coin/detail
  - 查询排名
      - /user/rank
  - 查询社区小圈
      - /community
  - 发表
      - /community/share
  - 点赞
      - /community/praise?cid=
  - 打赏
      - /community/reward?cid=&coin=
  - 设置提醒
      - /user/record/clock_remind
    
### 问题
  - 对于token的设计、使用、以及存储
  - url传 '+' 号会变成空格
  - MySQL to是关键词，不能做为列名
  - Minio
      - 使用Minio时注意桶的命名规则（如，不能使用下划线）
      - 上传文件时，需注意Content-type
      - 共享桶文件
          - mc policy download play/laichengfeng-health-butler
  - JAVA访问Https时，证书验证问题
      - 解决方法 https://blog.csdn.net/faye0412/article/details/6883879/
  - Arrays.asList(xx[])后进行add操作，有问题
      - 这是由于Arrays.asLisvt() 返回java.util.Arrays$ArrayList， 而不是ArrayList。Arrays$ArrayList和ArrayList都是继承AbstractList，remove，add等method在AbstractList中是默认throw UnsupportedOperationException而且不作任何操作。ArrayList override这些method来对list进行操作，但是Arrays$ArrayList没有override remove(int)，add(int)等，所以throw UnsupportedOperationException。
        解决方法是使用Iterator，或者转换为ArrayList
        List list = Arrays.asList(a[]);
        List arrayList = new ArrayList(list);
  - 并发问题
      - 当微信小程序端同时上传图片时，读取到的Community为同一个（即imgUrlList为""), 导致后来的把前面的imgUrlList覆盖，最终只存了一个路径
        - 解决方法  
           - 0：前端延时请求每张图片
           - 1：加事务（隔离级别为序列化）  
           - 2：通过imgNo自旋等待追加imgUrlList的形式 
           - 3: JAVA锁 
           - 4: （最终使用）使用SQL语句，"update community c set c.img_url_list=(case when c.img_url_list='' then '" + newImgUrl +"' else concat(c.img_url_list, '"+(imgSeparator+newImgUrl)+"') end) where c.id="+cid;
  - 频繁创建SqlSession, 且JDBC 连接无法被Spring管理
      - https://segmentfault.com/a/1190000015138959?utm_medium=referral&utm_source=tuicool
  - SpringBoot多项目部署项目运行被killed解决办法
      - https://blog.csdn.net/sihan2018/article/details/80146277
  - 
### 规约
  - 后端存储的ImgList， 均以#^$^#分隔
