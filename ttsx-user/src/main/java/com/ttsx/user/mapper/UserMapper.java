package com.ttsx.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ttsx.entity.pojo.Memberinfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: mqb
 * @Date: 2023/5/11
 * @Time: 19:57
 * @Description:
 */
@Mapper
public interface UserMapper extends BaseMapper<Memberinfo> {
}
