package com.mobinming.cloud_service.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * <p>
 * 
 * </p>
 *
 * @author mbm
 * @since 2020-11-15
 */
@EntityScan
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="NoteCategory对象", description="")
public class NoteCategory extends Model<NoteCategory> {

    private static final long serialVersionUID = 1L;

      private String id;

    private String name;

    private Date editTime;

    private String userId;

    @TableField("imgUrl")
    private String imgUrl;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
