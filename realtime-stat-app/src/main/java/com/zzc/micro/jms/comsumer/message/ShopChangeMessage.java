package com.zzc.micro.jms.comsumer.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Filename: com.zzc.micro.jms.comsumer.message.ShopChangeMessage.java</p>
 * <p>Date: 2022-06-27, 周一, 19:44:38.</p>
 *
 * @author zhichuanzhang
 * @version 0.1.0
 */
@Data
public class ShopChangeMessage implements Serializable {
    private static final long serialVersionUID = -5983805851465247375L;

    private String  adminId; // 非空
    private String  operatorType; // 非空
    private String  storeName;
    @JsonProperty("sid")
    @JSONField(name = "sid")
    private String  sceneId;
    @JsonProperty("bname")
    @JSONField(name = "bname")
    private String  sceneCode;
    private Date    addTime;
    private Date    expireTime;
    private Integer customerNum;
    private String  ticketId;
    private String  cellphone;
    private String  storeCode;
    private String  storeLogo;
    private String  jobType;
    private String  signStatus;
    private String  authStatus;
    private String  regSource;
    private String  lockStatus;
    private String  saleName;
    private String  saleOrgName;
}
