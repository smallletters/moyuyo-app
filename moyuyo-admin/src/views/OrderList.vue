<template>
  <div class="order-list-page">
    <div class="page-header">
      <h2 class="page-title">订单列表</h2>
      <div class="status-summary">
        <span class="summary-item">待发货 <strong>12</strong></span>
        <span class="summary-divider">|</span>
        <span class="summary-item">售后中 <strong>3</strong></span>
        <span class="summary-divider">|</span>
        <span class="summary-item summary-item--warn">异常 <strong>1</strong></span>
      </div>
    </div>

    <!-- 查询面板 -->
    <div class="query-panel">
      <div class="form-row">
        <div class="form-group">
          <label>订单状态</label>
          <select v-model="filters.status" class="form-input">
            <option value="">全部状态</option>
            <option value="pending">待付款</option>
            <option value="shipping">待发货</option>
            <option value="shipped">已发货</option>
            <option value="completed">已完成</option>
            <option value="canceled">已取消</option>
          </select>
        </div>
        <div class="form-group">
          <label>开始日期</label>
          <input v-model="filters.dateStart" type="date" class="form-input" />
        </div>
        <div class="form-group">
          <label>结束日期</label>
          <input v-model="filters.dateEnd" type="date" class="form-input" />
        </div>
        <div class="form-group">
          <label>搜索</label>
          <input v-model="filters.search" type="text" class="form-input" placeholder="订单号/用户名" />
        </div>
        <div class="form-group form-group--action">
          <label>&nbsp;</label>
          <button class="btn btn-primary" @click="handleSearch">搜索</button>
          <button class="btn" @click="handleReset">重置</button>
        </div>
      </div>
    </div>

    <!-- 数据表格 -->
    <div class="table-wrapper">
      <table class="data-table">
        <thead>
          <tr>
            <th><input type="checkbox" v-model="selectAll" @change="toggleSelectAll" /></th>
            <th>订单号</th>
            <th>用户</th>
            <th>商品</th>
            <th>金额</th>
            <th>状态</th>
            <th>下单时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in filteredOrders" :key="order.id">
            <td><input type="checkbox" v-model="selectedIds" :value="order.id" /></td>
            <td class="order-no">{{ order.no }}</td>
            <td>{{ order.user }}</td>
            <td>{{ order.items }}</td>
            <td>¥{{ order.amount }}</td>
            <td><span :class="'tag-' + order.statusClass">{{ order.status }}</span></td>
            <td class="order-time">{{ order.time }}</td>
            <td class="action-cell">
              <button class="btn btn-sm" @click="handleConfirmShip(order)">确认发货</button>
              <button class="btn btn-sm" @click="handleLogistics(order)">物流</button>
              <button class="btn btn-sm" @click="handleDetail(order)">详情</button>
            </td>
          </tr>
          <tr v-if="filteredOrders.length === 0">
            <td colspan="8" class="empty-cell">暂无数据</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 分页 -->
    <div class="pagination">
      <button class="btn btn-sm" :disabled="currentPage <= 1" @click="currentPage--">上一页</button>
      <span class="page-info">第 {{ currentPage }} / {{ totalPages }} 页</span>
      <button class="btn btn-sm" :disabled="currentPage >= totalPages" @click="currentPage++">下一页</button>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'

const filters = reactive({
  status: '',
  dateStart: '',
  dateEnd: '',
  search: ''
})

const selectAll = ref(false)
const selectedIds = ref([])
const currentPage = ref(1)
const pageSize = 10

const orderList = [
  { id: 1, no: 'ORD-20240716001', user: '张三', items: '纯棉T恤 × 2', amount: '299.00', status: '待发货', statusClass: 'yellow', time: '2024-07-16 14:30' },
  { id: 2, no: 'ORD-20240716002', user: '李四', items: '运动鞋 × 1', amount: '459.00', status: '已发货', statusClass: 'blue', time: '2024-07-16 13:20' },
  { id: 3, no: 'ORD-20240716003', user: '王五', items: '背包 × 1, 水壶 × 1', amount: '899.00', status: '已完成', statusClass: 'green', time: '2024-07-16 11:45' },
  { id: 4, no: 'ORD-20240715004', user: '赵六', items: '笔记本 × 1', amount: '59.90', status: '待付款', statusClass: 'gray', time: '2024-07-15 18:10' },
  { id: 5, no: 'ORD-20240715005', user: '孙七', items: '耳机 × 1', amount: '1,299.00', status: '已取消', statusClass: 'red', time: '2024-07-15 16:00' },
  { id: 6, no: 'ORD-20240714006', user: '周八', items: '键盘 × 1, 鼠标 × 1', amount: '689.00', status: '待发货', statusClass: 'yellow', time: '2024-07-14 10:15' },
  { id: 7, no: 'ORD-20240714007', user: '吴九', items: '显示器 × 1', amount: '2,199.00', status: '已发货', statusClass: 'blue', time: '2024-07-14 09:30' },
  { id: 8, no: 'ORD-20240713008', user: '郑十', items: '鼠标垫 × 3', amount: '89.70', status: '已完成', statusClass: 'green', time: '2024-07-13 20:00' },
  { id: 9, no: 'ORD-20240713009', user: '陈一', items: '手机壳 × 2', amount: '59.00', status: '待发货', statusClass: 'yellow', time: '2024-07-13 15:45' },
  { id: 10, no: 'ORD-20240712010', user: '林二', items: '充电宝 × 1', amount: '149.00', status: '已完成', statusClass: 'green', time: '2024-07-12 12:30' }
]

