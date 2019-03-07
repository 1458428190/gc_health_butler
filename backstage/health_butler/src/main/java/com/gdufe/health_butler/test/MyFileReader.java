package com.gdufe.health_butler.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Pattern;

public class MyFileReader {
    public static boolean check(String content, String pattern) {
        // 用于判断该pattern是否在content里面
        boolean result = Pattern.matches(pattern, content);
        return result;
    }

    private static String[] comb(String[] chs) {
        int len = chs.length;
        int nbits = 1 << len;
        String[] result = new String[nbits];
        for (int i = 0; i < nbits; ++i) {
            result[i] = new String();
            int t;
            for (int j = 0; j < len; j++) {
                t = 1 << j;
                if ((t & i) != 0) { // 与运算，同为1时才会是1
                    if(result[i].length() > 0) {
                        result[i] += " ";
                    }
                    result[i] += chs[j];
                }
            }
        }

        /*
        // 结果在result中
        for(String res: result) {
            System.out.println(res);
        }*/
        return result;
    }
    public static void main(String[] args) throws Exception {

        // 加载singer文件，获取所有的歌手名
        String strFile1 = "//Users/yuwanlong/Documents/dywx/project/variety_store/singer_name.txt";
        FileReader fr1 = new FileReader(strFile1);
        BufferedReader br1 = new BufferedReader(fr1);
        String content = "";
        while (br1.readLine() != null) {
            String s1 = br1.readLine();
            content += s1;
        }
        System.out.println(content);
        br1.close();

        // 加载title/description/youtubetagt
        String strFile2 = "//Users/yuwanlong/Documents/dywx/project/variety_store/title/test.csv";
        FileReader fr2 = new FileReader(strFile2);
        BufferedReader br2 = new BufferedReader(fr2);
        while (br2.readLine() != null) {
            String s2 = br2.readLine();

            // 提取第二列title
            String[] s3 = s2.split(",");
            // 切分时剔除特殊字符
            String[] s4 = s3[1].split("[- ) ( ' &]");

            System.out.println(s4);
            String[] s5 = comb(s4);
            // 判断每一个子集是否在singer_name中
            for (String res : s5) {
                boolean ee = check(content, ".*" + res + ".*");
                System.out.println(ee);
            }
        }

        // 调用check函数进行匹配
        String pattern = ".*mc livinho.*";
        boolean gg = check(content, pattern);
        System.out.println(gg);
    }
}
