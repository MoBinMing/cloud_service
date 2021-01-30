package com.mobinming.cloud_service.service.impl;

import com.mobinming.cloud_service.entity.ChitchatMessage;
import com.mobinming.cloud_service.mapper.ChitchatMessageMapper;
import com.mobinming.cloud_service.service.ChitchatMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mbm
 * @since 2021-01-28
 */
@Service
public class ChitchatMessageServiceImpl extends ServiceImpl<ChitchatMessageMapper, ChitchatMessage> implements ChitchatMessageService {
    @Override
    public boolean save(ChitchatMessage entity) {
        return super.save(entity);
    }
}
