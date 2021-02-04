package me.zhaotb.oauth.test;


import org.junit.Test;
import org.springframework.util.AntPathMatcher;

/**
 * @author zhaotangbo
 * @date 2021/2/3
 */
public class TestUrlMather {

    @Test
    public void testAntMatcher() {
        String pattern1 = "/resource/*";
        String pattern2 = "/resource/**";

        String url1 = "/resource";
        String url2 = "/resource/abc";
        String url3 = "/resource/abc/123";
        String url4 = "/resources";

        AntPathMatcher matcher = new AntPathMatcher();
        System.out.println(matcher.isPattern(pattern1));
        System.out.println(matcher.isPattern(pattern2));
        System.out.println("===========");

        System.out.println(matcher.match(pattern1, url1));
        System.out.println(matcher.match(pattern1, url2));
        System.out.println(matcher.match(pattern1, url3));
        System.out.println(matcher.match(pattern1, url4));
        System.out.println("===========");

        System.out.println(matcher.match(pattern2, url1));
        System.out.println(matcher.match(pattern2, url2));
        System.out.println(matcher.match(pattern2, url3));
        System.out.println(matcher.match(pattern2, url4));
        System.out.println("===========");
    }


}
