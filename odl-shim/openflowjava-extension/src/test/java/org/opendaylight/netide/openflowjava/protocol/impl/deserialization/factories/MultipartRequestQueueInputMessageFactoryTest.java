/*
 * Copyright (c) 2015 NetIDE Consortium and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.netide.openflowjava.protocol.impl.deserialization.factories;

import io.netty.buffer.ByteBuf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opendaylight.netide.openflowjava.protocol.impl.deserialization.NetIdeDeserializerRegistryImpl;
import org.opendaylight.netide.openflowjava.protocol.impl.util.BufferHelper;
import org.opendaylight.openflowjava.protocol.api.extensibility.DeserializerRegistry;
import org.opendaylight.openflowjava.protocol.api.keys.MessageCodeKey;
import org.opendaylight.openflowjava.protocol.api.util.EncodeConstants;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.common.types.rev130731.MultipartRequestFlags;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.common.types.rev130731.MultipartType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.MultipartRequestInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.multipart.request.multipart.request.body.MultipartRequestQueueCase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.multipart.request.multipart.request.body.MultipartRequestQueueCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.multipart.request.multipart.request.body.multipart.request.queue._case.MultipartRequestQueueBuilder;

/**
 * @author giuseppex.petralia@intel.com
 *
 */
public class MultipartRequestQueueInputMessageFactoryTest {
    ByteBuf bb = BufferHelper.buildBuffer("00 05 00 01 00 00 00 00 00 00 08 d0 00 00 08 a3");

    MultipartRequestInput deserializedMessage;

    @Before
    public void startUp() throws Exception {
        DeserializerRegistry desRegistry = new NetIdeDeserializerRegistryImpl();
        desRegistry.init();
        MultipartRequestInputMessageFactory factory = desRegistry
                .getDeserializer(new MessageCodeKey(EncodeConstants.OF13_VERSION_ID, 18, MultipartRequestInput.class));

        deserializedMessage = BufferHelper.deserialize(factory, bb);
    }

    @Test
    public void test() throws Exception {
        BufferHelper.checkHeaderV13(deserializedMessage);

        Assert.assertEquals("Wrong type", MultipartType.forValue(5), deserializedMessage.getType());
        Assert.assertEquals("Wrong flags", new MultipartRequestFlags(true), deserializedMessage.getFlags());
        Assert.assertEquals("Wrong aggregate", createRequestQueue(), deserializedMessage.getMultipartRequestBody());
    }

    private static MultipartRequestQueueCase createRequestQueue() {
        MultipartRequestQueueCaseBuilder caseBuilder = new MultipartRequestQueueCaseBuilder();
        MultipartRequestQueueBuilder builder = new MultipartRequestQueueBuilder();
        builder.setPortNo(2256L);
        builder.setQueueId(2211L);
        caseBuilder.setMultipartRequestQueue(builder.build());
        return caseBuilder.build();
    }

}
