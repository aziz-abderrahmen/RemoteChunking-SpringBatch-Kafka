package com.cfa.configs.hazelcast.serializer;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;
import org.nustaq.serialization.FSTConfiguration;

import java.io.IOException;

/**
 * Serializer configuration
 * @param <T>
 */
@Slf4j
public final class CustomSerializer<T> implements Serializer<T> {

  private static final ThreadLocal<FSTConfiguration> conf = ThreadLocal.withInitial(FSTConfiguration::createUnsafeBinaryConfiguration);

  @SneakyThrows
  @Override
  public byte[] serialize(String s, Object object) {
    if (null == object) {
      return null;
    }
    try {
      return conf.get().asByteArray(object);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new IOException("Failed to serialize object : " + object.getClass(), e);
    }

  }
}
