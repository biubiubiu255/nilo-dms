package com.nilo.dms.web.controller.system;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

/**
 * Created by admin on 2017/12/4.
 */
@Controller
@RequestMapping("/barCode")
public class BarCodeController extends BaseController {

    @RequestMapping("/{content}.html")
    public void createCode128(HttpServletRequest request, HttpServletResponse response, @PathVariable String content) throws Exception {

        if (StringUtil.isEmpty(content)) return;

        BarcodeFormat format = BarcodeFormat.CODE_128;
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                format, 1000, 300);
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        ImageIO.write(image, "png", os);
    }
}
