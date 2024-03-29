package cn.itcast.service.cargo;

import cn.itcast.domain.cargo.Packing;
import com.github.pagehelper.PageInfo;

public interface PackingService {
    PageInfo findAll(String id, int pageNum, int size);

    void save(Packing packing);

    Packing findById(String id);

    void update(Packing packing);

    void deleteById(String id);
}
