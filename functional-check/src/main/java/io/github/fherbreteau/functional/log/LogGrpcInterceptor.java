package io.github.fherbreteau.functional.log;

import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogGrpcInterceptor implements ClientInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(LogGrpcInterceptor.class);

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor,
                                                               CallOptions callOptions, Channel channel) {

        LOG.info("Call to {}", methodDescriptor.getFullMethodName());
        return channel.newCall(methodDescriptor, callOptions);
    }
}
