package com.zzc.micro.jms.comsumer.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Filename: com.zzc.micro.jms.comsumer.message.UserChangeMessage.java</p>
 * <p>Date: 2022-06-27, 周一, 19:45:17.</p>
 *
 * @author zhichuanzhang
 * @version 0.1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChangeMessage implements Serializable {

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 店铺编号
     */
    private String shopId;

    /**
     * 上级编号
     */
    private String adminId;

    /**
     * 千米ID
     */
    private String ticketId;

    /**
     * 用户状态   1正常 2:删除
     */
    private String userStatus;

    /**
     * 审核状态 0:待审核  1:审核通过  -1:审核拒绝
     */
    private String auditStatus;

    /**
     * 邀请状态(0: 邀请中, 1: 已合作)
     */
    private String inviteStatus;

    /**
     * 连锁邀请状态(-2: 清退, -1: 拒绝, 0: 邀请中, 1: 已合作)
     */
    private String o2oInviteStatus;

    /**
     * 锁定状态 0:锁定 1：正常
     */
    private String lockStatus;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 注册时间
     */
    private Date createTime;

    /**
     * 操作类型
     */
    private String operatorType;
}
