<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta content="telephone=no" name="format-detection" />
    <meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=2.0"/>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no">
    <meta name="keywords" content="#" />
    <meta name="description" content="#" />
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="">
    <link rel="apple-touch-icon-precomposed" sizes="57x57" href="">

    <title>Personal center</title>
    <link href="/mobile/css/cssdemo.css" type="text/css" rel="stylesheet" />


</head>

<body style="background:#fafafa;">
<div class="sy_wap">
    <div class="sy_wap_cnter">
        <div class="grz_t2" style="background-color: #37BCF9;">
            <div class="grz_t_img">
                <img src="/mobile/images/hrg_tx1.png" title="" alt=""/><br/>
                <h2>${sessionScope.userName}</h2>
                <p>kilimall member</p>
            </div>

        </div>

        <div class="grzx_h1">
            <ul>
                <a href="#" title="">
                    <li>
                        <i></i>
                        <span>View all orders</span>
                        <em class="grzx_h1_l3"></em>
                        <b>Order Tracking</b>
                    </li>
                </a>
            </ul>
            <div class="grzx_5d">
                <a href="#" title="waiting for payment">
              <span>
                <img src="/mobile/images/grzx_x4.png" title="waiting for payment" alt="waiting for payment"/><br/>payment
              </span>
                </a>
                <a href="#" title="Wait for delivery">
              <span>
                <img src="/mobile/images/grzx_x5.png" title="Wait for delivery" alt="Wait for delivery"/><br/>delivery
              </span>
                </a>
                <a href=#" title="Waiting for receipt">
              <span>
                <img src="/mobile/images/grzx_x6.png" title="Waiting for receipt" alt="Waiting for receipt"/><br/>receiving
              </span>
                </a>
                <a href="#" title="Waiting for evaluation">
              <span>
                <img src="/mobile/images/grzx_x7.png" title="Waiting for evaluation" alt="Waiting for evaluation"/><br/>evaluation
              </span>
                </a>
                <a href="#" title="Refund / after sale">
              <span>
                <img src="/mobile/images/grzx_x8.png" title="Refund / after sale" alt="Refund / after sale"/><br/>after sale
              </span>
                </a>
                <div class="clear"></div>
            </div>

        </div>

        <div class="grzx_h1">
            <ul>
                <a href="#" title="">
                    <li>

                            <i ></i>
                            <em class="grzx_h1_l6"></em>
                            <b>Personal settings</b>

                    </li>
                </a>
                <a href="#" title="">
                    <li>

                            <i ></i>
                            <em class="grzx_h1_l7"></em>
                            <b>change Password</b>

                    </li>
                </a>
                <a href="#" title="">
                    <li>

                            <i class=""></i>
                            <em class="grzx_h1_l8"></em>
                            <b>Exit the current account</b>

                    </li>
                </a>

            </ul>
        </div>
    </div>

</div>
<footer>
    <div class="w_current">
        <a href="/mobile/DemoController/toIndexPage.html">
            <i class="w_home"></i>
            <p>Workbench</p>
        </a>
    </div>
    <div class="">
        <a href="">
            <i class="w_vshop"></i>
            <p>Message</p>
        </a>
    </div>
    <div class="">
        <a href="">
            <i class="w_frbj"></i>
            <p>daily</p>
        </a>
    </div>
    <div class="">
        <a href="">
            <i class="w_classify"></i>
            <p>Statistical report</p>
        </a>
    </div>
    <div class="">
        <a href="/mobile/tiaozhuangController/user.html">
            <i class="w_f-cart"></i>
            <p>Personal settings</p>
        </a>
    </div>
</footer>
</body>
</html>
