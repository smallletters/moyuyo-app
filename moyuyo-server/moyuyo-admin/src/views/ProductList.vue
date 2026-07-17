<template>
  <div class="product-list-page">
    <div class="page-header">
      <h2 class="page-title">商品列表</h2>
      <button class="btn btn-primary" @click="handleAddProduct">+ 新增商品</button>
    </div>

    <!-- 筛选面板 -->
    <div class="query-panel">
      <div class="form-row">
        <div class="form-group">
          <label>搜索</label>
          <input v-model="filters.search" type="text" class="form-input" placeholder="商品名称/SKU" />
        </div>
        <div class="form-group">
          <label>商品状态</label>
          <select v-model="filters.status" class="form-input">
            <option value="">全部状态</option>
            <option value="active">在售</option>
            <option value="inactive">已下架</option>
            <option value="draft">草稿</option>
            <option value="pending">待审核</option>
          </select>
        </div>
        <div class="form-group">
          <label>商品分类</label>
          <select v-model="filters.category" class="form-input">
            <option value="">全部分类</option>
            <option value="clothing">服装</option>
            <option value="electronics">电子产品</option>
            <option value="accessories">配件</option>
            <option value="home">家居</option>
          </select>
        </div>
        <div class="form-group">
          <label>库存状态</label>
          <select v-model="filters.stock" class="form-input">
            <option value="">全部库存</option>
            <option value="low">库存紧张</option>
            <option value="normal">库存正常</option>
            <option value="out">缺货</option>
          </select>
        </div>
        <div class="form-group form-group--action">
          <label>&nbsp;</label>
          <button class="btn btn-primary" @click="handleSearch">搜索</button>
          <button class="btn" @click="handleReset">重置</button>
        </div>
      </div>
    </div>

    <!-- Tab 标签 -->
    <div class="tab-bar">
      <button
        v-for="tab in tabs"
        :key="tab.key"
        class="tab-item"
        :class="{ 'tab-item--active': activeTab === tab.key }"
        @click="activeTab = tab.key"
      >
        {{ tab.label }}
        <span class="tab-count">{{ tab.count }}</span>
      </button>
    </div>

    <!-- 批量操作栏 -->
    <div class="batch-bar" v-if="selectedIds.length > 0">
      <span class="batch-info">已选择 {{ selectedIds.length }} 项</span>
      <button class="btn btn-sm" @click="handleBatchAction('shelf')">上架</button>
      <button class="btn btn-sm" @click="handleBatchAction('unshelf')">下架</button>
      <button class="btn btn-sm btn-danger" @click="handleBatchAction('delete')">删除</button>
    </div>

    <!-- 数据表格 -->
    <div class="table-wrapper">
      <table class="data-table">
        <thead>
          <tr>
            <th><input type="checkbox" v-model="selectAll" @change="toggleSelectAll" /></th>
            <th>商品</th>
            <th>SKU</th>
            <th>价格</th>
            <th>库存</th>
            <th>销量</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="product in filteredProducts" :key="product.id">
            <td><input type="checkbox" v-model="selectedIds" :value="product.id" /></td>
            <td class="product-cell">
              <div class="product-thumb" :style="{ backgroundColor: product.color }"></div>
              <span class="product-name">{{ product.name }}</span>
            </td>
            <td class="sku-cell">{{ product.sku }}</td>
            <td>¥{{ product.price }}</td>
            <td>{{ product.stock }}</td>
            <td>{{ product.sales }}</td>
            <td><span :class="'tag-' + product.statusClass">{{ product.status }}</span></td>
            <td class="action-cell">
              <button class="btn btn-sm" @click="handleEdit(product)">编辑</button>
              <button class="btn btn-sm" @click="handleToggleStatus(product)">
                {{ product.statusClass === 'green' ? '下架' : '上架' }}
              </button>
            </td>
          </tr>
          <tr v-if="filteredProducts.length === 0">
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

const activeTab = ref('all')
const selectAll = ref(false)
const selectedIds = ref([])
const currentPage = ref(1)
const pageSize = 10

const tabs = [
  { key: 'all', label: '全部', count: 28 },
  { key: 'active', label: '在售', count: 18 },
  { key: 'inactive', label: '已下架', count: 5 },
  { key: 'draft', label: '草稿', count: 3 },
  { key: 'pending', label: '待审核', count: 2 }
]

const filters = reactive({
  search: '',
  status: '',
  category: '',
  stock: ''
})