const filteredOrders = computed(() => {
  let list = [...orderList]
  if (filters.status) {
    list = list.filter(o => o.statusClass === filters.status)
  }
  if (filters.search) {
    const q = filters.search.toLowerCase()
    list = list.filter(o => o.no.toLowerCase().includes(q) || o.user.toLowerCase().includes(q))
  }
  return list
})

const totalPages = computed(() => Math.ceil(filteredOrders.value.length / pageSize) || 1)

function toggleSelectAll() {
  if (selectAll.value) {
    selectedIds.value = filteredOrders.value.map(o => o.id)
  } else {
    selectedIds.value = []
  }
}

function handleSearch() {
  currentPage.value = 1
}

function handleReset() {
  filters.status = ''
  filters.dateStart = ''
  filters.dateEnd = ''
  filters.search = ''
  currentPage.value = 1
}

function handleConfirmShip(order) {
  console.log('确认发货', order.no)
}

function handleLogistics(order) {
  console.log('查看物流', order.no)
}

function handleDetail(order) {
  console.log('查看详情', order.no)
}
</script>

<style scoped lang="css">
.order-list-page {
  max-width: 1200px;
}

.page-header {
  display: flex;
  align-items: center;
  gap: 24px;
  margin-bottom: 20px;
}

.page-title {
  font-size: 22px;
  font-weight: 700;
  color: #1d1d1f;
  margin: 0;
}

.status-summary {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 13px;
  color: #8e8e93;
}

.summary-item strong {
  color: #1d1d1f;
  margin-left: 4px;
}

.summary-item--warn {
  color: #ef4444;
}

.summary-divider {
  color: #e5e5ea;
}

.query-panel {
  background: #fff;
  border-radius: 10px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
}

.form-row {
  display: flex;
  gap: 16px;
  align-items: flex-end;
  flex-wrap: wrap;
}

.form-group {
  flex: 1;
  min-width: 160px;
}

.form-group--action {
  flex: 0 0 auto;
  display: flex;
  gap: 8px;
  align-items: flex-end;
}

.form-group label {
  display: block;
  font-size: 13px;
  font-weight: 500;
  color: #555;
  margin-bottom: 6px;
}

.form-input {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #e5e5ea;
  border-radius: 6px;
  font-size: 13px;
  color: #1d1d1f;
  background: #f9f9fb;
  box-sizing: border-box;
}

.form-input:focus {
  outline: none;
  border-color: #2563eb;
}

.table-wrapper {
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
  overflow: hidden;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th {
  text-align: left;
  font-size: 12px;
  font-weight: 600;
  color: #8e8e93;
  padding: 12px 14px;
  border-bottom: 1px solid #f0f0f0;
  background: #fafafa;
}

.data-table td {
  padding: 12px 14px;
  font-size: 13px;
  color: #333;
  border-bottom: 1px solid #f5f5f7;
}

.data-table tr:hover {
  background: #fafafa;
}

.order-no {
  font-family: monospace;
  font-size: 12px;
  color: #2563eb;
}

.order-time {
  font-size: 12px;
  color: #8e8e93;
  white-space: nowrap;
}

.action-cell {
  display: flex;
  gap: 6px;
  white-space: nowrap;
}

.btn-sm {
  padding: 4px 10px;
  font-size: 12px;
  border: 1px solid #e5e5ea;
  background: #fff;
  border-radius: 4px;
  cursor: pointer;
  color: #555;
  transition: all 0.2s;
}

.btn-sm:hover {
  border-color: #2563eb;
  color: #2563eb;
}

.empty-cell {
  text-align: center;
  padding: 40px 0;
  color: #aeaeb2;
  font-size: 14px;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  margin-top: 20px;
}

.page-info {
  font-size: 13px;
  color: #8e8e93;
}
</style>
