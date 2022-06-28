package com.zzc.micro.jms.comsumer;

import com.zzc.micro.jms.DestinationConstants;
import com.zzc.micro.jms.comsumer.message.TradeOperateMessage;
import com.zzc.micro.stat.biz.events.MarketTradeDeal;
import com.zzc.micro.stat.biz.events.MarketTradeItem;
import com.zzc.micro.stat.core.CollectionEngine;
import com.zzc.micro.supports.configuration.JmsListenerNames;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;

/**
 * 订单消息监听
 * <p>Filename: com.zzc.micro.jms.comsumer.TradeOperateTopicConsumer.java</p>
 * <p>Date: 2022-06-27, 周一, 19:47:19.</p>
 *
 * @author zhichuanzhang
 * @version 0.1.0
 */
@Component
@ConditionalOnProperty(prefix = "debug", name = "jms-consumer.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TradeOperateTopicConsumer extends AbstractJsonMessageConsumer {

    private final CollectionEngine collectionEngine;

    @Override
    String getMessageFlag() {
        return "订单状态变更消息";
    }

    /**
     * 订单状态变更
     */
    @JmsListener(containerFactory = JmsListenerNames.tradeOperateTopicListenerContainerFactory,
            destination = DestinationConstants.TRADE_OPERATE,
            subscription = DestinationConstants.TRADE_OPERATE)
    public void tradeOperate(final Message sourceMessage) {
        log.info("receive message:{} = {}", DestinationConstants.TRADE_OPERATE, sourceMessage);
        super.onMessage(sourceMessage, TradeOperateMessage.class, this::consume);
    }

    private void consume(TradeOperateMessage message) {
        //fixme
        collectionEngine.accept(MarketTradeDeal.builder().build());
        collectionEngine.accept(MarketTradeItem.builder().build());
    }
}
