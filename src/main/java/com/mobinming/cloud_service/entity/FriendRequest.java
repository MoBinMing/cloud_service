package com.mobinming.cloud_service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.util.Date;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 *
 * </p>
 *
 * @author mbm
 * @since 2021-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "FriendRequest对象", description = "")
public class FriendRequest extends Model<FriendRequest> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "发起申请的用户id")
    private Integer userId;

    @ApiModelProperty(value = "申请的用户id")
    private Integer requestUserId;

    @ApiModelProperty(value = "申请状态：-1申请、0拒绝、1同意")
    private Integer state;

    @ApiModelProperty(value = "发起申请时间")
    private Date requestTime;

    @ApiModelProperty(value = "同意申请时间")
    private Date agreeTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
