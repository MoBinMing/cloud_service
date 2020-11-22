package com.mobinming.cloud_service.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
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
@ApiModel(value="NoteItme对象", description="")
public class NoteItme extends Model<NoteItme> {

    private static final long serialVersionUID = 1L;

      private String id;

    private String noteCategoryId;

    private String name;

    private String content;

    private Date editDate;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
