package com.amazon.aoc.callers;

import com.amazon.aoc.enums.GenericConstants;
import com.amazon.aoc.exception.BaseException;
import com.amazon.aoc.exception.ExceptionCode;
import com.amazon.aoc.helpers.RetryHelper;
import com.amazon.aoc.models.TraceFromEmitter;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.concurrent.atomic.AtomicReference;

public class HttpCaller implements ICaller{
  @Override
  public TraceFromEmitter callSampleApp(String url) throws Exception{
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
      .url(url)
      .build();

    AtomicReference<String> responseContent = new AtomicReference<>();
    RetryHelper.retry(60, ()->{
        try(Response response = client.newCall(request).execute()){
          if(!response.isSuccessful()){
            throw new BaseException(ExceptionCode.DATA_EMITTER_UNAVAILABLE);
          }
          responseContent.set(response.body().string());
        }
      }
    );

    return new ObjectMapper().readValue(responseContent.get(), TraceFromEmitter.class);
  }
}
