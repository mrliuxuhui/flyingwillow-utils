package com.flyingwillow.utils.concurrent;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.cloud.openfeign.FeignClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class AsyncFeignRequestUtilTest {
    @Mock
    private IAMClient amClient;

    @Test
    public void 代理AMClient() {

        final IAMBaseResponse<String> response = new IAMBaseResponse<>();
        final RespCode respCode = new RespCode();
        respCode.setCode("400");
        response.setRespCode(respCode);
        Mockito.when(amClient.getByAppCode(any()))
                .thenReturn(response);

        final IAMBaseResponse byAppCode = AsyncFeignRequestUtil.asyncOf(amClient).getByAppCode("");

        assertThat(byAppCode.getRespCode().getCode()).isEqualTo("400");
    }

    @FeignClient
    public interface IAMClient {
        IAMBaseResponse getByAppCode(String req);
    }

    public static class IAMBaseResponse<T> {
        RespCode respCode;
        T data;

        public void setRespCode(RespCode respCode) {
            this.respCode = respCode;
        }

        public void setData(T data) {
            this.data = data;
        }

        public RespCode getRespCode() {
            return respCode;
        }

        public T getData() {
            return data;
        }
    }

    private class RespCode {
        String message;
        String code;

        public String getMessage() {
            return message;
        }

        public String getCode() {
            return code;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
