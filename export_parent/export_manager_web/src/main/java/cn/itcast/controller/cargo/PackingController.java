package cn.itcast.controller.cargo;

import cn.itcast.controller.BaseController;
import cn.itcast.domain.cargo.ExportExample;
import cn.itcast.domain.cargo.Packing;
import cn.itcast.service.cargo.ExportService;
import cn.itcast.service.cargo.PackingService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
@RequestMapping("/cargo/packing")
public class PackingController extends BaseController {

    @Reference
    private PackingService packingService;
    @Reference
    private ExportService exportService;

    @RequestMapping(value = "/list", name = "展示装箱单数据")
    public String findPage(@RequestParam(name = "page", defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int size) {
        //查询装箱单数据
        PageInfo pageInfo = packingService.findAll(getUser().getCompanyId(), pageNum, size);
        request.setAttribute("page", pageInfo);
        return "cargo/packing/packing-list";
    }

    @RequestMapping(value = "/toAdd", name = "进入添加装箱单页面")
    public String toAdd(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        //需要查询报运单数据
        ExportExample exportExample = new ExportExample();
        ExportExample.Criteria criteria = exportExample.createCriteria();
        criteria.andStateEqualTo(1);
        criteria.andCompanyIdEqualTo(getCompanyId());
        exportExample.setOrderByClause("create_time desc");
        PageInfo pageInfo = exportService.findAll(exportExample, page, size);
        request.setAttribute("page", pageInfo);
        return "cargo/packing/packing-add";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        Packing packing=packingService.findById(id);
        request.setAttribute("packing",packing);
        return "cargo/packing/packing-update";
    }

    @RequestMapping(value = "/edit", name = "添加装箱单")
    public String edit(Packing packing,String id) {
        if(StringUtils.isEmpty(packing.getPackingListId())) {
            packing.setPackingListId(id);
            packing.setState(0L);
            packing.setCreateBy(getUser().getUserName());
            packing.setCreateDept(getUser().getDeptId());
            packing.setCreateTime(new Date());
            packing.setCompanyId(getCompanyId());
            packing.setCompanyName(getCompanyName());
            packingService.save(packing);
        }
        return "redirect:/cargo/packing/list.do";
    }


}
