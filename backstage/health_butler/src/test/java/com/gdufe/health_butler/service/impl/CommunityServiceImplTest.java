package com.gdufe.health_butler.service.impl;

import com.gdufe.health_butler.HealthButlerApplicationTests;
import com.gdufe.health_butler.common.util.MinioUtils;
import com.gdufe.health_butler.common.util.ThreadPoolUtils;
import com.gdufe.health_butler.service.CommunityService;
import io.minio.MinioClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * @Author: laichengfeng
 * @Description: 测试社区
 * @Date: 2019/3/5 13:34
 */
public class CommunityServiceImplTest extends HealthButlerApplicationTests {

    @Value("${minio.imgBucket}")
    private String imgBucket;

    @Value("${img.separator}")
    private String imgSeparator;

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private CommunityService communityService;

    /**
     * 图片上传到minio并共享
     */
    @Test
    public void upload() {
        try {
//            MinioUtils.FileUploaderByUrl(minioClient, imgBucket, "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2597092681,26265505&fm=26&gp=0.jpg");
            String url = MinioUtils.FileUploaderByFile(minioClient, imgBucket, new File("E:\\codelife\\health_butler\\view\\health_butler\\image\\icon_suit.png"));
//            minioClient.putObject("laichengfeng-health-butler",  "island.jpg", "E:\\codelife\\health_butler\\view\\health_butler\\image\\icon_suit.png");
            System.out.println(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试事务
     */
    @Test
    public void testTransaction() throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = ThreadPoolUtils.createDefaultThreadPoolExecutor(9, 10);
        for(int i=0; i<9; i++) {
//            threadPoolExecutor.execute(() -> communityService.updateImgUrlList(18, "https://play.minio.io:9000/laichengfeng-health-butler/b3dbbfd02b79f7235b20bad30bc96fd7.png"));
        }
        Thread.sleep(100000000000L);
    }

}