const productList = [
  { id: 1, name: '纯棉圆领T恤', sku: 'TS-001', price: '89.00', stock: 256, sales: 1280, status: '在售', statusClass: 'green', color: '#3b82f6' },
  { id: 2, name: '无线蓝牙耳机', sku: 'BT-002', price: '299.00', stock: 48, sales: 856, status: '在售', statusClass: 'green', color: '#1d1d1f' },
  { id: 3, name: '便携式充电宝', sku: 'PB-003', price: '149.00', stock: 0, sales: 623, status: '缺货', statusClass: 'red', color: '#f59e0b' },
  { id: 4, name: '机械键盘 87键', sku: 'KB-004', price: '399.00', stock: 12, sales: 445, status: '库存紧张', statusClass: 'yellow', color: '#6366f1' },
  { id: 5, name: '鼠标垫 大号', sku: 'MP-005', price: '29.90', stock: 520, sales: 2100, status: '在售', statusClass: 'green', color: '#10b981' },
  { id: 6, name: '简约双肩背包', sku: 'BP-006', price: '259.00', stock: 3, sales: 334, status: '待审核', statusClass: 'yellow', color: '#8b5cf6' },
  { id: 7, name: '智能手环', sku: 'WB-007', price: '599.00', stock: 0, sales: 189, status: '已下架', statusClass: 'gray', color: '#ef4444' },
  { id: 8, name: '不锈钢保温杯', sku: 'BC-008', price: '79.00', stock: 180, sales: 756, status: '在售', statusClass: 'green', color: '#06b6d4' },
  { id: 9, name: 'USB-C 扩展坞', sku: 'DH-009', price: '189.00', stock: 65, sales: 410, status: '草稿', statusClass: 'gray', color: '#1d1d1f' },
  { id: 10, name: '运动速干短裤', sku: 'ST-010', price: '129.00', stock: 92, sales: 567, status: '在售', statusClass: 'green', color: '#22c55e' }
]

const filteredProducts = computed(() => {
  let list = [...productList]
  // tab 过滤
  if (activeTab.value === 'active') {
    list = list.filter(p => p.statusClass === 'green')
  } else if (activeTab.value === 'inactive') {
    list = list.filter(p => p.status === '已下架')
  } else if (activeTab.value === 'draft') {
    list = list.filter(p => p.status === '草稿')
  } else if (activeTab.value === 'pending') {
    list = list.filter(p => p.status === '待审核')
  }
  if (filters.search) {
    const q = filters.search.toLowerCase()
    list = list.filter(p => p.name.toLowerCase().includes(q) || p.sku.toLowerCase().includes(q))
  }
  return list
})

const totalPages = computed(() => Math.ceil(filteredProducts.value.length / pageSize) || 1)

function toggleSelectAll() {
  if (selectAll.value) {
    selectedIds.value = filteredProducts.value.map(p => p.id)
  } else {
    selectedIds.value = []
  }
}

function handleSearch() {
  currentPage.value = 1
}

function handleReset() {
  filters.search = ''
  filters.status = ''
  filters.category = ''
  filters.stock = ''
  currentPage.value = 1
}

function handleAddProduct() {
  console.log('新增商品')
}

function handleEdit(product) {
  console.log('编辑商品', product.name)
}

function handleToggleStatus(product) {
  console.log('切换状态', product.name)
}

function handleBatchAction(action) {
  console.log('批量操作', action, selectedIds.value)
}
</script>

<style scoped lang="css">
.product-list-page {
  max-width: 1200px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title {
  font-size: 22px;
  font-weight: 700;
  color: #1d1d1f;
  margin: 0;
}

.query-panel {
  background: #fff;
  border-radius: 10px;
  padding: 20px;
  margin-bottom: 16px;
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

/* Tab 栏 */
.tab-bar {
  display: flex;
  gap: 0;
  background: #fff;
  border-radius: 10px 10px 0 0;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
}

.tab-item {
  flex: 1;
  padding: 12px 16px;
  border: none;
  background: #fff;
  font-size: 13px;
  color: #8e8e93;
  cursor: pointer;
  transition: all 0.2s;
  border-bottom: 2px solid transparent;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

.tab-item:hover {
  color: #333;
}

.tab-item--active {
  color: #2563eb;
  border-bottom-color: #2563eb;
  font-weight: 600;
}

.tab-count {
  font-size: 11px;
  background: #f0f0f0;
  padding: 1px 7px;
  border-radius: 10px;
  color: #8e8e93;
}

.tab-item--active .tab-count {
  background: #eff6ff;
  color: #2563eb;
}

/* 批量操作栏 */
.batch-bar {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 16px;
  background: #f0f7ff;
  border-bottom: 1px solid #dbeafe;
}

.batch-info {
  font-size: 13px;
  color: #2563eb;
  margin-right: 8px;
}

.btn-danger {
  color: #ef4444;
  border-color: #fca5a5;
}

.btn-danger:hover {
  background: #fef2f2;
  border-color: #ef4444;
}

/* 表格 */
.table-wrapper {
  background: #fff;
  border-radius: 0 0 10px 10px;
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

.product-cell {
  display: flex;
  align-items: center;
  gap: 10px;
}

.product-thumb {
  width: 40px;
  height: 40px;
  border-radius: 6px;
  flex-shrink: 0;
}

.product-name {
  font-weight: 500;
  color: #1d1d1f;
}

.sku-cell {
  font-family: monospace;
  font-size: 12px;
  color: #8e8e93;
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
