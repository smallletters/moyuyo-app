package com.moyuyo.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyuyo.dao.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper extends BaseMapper<OrderEntity> {

  /**
   * 按用户分页查询订单，可按订单状态筛选
   */
  Page<OrderEntity> selectPageByUserId(IPage<OrderEntity> page,
                                       @Param("userId") Long userId,
                                       @Param("status") String status);
}
