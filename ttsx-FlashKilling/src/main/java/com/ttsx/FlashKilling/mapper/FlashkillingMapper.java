package com.ttsx.FlashKilling.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ttsx.entity.pojo.FlashKilling;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dx
 * @since 2023-08-22
 */
public interface FlashkillingMapper extends BaseMapper<FlashKilling> {

    Integer decrStock(@Param("fno") Integer fno);

    Integer incrStock(@Param("fno") Integer fno);

}
