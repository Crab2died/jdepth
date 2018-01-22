package com.github.jvm.io.protocol.c2d.codc.marshalling;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;
import org.jboss.marshalling.*;

import java.io.IOException;

public class MarshallingCodecFactory {
    /**
     * 创建Jboss Marshaller
     */
    public static C2DMarshalEncoder buildMarshalling() throws IOException {
        MarshallerFactory marshallerFactory = Marshalling
                .getProvidedMarshallerFactory("serial");
        MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        MarshallerProvider provider = new DefaultMarshallerProvider(marshallerFactory, configuration);
        return new C2DMarshalEncoder(provider);
    }

    /**
     * 创建Jboss Unmarshaller
     */
    public static C2DMarshalDecoder buildUnMarshalling() throws IOException {
        MarshallerFactory marshallerFactory = Marshalling
                .getProvidedMarshallerFactory("serial");
        MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        UnmarshallerProvider provider = new DefaultUnmarshallerProvider(marshallerFactory, configuration);
        return new C2DMarshalDecoder(provider, 1024 * 1024);
    }
}
