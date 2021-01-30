package com.mobinming.cloud_service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.util.Date;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.mobinming.cloud_service.nettyWebSocket.enumType.SendType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author mbm
 * @since 2021-01-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ChitchatMessage对象", description="")
public class ChitchatMessage extends Model<ChitchatMessage> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "聊天记录id")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "发送消息的用户id")
    private Integer sendUserId;

    @ApiModelProperty(value = "接收消息的用户id")
    private Integer toUserId;

    @ApiModelProperty(value = "发送的消息")
    private String msg;

    @ApiModelProperty(value = "是否以发送")
    private Integer isSend;

    @ApiModelProperty(value = "发送时间")
    private Date sentTime;

    @ApiModelProperty(value = "发送类型的值")
    private SendType sendType;


    public boolean isNoEmpty(){
        return sendUserId!=null&&toUserId!=null&&msg!=null&&msg.length()>0&&sendType!=null;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
