# AsyncFeignRequestUtil

## 1. 说明
    异步处理feign请求，针对一次数据处理，需要从多个接口取数据时使用，发起请求后，可以进行其他前置工作，待数据需要使用数据时再调取。压缩响应时间。
    通过动态代理实现
   

## 2. 示例

```java

import org.springframework.cloud.openfeign.FeignClient;

class Test {

    private FeignClient client;

    public void test() {
        final BaseResponse response = AsyncFeignRequestUtil.asyncOf(client).getByAppCode("");
    }
}

```
