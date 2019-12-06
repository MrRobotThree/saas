package cn.itcast.dao.cargo;


import cn.itcast.domain.cargo.Packing;

import java.util.List;

public interface PackingDao {
    int deleteByPrimaryKey(String packingListId);

    int insert(Packing record);

    int insertSelective(Packing record);

    Packing selectByPrimaryKey(String packingListId);

    int updateByPrimaryKeySelective(Packing record);

    int updateByPrimaryKey(Packing record);

    List<Packing> findByCompanyId(String id);


}