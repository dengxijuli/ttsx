package com.ttsx.FlashKilling.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ttsx.FlashKilling.Service.IPayLogService;
import com.ttsx.FlashKilling.mapper.PayLogMapper;
import com.ttsx.entity.log.PayLog;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dx
 * @since 2023-08-31
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements IPayLogService {

}
