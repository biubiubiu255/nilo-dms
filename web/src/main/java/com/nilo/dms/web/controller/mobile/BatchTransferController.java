package com.nilo.dms.web.controller.mobile;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mobile/rider/Batch")
public class BatchTransferController {

    @RequestMapping(value = "/sign.html")
    public String sign() {
        return "mobile/rider/BatchTransfer/plzz";
    }
}
