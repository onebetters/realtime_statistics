package com.zzc.micro.jms.comsumer;

import com.zzc.micro.jms.DestinationConstants;
import com.zzc.micro.jms.comsumer.message.UserChangeMessage;
import com.zzc.micro.stat.biz.events.MarketBuyerJoin;
import com.zzc.micro.stat.biz.events.MarketSellerAdd;
import com.zzc.micro.stat.core.CollectionEngine;
import com.zzc.micro.supports.configuration.JmsListenerNames;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;

/**
 * <p>Filename: com.zzc.micro.jms.comsumer.UserChangeConsumer.java</p>
 * <p>Date: 2022-06-27, 周一, 19:48:54.</p>
 *
 * @author zhichuanzhang
 * @version 0.1.0
 */
@Component
@ConditionalOnProperty(prefix = "debug", name = "jms-consumer.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserChangeConsumer extends AbstractJsonMessageConsumer {

    private final CollectionEngine collectionEngine;

    @JmsListener(containerFactory = JmsListenerNames.userChangeTopicListenerContainerFactory,
            destination = DestinationConstants.USER_CHANGE_TOPIC,
            subscription = DestinationConstants.USER_CHANGE_TOPIC)
    public void onUserChange(final Message sourceMessage) {
        log.info("receive message:{} = {}", DestinationConstants.USER_CHANGE_TOPIC, sourceMessage);
        super.onMessage(sourceMessage, UserChangeMessage.class, this::consume);
    }

    private void consume(UserChangeMessage message) {
        doMarketUserIncrementRealtimeStat(message);
    }

    private void doMarketUserIncrementRealtimeStat(UserChangeMessage message) {
        // 市场商家
        collectionEngine.accept(MarketSellerAdd.builder()
                                               .userId(message.getUserId())
                                               // .appId(appId)
                                               // .b2bGroupId(b2bGroupId)
                                               // .marketId(message.getAdminId())
                                               // .sellerShopId(message.getShopId())
                                               // .sellerProvince(customerShop.getProvince())
                                               // .sellerCity(customerShop.getCity())
                                               // .baseTime(DateTimeUtils.toLocalDateTime(auditTime))
                                               .build());
        // 市场客户
        collectionEngine.accept(MarketBuyerJoin.builder()
                                               .userId(message.getUserId())
                                               // .appId(appId)
                                               // .b2bGroupId(b2bGroupId)
                                               .marketId(message.getAdminId())
                                               .buyerShopId(message.getShopId())
                                               // .buyerProvince(customerShop.getProvince())
                                               // .buyerCity(customerShop.getCity())
                                               // .baseTime(DateTimeUtils.toLocalDateTime(auditTime))
                                               .build());
    }

    @Override
    String getMessageFlag() {
        return "用户变更消息";
    }
}
