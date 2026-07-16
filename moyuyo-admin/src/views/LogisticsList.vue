<template>
  <div class="logistics-list">
    <!-- 警告横幅 -->
    <div class="alert-banner">
      ⚠️ 3个商品库存低于预警线
    </div>

    <h2 class="page-title">物流管理</h2>

    <!-- KPI 卡片 -->
    <div class="kpi-row">
      <div class="kpi-card">
        <div class="kpi-value yellow">12</div>
        <div class="kpi-label">待发货</div>
      </div>
      <div class="kpi-card">
        <div class="kpi-value blue">45</div>
        <div class="kpi-label">运输中</div>
      </div>
      <div class="kpi-card">
        <div class="kpi-value red">2</div>
        <div class="kpi-label">异常</div>
      </div>
      <div class="kpi-card">
        <div class="kpi-value">3.2</div>
        <div class="kpi-label">平均时效（天）</div>
      </div>
    </div>

    <!-- 仓库标签 -->
    <div class="tab-switcher">
      <button
        v-for="wh in warehouses"
        :key="wh.key"
        class="tab-switcher-item"
        :class="{ active: activeWarehouse === wh.key }"
        @click="activeWarehouse = wh.key"
      >
        {{ wh.label }}
      </button>
    </div>

    <div class="logistics-grid">
      <!-- 左栏：运输中的包裹 -->
      <div class="logistics-main">
        <div class="data-table-wrapper">
          <table class="data-table">
            <thead>
              <tr>
                <th>运单号</th>
                <th>承运商</th>
                <th>运输路线</th>
                <th>状态</th>
                <th>发货时间</th>
                <th>预计到达</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="pkg in packages" :key="pkg.trackingNo">
                <td><span class="table-link">{{ pkg.trackingNo }}</span></td>
                <td><span class="tag" :class="pkg.carrierClass">{{ pkg.carrier }}</span></td>
                <td>{{ pkg.route }}</td>
                <td><span class="tag" :class="pkg.statusClass">{{ pkg.statusLabel }}</span></td>
                <td>{{ pkg.shipTime }}</td>
                <td>{{ pkg.eta }}</td>
                <td class="cell-actions">
                  <span class="table-link">追踪</span>
                  <span class="table-link">详情</span>
                </td>
              </tr>
            </tbody>
          </table>
          <div class="pagination">
            <div class="pagination-info">显示 1-6 条，共 45 条</div>
            <div class="pagination-btns">
              <button class="pagination-btn">上一页</button>
              <button class="pagination-btn active">1</button>
              <button class="pagination-btn">2</button>
              <button class="pagination-btn">3</button>
              <button class="pagination-btn">下一页</button>
            </div>
          </div>
        </div>
      </div>

      <!-- 右栏：库存预警 + 承运占比 -->
      <div class="logistics-side">
        <!-- 库存预警 -->
        <div class="card">
          <div class="card-header">
            <h3>库存预警</h3>
          </div>
          <div class="card-body">
            <div class="alert-list">
              <div class="alert-item" v-for="alert in inventoryAlerts" :key="alert.sku">
                <div class="alert-info">
                  <div class="alert-name">{{ alert.name }}</div>
                  <div class="alert-sku">{{ alert.sku }}</div>
                </div>
                <div class="alert-stock">
                  <span class="stock-count">{{ alert.stock }}</span>
                  <span class="stock-unit">件</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 承运商分布 -->
        <div class="card">
          <div class="card-header">
            <h3>承运商分布</h3>
          </div>
          <div class="card-body">
            <div class="carrier-dist">
              <div class="carrier-item" v-for="c in carrierDist" :key="c.name">
                <div class="carrier-header">
                  <span>{{ c.name }}</span>
                  <span class="carrier-pct">{{ c.percent }}%</span>
                </div>
                <div class="bar-track">
                  <div class="bar-fill-cd" :style="{ width: c.percent + '%', background: c.color }"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'

const activeWarehouse = ref('la')

const warehouses = [
  { key: 'la', label: '洛杉矶仓' },
  { key: 'ny', label: '纽约仓' },
  { key: 'london', label: '伦敦仓' }
]

