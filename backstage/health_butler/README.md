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
  - Aop

   
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

  - 健康食谱
      - 查询分类
          - /food/category
      - 查询食物列表(关键字搜索，分类搜索)
          - /food/list?keyword=&fcid=
      - 查询食物详情
          - /food/detail?fid=

  - 健康资讯
      - 查询分类
          - /article/category
      - 分页查询指定分类的文章列表
          - /article/pageList?cid=&page=&size=
      - 查询指定分类的所有文章列表
          - /article/list?cid=
      - 查询文章数
          - /article/list_size?cid=
      - 查询文章详情
          - /article/detail?id=

  - 用户信息相关
      - 保存或更新用户信息
          - /user/save/userInfo?token=&iv=&encryptedData=
      - 获取token
          - /user/getToken?code=
      - 查询主页信息(封面, 提醒, 总步数, 总健康币, 排名, 个人信息)
          - /user/info?token=
      - 查询排行榜数据
          - /user/rank?token=
      - 设置打卡时间提醒
          - /user/set/clockInRemind?token=&type=&time=
      - 访问主页信息（可以是自己，也可以是他人的）
          - /user/seeHomePage?token=&toUid=
      - 邀请用户
          - /user/invite?token=
      - 打赏
          - /user/reward?toUid=&coin=&token=
      - 个人主页封面上传更新(POST)
          - /user/cover/upload?token=&imgNo=&file=
      - 重置封面
          - /user/cover/reset?token=

  - BMI指数计算相关
      - 保存BMI计算记录
          - /bmiRecord/save?token=&height=&weight
      - 查询BMI历史记录
          - /bmiRecord/list?token=

  - AI测肤相关
      - 测肤(POST)
          - /aiSkin/measure?token=&file=
      - 查询测肤历史
          - /aiSkin/history?token=
      - 查询指定测肤记录的结果
          - /aiSkin/inquiry?token=&id=

  - 打卡功能相关
      - 获取各打卡记录(早睡、早起、运动)
          - /clockIn/list?token=
      - 打卡(早睡、早起、运动)
          - /clockIn/clockIn?token=&type=

  - 健康卡交易明细相关
      - 获取交易明细
          - /coinDetail/list?token=

  - 健康小圈相关
      - 获取社区分享
          - /community/list?token=
      - 获取社区分享 动态数
          - /community/list_size?token=
      - 分页获取动态
          - /community/pageList?token=&pageNo=&size=
      - 分享发表内容
          - /community/share?token=&content=&onlyMe=
      - 上传发表的图片文件(POST)
          - /community/upload?token=&cid=&imgNo=&file=
      - 删除动态
          - /community/delete?token=&cid=
      - 获取自己的动态
          - /community/getMe?token=
      - 获取指定的动态详情
          - /community/getByCid?token=&cid=
      - 点赞或取消点赞
          - /communityRecord/praise?token=&cid=&type=
      - 评论
          - /communityRecord/comment?token=&cid=&content=
      - 回复评论
          - /communityRecord/replay?token=&cid=&content=&toUid=
      - 删除回复或删除评论
          - /communityRecord/deleteComment?token=&rid=
      - 获取未读消息的个数
          - /infoRecord/getUnReadInfoCount?token=
      - 获取未读消息列表
          - /infoRecord/getUnReadInfo?token=
      - 获取已读消息列表
          - /infoRecord/getReadInfo?token=

  - 兑换的商品相关
      - 查询可兑换的商品列表
          - /goods/list
      - 兑换商品
          - /goods/conversion?token=&id=

  - 后台管理相关
      - 赠送健康币(此接口有ip访问限制，仅限服务器本身可以访问)
          - /manager/give?uid=&coin=

  - 操作手册相关
      - 获取操作手册内容
          - /operation/list

  - 用户记录相关
      - 上传步数数据
          - /record/getAndUploadRunData?token=&iv=&encryptedData=
      - 查询任务列表
          - /record/task
      - 查询兑换记录
          - /record/conversion?token=
      - 获取已兑换的商品详情
          - /record/getDetail?token=&rid=
      - 获取步数记录
          - /record/step?token=&toUid=

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
