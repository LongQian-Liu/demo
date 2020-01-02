package com.qf.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import java.util.Random;

@RestController
public class YanZhengMaController {

    public String getMa(){
        String num = "";
        for (int i = 0;i<6;i++){
            int i1 = new Random().nextInt(10);
            num += i1;
        }
        return num;
    }
    @RequestMapping("/getMaByPhone")
    public String getMaByPhone(String phone){
        Jedis jedis = new Jedis("10.8.161.4",6379);
        String k1 = jedis.get("phone:"+phone);
        if (k1 != null){
            if (Integer.parseInt(k1)>=3){
                return "error";
            }else {
                jedis.incr("phone:"+phone);
            }
        }else {
            jedis.setex("phone:"+phone,(3600*24),"1");
        }
        String ma = getMa();
        jedis.setex("ma",30,ma);
        return "success";
    }

    @RequestMapping("/yanZheng")
    public String yanZheng(String yanZhengMa){

        Jedis jedis = new Jedis("10.8.161.4",6379);
        String ma = jedis.get("ma");

        if (ma.equals(yanZhengMa)){
            return "success";
        }
        return "error";
    }
}
