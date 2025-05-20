package space.astralbridge.spring.moviehub.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.dto.PaymentRequest;
import space.astralbridge.spring.moviehub.dto.PaymentResult;
import space.astralbridge.spring.moviehub.entity.PaymentOrder;
import space.astralbridge.spring.moviehub.entity.User;
import space.astralbridge.spring.moviehub.security.UserDetailsImpl;
import space.astralbridge.spring.moviehub.service.PaymentService;
import space.astralbridge.spring.moviehub.service.UserService;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;

    /**
     * 发起支付宝支付请求 (以购买VIP为例)
     */
    @PostMapping("/pay/vip")
    public Result<PaymentResult> payVip(@RequestBody(required = false) PaymentRequest request) {
        // 1. 获取当前用户ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId;

        // 安全地获取用户ID
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            userId = ((UserDetailsImpl) principal).getId();
        } else {
            // 未能获取到用户ID，返回错误
            return Result.fail("用户未登录或会话已过期");
        }

        // 检查用户是否存在
        User user = userService.getById(userId);
        if (user == null) {
            return Result.fail("用户不存在");
        }else{
            System.out.println("用户存在" + user.getUsername());
        }

        // 检查用户是否已经是VIP
        if (user.getUserType() != null && user.getUserType() == 1) {
            return Result.fail("您已经是VIP会员，无需重复购买");
        }

        // 2. 确保request不为空
        if (request == null) {
            request = new PaymentRequest(); // 使用默认月度VIP
        }

        // 3. 调用支付服务创建支付表单
        String formHtml;
        try {
            formHtml = paymentService.createAlipayForm(userId, request);
            log.info("成功为用户{}创建支付订单", userId);
        } catch (Exception e) {
            log.error("创建支付订单失败", e);
            return Result.fail("创建支付订单失败：" + e.getMessage());
        }

        return Result.success("请在浏览器中提交此表单以完成支付", new PaymentResult(formHtml));
    }
    
    /**
     * 查询当前用户的订单列表
     */
    @GetMapping("/orders")
    public Result<List<PaymentOrder>> getUserOrders() {
        // 获取当前用户ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId;
        
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            userId = ((UserDetailsImpl) principal).getId();
        } else {
            // 未能获取到用户ID，返回错误
            return Result.fail("用户未登录或会话已过期");
        }
        
        // 查询用户订单
        List<PaymentOrder> orders = paymentService.getOrdersByUserId(userId);
        return Result.success("查询成功", orders);
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/orders/{orderNo}")
    public Result<PaymentOrder> getOrderDetail(@PathVariable String orderNo) {
        PaymentOrder order = paymentService.getOrderByOrderNo(orderNo);
        if (order == null) {
            return Result.fail("订单不存在");
        }
        return Result.success("查询成功", order);
    }

    /**
     * 查询当前用户VIP状态
     * 用于前端在支付完成后检查用户是否已成功升级为VIP
     */
    @GetMapping("/vip/status")
    public Result<Map<String, Object>> checkVipStatus(@RequestParam(required = false) Long userId, 
                                                     @RequestParam(required = false) String username) {
        User user = null;
        
        // 优先通过userId查询
        if (userId != null) {
            user = userService.getById(userId);
        } 
        // 其次通过username查询
        else if (username != null && !username.isEmpty()) {
            user = userService.getByUsername(username);
        } 
        // 最后尝试从当前认证信息获取
        else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
                Long authUserId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
                user = userService.getById(authUserId);
            }
        }

        if (user == null) {
            return Result.fail("用户不存在或未提供有效的查询参数");
        }

        Map<String, Object> data = new HashMap<>();
        boolean isVip = user.getUserType() != null && user.getUserType() == 1;
        data.put("isVip", isVip);
        data.put("userType", user.getUserType());
        data.put("username", user.getUsername());

        return Result.success(isVip ? "用户已是VIP会员" : "用户不是VIP会员", data);
    }

    /**
     * 创建带订单信息和自动重定向的HTML响应页面
     */
    private String createHtmlResponseWithOrderInfo(String title, String message, boolean success, String redirectUrl, 
                                                 PaymentOrder order, String tradeNo, User user) {
        String color = success ? "#4CAF50" : "#FF5722";
        String icon = success ? "✓" : "!";
        
        // 格式化订单金额，保留两位小数
        String amountStr = order.getAmount().setScale(2, java.math.RoundingMode.HALF_UP).toString();
        
        // 格式化日期时间
        String createTimeStr = order.getCreateTime() != null ? 
                            order.getCreateTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : 
                            "未知";
        String updateTimeStr = order.getUpdateTime() != null ? 
                            order.getUpdateTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : 
                            "未知";
        
        // 获取订单状态文本
        String statusText = "";
        if (order.getStatus() == 0) {
            statusText = "待支付";
        } else if (order.getStatus() == 1) {
            statusText = "支付成功";
        } else if (order.getStatus() == 2) {
            statusText = "支付失败";
        } else {
            statusText = "未知状态(" + order.getStatus() + ")";
        }
        
        // 获取VIP类型文本
        String vipTypeText = "monthly".equals(order.getVipDuration()) ? "月度VIP会员" : "年度VIP会员";
        
        return "<!DOCTYPE html>\n" +
                "<html lang=\"zh-CN\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>" + title + " - MovieHub</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Microsoft YaHei', sans-serif;\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "            align-items: center;\n" +
                "            min-height: 100vh;\n" +
                "            background-color: #f5f5f5;\n" +
                "            margin: 0;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        .container {\n" +
                "            background-color: white;\n" +
                "            padding: 30px;\n" +
                "            border-radius: 10px;\n" +
                "            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\n" +
                "            text-align: center;\n" +
                "            max-width: 600px;\n" +
                "            width: 100%;\n" +
                "        }\n" +
                "        .icon {\n" +
                "            font-size: 48px;\n" +
                "            color: " + color + ";\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        h1 {\n" +
                "            color: #333;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        p {\n" +
                "            color: #666;\n" +
                "            margin-bottom: 25px;\n" +
                "        }\n" +
                "        .btn {\n" +
                "            display: inline-block;\n" +
                "            background-color: #1677ff;\n" +
                "            color: white;\n" +
                "            padding: 12px 24px;\n" +
                "            border-radius: 4px;\n" +
                "            text-decoration: none;\n" +
                "            font-size: 16px;\n" +
                "            margin-top: 20px;\n" +
                "        }\n" +
                "        .btn:hover {\n" +
                "            background-color: #0d5dda;\n" +
                "        }\n" +
                "        .order-info {\n" +
                "            background-color: #f9f9f9;\n" +
                "            border: 1px solid #e0e0e0;\n" +
                "            border-radius: 5px;\n" +
                "            padding: 15px;\n" +
                "            margin: 20px 0;\n" +
                "            text-align: left;\n" +
                "        }\n" +
                "        .order-info p {\n" +
                "            margin: 8px 0;\n" +
                "            font-size: 14px;\n" +
                "            color: #333;\n" +
                "        }\n" +
                "        .order-info-title {\n" +
                "            font-weight: bold;\n" +
                "            color: #333;\n" +
                "            border-bottom: 1px solid #e0e0e0;\n" +
                "            padding-bottom: 8px;\n" +
                "            margin-bottom: 10px;\n" +
                "            font-size: 16px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .highlight {\n" +
                "            color: #1677ff;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"icon\">" + icon + "</div>\n" +
                "        <h1>" + title + "</h1>\n" +
                "        <p>" + message + "</p>\n" +
                "        \n" +
                "        <div class=\"order-info\">\n" +
                "            <div class=\"order-info-title\">订单详情</div>\n" +
                "            <p><strong>订单号：</strong> <span class=\"highlight\">" + order.getOrderNo() + "</span></p>\n" +
                "            <p><strong>用户名：</strong> " + user.getUsername() + "</p>\n" +
                "            <p><strong>商品：</strong> " + vipTypeText + "</p>\n" +
                "            <p><strong>金额：</strong> ¥" + amountStr + "</p>\n" +
                "            <p><strong>支付方式：</strong> 支付宝</p>\n" +
                "            <p><strong>支付状态：</strong> <span class=\"highlight\">" + statusText + "</span></p>\n" +
                (tradeNo != null ? "            <p><strong>支付宝交易号：</strong> " + tradeNo + "</p>\n" : "") +
                "            <p><strong>创建时间：</strong> " + createTimeStr + "</p>\n" +
                "            <p><strong>更新时间：</strong> " + updateTimeStr + "</p>\n" +
                "        </div>\n" +
                "        \n" +
                "        <a href=\"" + redirectUrl + "\" class=\"btn\">返回首页</a>\n" +
                "    </div>\n" +
                "    <script>\n" +
                "        // 15秒后自动跳转到静态首页，不需要登录验证\n" +
                "        setTimeout(function() {\n" +
                "            window.location.href = '" + redirectUrl + "';\n" +
                "        }, 15000);\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";
    }

    /**
     * 支付宝支付结果同步回调
     * 用户支付完成后，支付宝会将用户重定向到这个地址
     * 注意：此方法不依赖用户的登录状态，直接使用订单中的用户信息完成支付流程
     */
    @GetMapping(value = "/return", produces = "text/html;charset=UTF-8")
    public String alipayReturn(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        
        log.info("支付宝同步回调参数: {}", params);
        
        String outTradeNo = params.get("out_trade_no");
        if (outTradeNo == null) {
            return createHtmlResponse("支付结果查询失败", "未能获取订单号，请联系客服", false);
        }
        
        PaymentOrder order = paymentService.getOrderByOrderNo(outTradeNo);
        if (order == null) {
            log.error("订单不存在: {}", outTradeNo);
            return createHtmlResponse("订单不存在", "未找到对应的订单记录，请联系客服", false);
        }
        
        Long userId = order.getUserId();
        User user = userService.getById(userId);
        if (user == null) {
            log.error("订单关联的用户不存在，订单号: {}, 用户ID: {}", outTradeNo, userId);
            return createHtmlResponse("用户不存在", "订单关联的用户不存在，请联系客服", false);
        }
        
        String tradeNo = params.get("trade_no"); 
        
        boolean orderUpdated = false;
        if (order.getStatus() != 1) {
            log.info("将订单 {} 状态更新为支付成功", outTradeNo);
            order.setStatus(1);
            order.setTradeNo(tradeNo != null ? tradeNo : "manual_success");
            orderUpdated = paymentService.updateById(order);
            
            if (!orderUpdated) {
                log.warn("更新订单 {} 状态失败，尝试再次更新", outTradeNo);
                try {
                    PaymentOrder refreshOrder = paymentService.getOrderByOrderNo(outTradeNo);
                    if (refreshOrder != null) {
                        refreshOrder.setStatus(1);
                        refreshOrder.setTradeNo(tradeNo != null ? tradeNo : "manual_success");
                        orderUpdated = paymentService.updateById(refreshOrder);
                        log.info("再次尝试更新订单 {} 状态: {}", outTradeNo, orderUpdated ? "成功" : "失败");
                        if(orderUpdated) order = refreshOrder; 
                    }
                } catch (Exception e) {
                    log.error("再次更新订单状态时出错: {}", outTradeNo, e);
                }
            } else {
                log.info("成功更新订单 {} 状态为已支付", outTradeNo);
            }
        } else {
            log.info("订单 {} 已处于支付成功状态", outTradeNo);
        }
        
        boolean isUserVip = user.getUserType() != null && user.getUserType() == 1;
        if (!isUserVip) {
            log.info("开始为用户 {} 升级VIP", userId);
            boolean vipUpdated = upgradeUserToVip(user, userId, outTradeNo);
            
            if (!vipUpdated) {
                log.warn("通过userService升级用户 {} VIP失败，尝试直接更新", userId);
                for (int i = 0; i < 3 && !isUserVip; i++) {
                    User latestUser = userService.getById(userId);
                    if (latestUser != null) {
                        if (latestUser.getUserType() != null && latestUser.getUserType() == 1) {
                            log.info("用户 {} 已成功升级为VIP (检测到)", userId);
                            isUserVip = true;
                            user = latestUser;
                        } else {
                            latestUser.setUserType(1);
                            boolean directUpdate = userService.updateById(latestUser);
                            log.info("第{}次直接尝试更新用户 {} VIP: {}", i + 1, userId, directUpdate ? "成功" : "失败");
                            if (directUpdate) {
                                isUserVip = true;
                                user = latestUser;
                            }
                        }
                    }
                    if (!isUserVip && i < 2) { // Sleep if not last attempt and not yet VIP
                        try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    }
                }
            } else {
                 isUserVip = true;
                 User updatedUser = userService.getById(userId);
                 if(updatedUser != null) user = updatedUser;
            }
        }
        
        String redirectUrl = "/"; 
        
        log.info("支付流程完成，订单号: {}, 订单状态: {}, 用户VIP: {}", 
                order.getOrderNo(), order.getStatus(), isUserVip);
        
        String resultMessage = isUserVip ? 
                "恭喜您已成功升级为VIP会员！享受全部影片的无限观看权限。" :
                (order.getStatus() == 1 ? "支付已完成！系统正在处理您的VIP权限，请稍后刷新查看。" : "支付处理中，请稍后查看订单状态或联系客服。");
        
        return createHtmlResponseWithOrderInfo(
                "支付成功", 
                resultMessage, 
                true, 
                redirectUrl, 
                order, 
                tradeNo != null ? tradeNo : order.getTradeNo(), 
                user
        );
    }

    /**
     * 创建HTML响应页面
     */
    private String createHtmlResponse(String title, String message, boolean success) {
        String color = success ? "#4CAF50" : "#FF5722";
        String icon = success ? "✓" : "!";
        
        return "<!DOCTYPE html>\n" +
                "<html lang=\"zh-CN\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>" + title + " - MovieHub</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Microsoft YaHei', sans-serif;\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "            align-items: center;\n" +
                "            height: 100vh;\n" +
                "            background-color: #f5f5f5;\n" +
                "            margin: 0;\n" +
                "        }\n" +
                "        .container {\n" +
                "            background-color: white;\n" +
                "            padding: 30px;\n" +
                "            border-radius: 10px;\n" +
                "            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\n" +
                "            text-align: center;\n" +
                "            max-width: 500px;\n" +
                "            width: 100%;\n" +
                "        }\n" +
                "        .icon {\n" +
                "            font-size: 48px;\n" +
                "            color: " + color + ";\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        h1 {\n" +
                "            color: #333;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        p {\n" +
                "            color: #666;\n" +
                "            margin-bottom: 25px;\n" +
                "        }\n" +
                "        .btn {\n" +
                "            display: inline-block;\n" +
                "            background-color: #1677ff;\n" +
                "            color: white;\n" +
                "            padding: 12px 24px;\n" +
                "            border-radius: 4px;\n" +
                "            text-decoration: none;\n" +
                "            font-size: 16px;\n" +
                "            margin-top: 20px;\n" +
                "        }\n" +
                "        .btn:hover {\n" +
                "            background-color: #0d5dda;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"icon\">" + icon + "</div>\n" +
                "        <h1>" + title + "</h1>\n" +
                "        <p>" + message + "</p>\n" +
                "        <a href=\"/\" class=\"btn\">返回首页</a>\n" +
                "    </div>\n" +
                "    <script>\n" +
                "        // 5秒后自动跳转到首页\n" +
                "        setTimeout(function() {\n" +
                "            window.location.href = '/';\n" +
                "        }, 5000);\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";
    }

    /**
     * 升级用户为VIP
     * @param user 用户对象
     * @param userId 用户ID
     * @param orderNo 订单号(用于日志)
     * @return 是否升级成功
     */
    private boolean upgradeUserToVip(User user, Long userId, String orderNo) {
        // 首先检查用户是否已经是VIP
        if (user != null && user.getUserType() != null && user.getUserType() == 1) {
            log.info("用户{}已经是VIP，无需升级", userId);
            return true; // 用户已经是VIP，视为升级成功
        }
        
        log.info("开始执行用户{}升级VIP操作，当前用户类型: {}", userId, user != null ? user.getUserType() : "未知");
        
        // 先刷新一次用户数据，确保拿到最新状态
        User freshUser = userService.getById(userId);
        if (freshUser != null && freshUser.getUserType() != null && freshUser.getUserType() == 1) {
            log.info("刷新用户数据后发现用户{}已经是VIP，无需升级", userId);
            return true;
        }
        
        try {
            // 使用UserService的upgradeToVip方法升级用户
            log.info("调用userService.upgradeToVip升级用户{}为VIP会员，订单号:{}", userId, orderNo);
            boolean success = userService.upgradeToVip(userId);
            
            if (success) {
                log.info("用户{}已成功升级为VIP会员", userId);
                
                // 再次检查确认用户确实被升级为VIP
                User updatedUser = userService.getById(userId);
                if (updatedUser != null && updatedUser.getUserType() != null && updatedUser.getUserType() == 1) {
                    log.info("已确认用户{}的VIP状态已更新，用户类型: {}", userId, updatedUser.getUserType());
                    return true;
                } else {
                    log.warn("用户{}的VIP状态可能未正确更新，当前用户类型: {}, 尝试再次更新", 
                           userId, updatedUser != null ? updatedUser.getUserType() : "null");
                    
                    // 再次尝试直接更新
                    if (updatedUser != null) {
                        updatedUser.setUserType(1);
                        log.info("直接设置用户{}的userType=1并再次尝试更新", userId);
                        boolean directUpdate = userService.updateById(updatedUser);
                        if (directUpdate) {
                            log.info("用户{}的VIP状态已通过直接更新成功设置", userId);
                            
                            // 最终再确认一次
                            User finalCheck = userService.getById(userId);
                            log.info("最终确认: 用户{}的用户类型为: {}", userId, 
                                   finalCheck != null ? finalCheck.getUserType() : "获取失败");
                            
                            return finalCheck != null && finalCheck.getUserType() != null && finalCheck.getUserType() == 1;
                        } else {
                            log.error("直接更新用户{}的VIP状态失败", userId);
                        }
                    } else {
                        log.error("无法获取用户{}的最新数据", userId);
                    }
                    return false;
                }
            } else {
                log.error("升级用户{}为VIP失败, 订单号:{}, 尝试直接更新方式", userId, orderNo);
                
                // 首选方法失败，尝试直接更新
                User directUser = userService.getById(userId);
                if (directUser != null) {
                    directUser.setUserType(1);
                    boolean directUpdate = userService.updateById(directUser);
                    log.info("直接更新用户{}的VIP状态: {}", userId, directUpdate ? "成功" : "失败");
                    return directUpdate;
                }
                
                return false;
            }
        } catch (Exception e) {
            log.error("升级用户{}为VIP过程中发生异常: {}", userId, e.getMessage(), e);
            
            // 即使发生异常，也尝试最后一次直接更新
            try {
                User emergencyUser = userService.getById(userId);
                if (emergencyUser != null) {
                    emergencyUser.setUserType(1);
                    boolean emergencyUpdate = userService.updateById(emergencyUser);
                    log.info("异常恢复: 直接更新用户{}的VIP状态: {}", userId, emergencyUpdate ? "成功" : "失败");
                    return emergencyUpdate;
                }
            } catch (Exception ex) {
                log.error("异常恢复尝试失败", ex);
            }
            
            return false;
        }
    }
}