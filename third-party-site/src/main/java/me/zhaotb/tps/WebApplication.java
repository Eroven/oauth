package me.zhaotb.tps;

import com.thebeastshop.forest.springboot.annotation.ForestScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhaotangbo
 * @date 2021/2/5
 */
@SpringBootApplication
@ForestScan(basePackages = {"me.zhaotb.tps.web.http"})
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
