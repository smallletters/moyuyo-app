package com.moyuyo.api.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyuyo.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Tag(name = "管理后台")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    @Value("${admin.email:}")
    private String adminEmail;

    @Value("${admin.password:}")
    private String adminPassword;

    @Operation(summary = "管理员登录")
    @PostMapping("/auth/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        if (adminEmail.isEmpty() || adminPassword.isEmpty()) {
            return Result.error(500, "管理员账号未配置，请在 application-prod.yml 中设置 admin.email 和 admin.password");
        }
        if (adminEmail.equals(email) && adminPassword.equals(password)) {
            Map<String, Object> data = new HashMap<>();
            data.put("token", "admin_token_" + UUID.randomUUID().toString().substring(0, 8));
            data.put("name", "Admin");
            data.put("role", "超级管理员");
            return Result.success(data);
        }
        return Result.error(401, "邮箱或密码错误");
    }

    @Operation(summary = "仪表盘统计数据")
    @GetMapping("/dashboard/stats")
    public Result<Map<String, Object>> dashboardStats() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("todayGmv", 12580);
        data.put("todayOrders", 156);
        data.put("activeUsers", 2341);
        data.put("conversionRate", 3.8);
        data.put("gmvTrend", 12.5);
        data.put("ordersTrend", 8.3);
        data.put("usersTrend", -2.1);
        data.put("rateTrend", 0.5);
        data.put("pendingShip", 5);
        data.put("pendingReview", 3);
        data.put("pendingRefund", 1);
        return Result.success(data);
    }

    @Operation(summary = "最近订单")
    @GetMapping("/dashboard/recent-orders")
    public Result<List<Map<String, Object>>> recentOrders() {
        List<Map<String, Object>> list = new ArrayList<>();
        String[][] data = {
                {"MOY20260708012", "宠物洗护套装", "129.00", "PENDING_SHIP"},
                {"MOY20260708011", "舒适胸背带-M", "49.00", "SHIPPED"},
                {"MOY20260708010", "益智玩具球", "35.00", "COMPLETED"},
                {"MOY20260708009", "宠物航空箱-L", "299.00", "PENDING_SHIP"},
                {"MOY20260708008", "鹿角磨牙棒", "22.00", "COMPLETED"},
        };
        for (String[] row : data) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("orderNo", row[0]);
            item.put("productName", row[1]);
            item.put("amount", new BigDecimal(row[2]));
            item.put("status", row[3]);
            list.add(item);
        }
        return Result.success(list);
    }

    @Operation(summary = "销售趋势")
    @GetMapping("/dashboard/sales-trend")
    public Result<List<Map<String, Object>>> salesTrend() {
        List<Map<String, Object>> list = new ArrayList<>();
        String[] days = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        double[] values = {9800, 11200, 10500, 12300, 13600, 11800, 10800};
        for (int i = 0; i < days.length; i++) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("day", days[i]);
            item.put("value", values[i]);
            list.add(item);
        }
        return Result.success(list);
    }

    @Operation(summary = "订单列表")
    @GetMapping("/orders")
    public Result<Page<Map<String, Object>>> orderList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        Page<Map<String, Object>> result = new Page<>(page, size, 128);
        List<Map<String, Object>> records = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("orderNo", "MOY202607" + String.format("%04d", i));
            item.put("userName", "User " + i);
            item.put("productName", "Product " + i);
            item.put("amount", new BigDecimal(50 + i * 10));
            item.put("status", i % 7 == 0 ? "CANCELLED" : i % 5 == 0 ? "COMPLETED" : i % 3 == 0 ? "SHIPPED" : "PENDING_SHIP");
            item.put("createTime", LocalDateTime.now().minusDays(i));
            records.add(item);
        }
        result.setRecords(records);
        return Result.success(result);
    }

    @Operation(summary = "商品列表")
    @GetMapping("/products")
    public Result<Page<Map<String, Object>>> productList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int size) {
        Page<Map<String, Object>> result = new Page<>(page, size, 128);
        List<Map<String, Object>> records = new ArrayList<>();
        String[] names = {"高端宠物洗护套装", "舒适胸背带", "互动益智玩具", "宠物潮流外套", "智能喂食器", "宠物航空箱"};
        for (String name : names) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("name", name);
            item.put("sku", "SKU-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
            item.put("price", new BigDecimal(30 + Math.random() * 300));
            item.put("stock", (int) (Math.random() * 100));
            item.put("sales", (int) (Math.random() * 500));
            item.put("status", "ON_SALE");
            records.add(item);
        }
        result.setRecords(records);
        return Result.success(result);
    }

    @Operation(summary = "用户列表")
    @GetMapping("/users")
    public Result<Page<Map<String, Object>>> userList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int size) {
        Page<Map<String, Object>> result = new Page<>(page, size, 12580);
        List<Map<String, Object>> records = new ArrayList<>();
        String[][] users = {
                {"Luna Chen", "luna@example.com", "VIP"},
                {"Milo Wang", "milo@example.com", "V2"},
                {"Sophie Li", "sophie@example.com", "V1"},
                {"Kevin Zhang", "kevin@example.com", "NORMAL"},
                {"Amy Xu", "amy@example.com", "V1"},
                {"Oliver Zhao", "oliver@example.com", "VIP"},
                {"Emma Liu", "emma@example.com", "V2"},
                {"Jack Huang", "jack@example.com", "NORMAL"},
        };
        for (String[] u : users) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("name", u[0]);
            item.put("email", u[1]);
            item.put("level", u[2]);
            item.put("orders", (int) (Math.random() * 60));
            item.put("spent", new BigDecimal(Math.random() * 12000));
            item.put("status", "ACTIVE");
            item.put("registerTime", LocalDateTime.now().minusDays((long) (Math.random() * 365)));
            records.add(item);
        }
        result.setRecords(records);
        return Result.success(result);
    }

    @Operation(summary = "工单列表")
    @GetMapping("/tickets")
    public Result<List<Map<String, Object>>> ticketList() {
        List<Map<String, Object>> records = new ArrayList<>();
        String[][] data = {
                {"TK-001", "退款", "高", "商品质量问题", "Alice", "PENDING"},
                {"TK-002", "物流", "中", "快递丢失", "Bob", "PROCESSING"},
                {"TK-003", "咨询", "低", "尺码推荐", "Charlie", "CLOSED"},
                {"TK-004", "投诉", "高", "客服态度差", "David", "PROCESSING"},
                {"TK-005", "退款", "中", "申请仅退款", "Eva", "PENDING"},
        };
        for (String[] t : data) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("ticketNo", t[0]);
            item.put("type", t[1]);
            item.put("priority", t[2]);
            item.put("title", t[3]);
            item.put("user", t[4]);
            item.put("status", t[5]);
            item.put("createTime", LocalDateTime.now().minusHours((long) (Math.random() * 48)));
            records.add(item);
        }
        return Result.success(records);
    }

    @Operation(summary = "退款列表")
    @GetMapping("/refunds")
    public Result<List<Map<String, Object>>> refundList() {
        List<Map<String, Object>> records = new ArrayList<>();
        String[][] data = {
                {"RF-001", "质量问题", "APPROVED"},
                {"RF-002", "不想要了", "PENDING"},
                {"RF-003", "商品损坏", "PENDING"},
                {"RF-004", "空包裹", "REJECTED"},
                {"RF-005", "其他", "COMPLETED"},
        };
        for (String[] r : data) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("refundNo", r[0]);
            item.put("reason", r[1]);
            item.put("status", r[2]);
            item.put("amount", new BigDecimal(30 + Math.random() * 200));
            records.add(item);
        }
        return Result.success(records);
    }

    @Operation(summary = "营销活动列表")
    @GetMapping("/campaigns")
    public Result<List<Map<String, Object>>> campaignList() {
        List<Map<String, Object>> records = new ArrayList<>();
        String[][] data = {
                {"夏日宠物狂欢节", "ACTIVE", "限时秒杀全场6折起"},
                {"满减特惠周", "UPCOMING", "满200减30，满300减50"},
                {"新人专属优惠券", "ACTIVE", "新人注册即享"},
                {"限时折扣清仓", "ENDED", "换季清仓5折起"},
        };
        for (String[] c : data) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("name", c[0]);
            item.put("status", c[1]);
            item.put("desc", c[2]);
            item.put("participants", (int) (Math.random() * 500));
            item.put("gmv", new BigDecimal(Math.random() * 50000));
            records.add(item);
        }
        return Result.success(records);
    }
}
