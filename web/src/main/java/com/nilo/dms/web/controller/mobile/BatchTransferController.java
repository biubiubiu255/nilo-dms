package com.nilo.dms.web.controller.mobile;
import com.nilo.dms.dao.WaybillTaskDao;
import com.nilo.dms.dao.dataobject.WaybillTaskDo;
import com.nilo.dms.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/mobile/rider/Batch")
public class BatchTransferController extends BaseController {
    @Autowired
    private WaybillTaskDao waybillTaskDao;

    @RequestMapping(value = "/sign.html")
    public String sign(Model model) {
        List<WaybillTaskDo> list = waybillTaskDao.queryAllWaybillTask();
//        for (WaybillTaskDo l : list) {
//            System.out.println(l);
//        }
        model.addAttribute("list", list);
            return "mobile/rider/BatchTransfer/plzz";
    }

    @RequestMapping(value = "/Complete.html")
    public String Complete(Model model,String orderNo[]) {
        System.out.println("---------------");
        for(int i=0;i<orderNo.length;i++){
            System.out.println(orderNo[i]);
        }
        return "mobile/rider/BatchTransfer/plzz2";
    }

    @ResponseBody
    @RequestMapping(value = "/submit.html", method = RequestMethod.POST)
    public String submit(MultipartFile file, int totalOrders, double totalSum, String serialNo) {
        System.out.println(totalOrders);
        System.out.println(totalSum);
        System.out.println(serialNo);
        return toJsonTrueMsg();
    }
}
