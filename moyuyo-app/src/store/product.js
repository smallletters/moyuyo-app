import { defineStore } from 'pinia'
import { productApi } from '@/api'

export const useProductStore = defineStore('product', {
  state: () => ({
    categoryTree: [],
    homeRecommend: [],
    searchHistory: [],
  }),

  actions: {
    async loadCategoryTree(force = false) {
      if (!force && this.categoryTree.length > 0) return this.categoryTree
      const list = await productApi.getCategoryList()
      this.categoryTree = list
      return list
    },

    async loadHomeData() {
      const recommend = await productApi.getProductList({
        page: 1,
        size: 10,
        sortBy: 'sales',
        sortOrder: 'desc',
      })
      this.homeRecommend = recommend?.records || recommend || []
      return { recommend: this.homeRecommend }
    },

    addSearchHistory(keyword) {
      if (!keyword) return
      const list = this.searchHistory.filter((k) => k !== keyword)
      list.unshift(keyword)
      this.searchHistory = list.slice(0, 10)
    },

    clearSearchHistory() {
      this.searchHistory = []
    },
  },
})