const packages = reactive([
  { trackingNo: 'FDX-20260708-001', carrier: 'FedEx', carrierClass: 'tag-blue', route: '洛杉矶 → 纽约', statusLabel: '运输中', statusClass: 'tag-blue', shipTime: '07-06 09:00', eta: '07-10' },
  { trackingNo: 'UPS-20260708-002', carrier: 'UPS', carrierClass: 'tag-orange', route: '洛杉矶 → 芝加哥', statusLabel: '派送中', statusClass: 'tag-green', shipTime: '07-07 14:00', eta: '07-09' },
  { trackingNo: 'USPS-20260708-003', carrier: 'USPS', carrierClass: 'tag-gray', route: '纽约 → 波士顿', statusLabel: '已签收', statusClass: 'tag-gray', shipTime: '07-05 10:00', eta: '07-08' },
  { trackingNo: 'FDX-20260708-004', carrier: 'FedEx', carrierClass: 'tag-blue', route: '洛杉矶 → 旧金山', statusLabel: '异常', statusClass: 'tag-red', shipTime: '07-06 16:00', eta: '—' },
  { trackingNo: 'UPS-20260708-005', carrier: 'UPS', carrierClass: 'tag-orange', route: '纽约 → 华盛顿', statusLabel: '运输中', statusClass: 'tag-blue', shipTime: '07-08 08:00', eta: '07-11' },
  { trackingNo: 'FDX-20260708-006', carrier: 'FedEx', carrierClass: 'tag-blue', route: '洛杉矶 → 西雅图', statusLabel: '运输中', statusClass: 'tag-blue', shipTime: '07-08 10:00', eta: '07-12' }
])

const inventoryAlerts = reactive([
  { name: '天然深海鱼油软胶囊', sku: 'FISH-OIL-001', stock: 12 },
  { name: '有机螺旋藻片', sku: 'SPIR-002', stock: 8 },
  { name: '维生素C泡腾片', sku: 'VITC-003', stock: 5 }
])

const carrierDist = reactive([
  { name: 'FedEx', percent: 42, color: 'var(--brand-500)' },
  { name: 'UPS', percent: 28, color: 'var(--state-warning)' },
  { name: 'USPS', percent: 18, color: 'var(--state-success)' },
  { name: '其他', percent: 12, color: 'var(--text-300)' }
])
</script>

<style scoped lang="css">
.alert-banner {
  padding: 10px 16px;
  background: var(--state-warning-surface);
  border: 1px solid var(--state-warning);
  border-radius: var(--radius-sm);
  color: var(--state-warning);
  font-size: 13px;
  font-weight: 600;
  margin-bottom: 16px;
}

.page-title {
  font-size: 20px;
  font-weight: 700;
  color: var(--text-800);
  margin: 0 0 20px;
}

/* KPI */
.kpi-row {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
}

.kpi-card {
  flex: 1;
  padding: 20px;
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  box-shadow: var(--shadow-xs);
}

.kpi-value {
  font-size: 28px;
  font-weight: 700;
  color: var(--text-800);
  margin-bottom: 4px;
}

.kpi-value.yellow { color: var(--state-warning); }
.kpi-value.blue { color: var(--primary); }
.kpi-value.red { color: var(--state-error); }

.kpi-label {
  font-size: 13px;
  color: var(--text-400);
}

.logistics-grid {
  display: flex;
  gap: 24px;
  align-items: flex-start;
  margin-top: 16px;
}

.logistics-main {
  flex: 1;
}

.logistics-side {
  width: 320px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* 库存预警 */
.alert-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.alert-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 12px;
  background: var(--state-error-surface);
  border-radius: var(--radius-sm);
}

.alert-name {
  font-size: 13px;
  font-weight: 600;
  color: var(--text-800);
  margin-bottom: 2px;
}

.alert-sku {
  font-size: 11px;
  color: var(--text-400);
}

.stock-count {
  font-size: 20px;
  font-weight: 700;
  color: var(--state-error);
}

.stock-unit {
  font-size: 11px;
  color: var(--text-400);
  margin-left: 2px;
}

/* 承运商分布 */
.carrier-dist {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.carrier-header {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
  color: var(--text-600);
  margin-bottom: 4px;
}

.carrier-pct {
  font-weight: 600;
  color: var(--text-800);
}

.bar-track {
  height: 8px;
  border-radius: 4px;
  background: var(--background-100);
  overflow: hidden;
}

.bar-fill-cd {
  height: 100%;
  border-radius: 4px;
  transition: width 0.3s ease;
}
</style>
