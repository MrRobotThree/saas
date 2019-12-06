package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.PackingDao;
import cn.itcast.domain.cargo.Packing;
import cn.itcast.service.cargo.PackingService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

@Service
public class PackingServiceImpl implements PackingService {

    @Autowired
    private PackingDao packingDao;
    @Override
    public PageInfo findAll(String id,int pageNum, int size) {
        PageHelper.startPage(pageNum,size);
        List<Packing> list = packingDao.findByCompanyId(id);

        return new PageInfo(list);
    }

    @Override
    public void save(Packing packing) {
        packingDao.insertSelective(packing);
    }

    @Override
    public Packing findById(String id) {
        Packing packing = packingDao.selectByPrimaryKey(id);
        return packing;
    }
}
