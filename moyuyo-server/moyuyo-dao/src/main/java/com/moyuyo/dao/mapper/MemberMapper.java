package com.moyuyo.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moyuyo.dao.entity.MemberEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper extends BaseMapper<MemberEntity> {
}
