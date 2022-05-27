package com.cfa.configs.hazelcast.deserializer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.nustaq.serialization.FSTConfiguration;

/**
 * Deserializer configuration
 * @param <T>
 */
@Slf4j
public final class CustomDeserializer<T> implements Deserializer<T> {

  private static final ThreadLocal<FSTConfiguration> conf = ThreadLocal.withInitial(FSTConfiguration::createUnsafeBinaryConfiguration);

  @SuppressWarnings("unchecked")
  @Override
  public T deserialize(String s, byte[] bytes) {
    return null == bytes ? null : (T)conf.get().asObject(bytes);
  }
}
