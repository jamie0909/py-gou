package com.pinyougou.mapper;

import com.pinyougou.pojo.TbSalesreturn;
import com.pinyougou.pojo.TbSalesreturnExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbSalesreturnMapper {
    int countByExample(TbSalesreturnExample example);

    int deleteByExample(TbSalesreturnExample example);

    int deleteByPrimaryKey(String orderId);

    int insert(TbSalesreturn record);

    int insertSelective(TbSalesreturn record);

    List<TbSalesreturn> selectByExampleWithBLOBs(TbSalesreturnExample example);

    List<TbSalesreturn> selectByExample(TbSalesreturnExample example);

    TbSalesreturn selectByPrimaryKey(String orderId);

    int updateByExampleSelective(@Param("record") TbSalesreturn record, @Param("example") TbSalesreturnExample example);

    int updateByExampleWithBLOBs(@Param("record") TbSalesreturn record, @Param("example") TbSalesreturnExample example);

    int updateByExample(@Param("record") TbSalesreturn record, @Param("example") TbSalesreturnExample example);

    int updateByPrimaryKeySelective(TbSalesreturn record);

    int updateByPrimaryKeyWithBLOBs(TbSalesreturn record);

    int updateByPrimaryKey(TbSalesreturn record);
}