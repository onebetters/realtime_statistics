package com.zzc.micro.jms.comsumer;

import com.zzc.micro.jms.DestinationConstants;
import com.zzc.micro.jms.comsumer.message.ShopChangeMessage;
import com.zzc.micro.stat.biz.events.MarketAdd;
import com.zzc.micro.stat.biz.events.SellerShopAdd;
import com.zzc.micro.stat.core.CollectionEngine;
import com.zzc.micro.stat.utils.DateTimeUtils;
import com.zzc.micro.supports.configuration.JmsListenerNames;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@ConditionalOnProperty(prefix = "debug", name = "jms-consumer.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShopChangeConsumer extends AbstractJsonMessageConsumer {

    private final CollectionEngine collectionEngine;

    @JmsListener(containerFactory = JmsListenerNames.shopChangeTopicListenerContainerFactory,
            destination = DestinationConstants.SHOP_CHANGE_TOPIC,
            subscription = DestinationConstants.SHOP_CHANGE_TOPIC)
    public void onUserChange(final Message sourceMessage) {
        log.info("receive message:{}", sourceMessage);
        super.onMessage(sourceMessage, ShopChangeMessage.class, this::consume);
    }

    private void consume(ShopChangeMessage message) {
        collectionEngine.accept(buildMarketShopEvent(message));
        collectionEngine.accept(buildSupplierShopEvent(message));
    }

    private MarketAdd buildMarketShopEvent(final ShopChangeMessage message) {
        return MarketAdd.builder()
                // .appId(appIds.stream().findFirst().orElse(null))
                // .b2bGroupId(b2bApi.queryB2bGroupIdByShopId(message.getAdminId()))
                // .shopId(message.getAdminId())
                // .shopSceneCode(message.getSceneCode())
                // .province(store.getProvince())
                // .city(store.getCity())
                .baseTime(Optional.ofNullable(message.getAddTime())
                        .map(DateTimeUtils::toLocalDateTime)
                        .orElse(LocalDateTime.now()))
                .build();
    }

    private SellerShopAdd buildSupplierShopEvent(final ShopChangeMessage message) {
        return SellerShopAdd.builder()
                .shopId(message.getAdminId())
                .shopSceneCode(message.getSceneCode())
                // .province(store.getProvince())
                // .city(store.getCity())
                .baseTime(Optional.ofNullable(message.getAddTime())
                        .map(DateTimeUtils::toLocalDateTime)
                        .orElse(LocalDateTime.now()))
                .build();
    }

    @Override
    String getMessageFlag() {
        return "店铺信息变更消息";
    }
}
