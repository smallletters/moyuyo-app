package com.moyuyo.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moyuyo.dao.entity.OrderItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItemEntity> {

  /**
   * 根据订单ID查询所有订单行
   */
  List<OrderItemEntity> selectByOrderId(@Param("orderId") Long orderId);
}
