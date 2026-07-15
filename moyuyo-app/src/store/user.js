import { defineStore } from 'pinia'
import { userApi } from '@/api'
import { setStorage, getStorage, removeStorage, STORAGE_KEYS } from '@/utils/storage'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: getStorage(STORAGE_KEYS.TOKEN, ''),
    refreshToken: getStorage('moyuyo_refresh_token', ''),
    userInfo: getStorage(STORAGE_KEYS.USER_INFO, null),
    deviceList: getStorage(STORAGE_KEYS.DEVICE_LIST, []),
  }),

  getters: {
    isLoggedIn: (state) => !!state.token && !!state.userInfo,
    userId: (state) => state.userInfo?.id || null,
  },

  actions: {
    async login(credentials) {
      const result = await userApi.login(credentials.username, credentials.password)
      this.token = result.accessToken
      this.refreshToken = result.refreshToken
      setStorage(STORAGE_KEYS.TOKEN, result.accessToken)
      setStorage('moyuyo_refresh_token', result.refreshToken)
      await this.fetchProfile()
      if (this.userInfo?.twoFactorEnabled) {
        return { requiresTwoFactor: true }
      }
      return true
    },

    async register(userData) {
      const result = await userApi.register(userData)
      this.token = result.accessToken
      this.refreshToken = result.refreshToken
      setStorage(STORAGE_KEYS.TOKEN, result.accessToken)
      setStorage('moyuyo_refresh_token', result.refreshToken)
      await this.fetchProfile()
      return true
    },

    async fetchProfile() {
      try {
        const data = await userApi.getUserInfo()
        this.userInfo = {
          id: data.id,
          email: data.email,
          nickname: data.nickname || '',
          avatar: data.avatar || '',
          phone: data.phone || '',
          birthday: data.birthday || '',
          country: data.country || '',
          emailVerified: data.emailVerified || false,
          twoFactorEnabled: data.twoFactorEnabled || false,
        }
        setStorage(STORAGE_KEYS.USER_INFO, this.userInfo)
        return this.userInfo
      } catch (e) {
        console.error('[user] fetchProfile error', e)
        if (this.token) {
          const cached = getStorage(STORAGE_KEYS.USER_INFO)
          if (cached) this.userInfo = cached
        }
        return this.userInfo
      }
    },

    async updateProfile(data) {
      const updated = await userApi.updateUser(data)
      this.userInfo = { ...this.userInfo, ...updated }
      setStorage(STORAGE_KEYS.USER_INFO, this.userInfo)
      return updated
    },

    async refreshTokenAction() {
      if (!this.refreshToken) throw new Error('No refresh token')
      const result = await userApi.refreshToken(this.refreshToken)
      this.token = result.accessToken
      this.refreshToken = result.refreshToken
      setStorage(STORAGE_KEYS.TOKEN, result.accessToken)
      setStorage('moyuyo_refresh_token', result.refreshToken)
    },

    async logout() {
      try { await userApi.logout() } catch (e) { /* ignore */ }
      this.token = ''
      this.refreshToken = ''
      this.userInfo = null
      this.deviceList = []
      removeStorage(STORAGE_KEYS.TOKEN)
      removeStorage(STORAGE_KEYS.USER_INFO)
      removeStorage('moyuyo_refresh_token')
    },

    forceLogout() {
      this.token = ''
      this.refreshToken = ''
      this.userInfo = null
      removeStorage(STORAGE_KEYS.TOKEN)
      removeStorage(STORAGE_KEYS.USER_INFO)
      removeStorage('moyuyo_refresh_token')
    },

    async sendEmailVerification(email) {
      await userApi.sendEmailVerification(email)
    },

    async confirmEmailVerification(email, code) {
      await userApi.confirmEmailVerification(email, code)
      if (this.userInfo) {
        this.userInfo.emailVerified = true
        setStorage(STORAGE_KEYS.USER_INFO, this.userInfo)
      }
    },

    async forgotPassword(email) {
      await userApi.forgotPassword(email)
    },

    async resetPassword(token, newPassword) {
      await userApi.resetPassword(token, newPassword)
    },

    async changePassword(oldPassword, newPassword) {
      await userApi.changePassword(oldPassword, newPassword)
    },

    async sendMagicLink(email) {
      await userApi.sendMagicLink(email)
    },

    async verifyMagicLink(token) {
      const result = await userApi.verifyMagicLink(token)
      this.token = result.accessToken
      this.refreshToken = result.refreshToken
      setStorage(STORAGE_KEYS.TOKEN, result.accessToken)
      setStorage('moyuyo_refresh_token', result.refreshToken)
      await this.fetchProfile()
      return true
    },

    async toggle2FA(enabled) {
      if (this.userInfo) {
        this.userInfo.twoFactorEnabled = enabled
        setStorage(STORAGE_KEYS.USER_INFO, this.userInfo)
      }
    },

    async sendTwoFactorCode() {
      await userApi.sendTwoFactorCode()
    },

    async verifyTwoFactorCode(code) {
      await userApi.verifyTwoFactorCode(code)
    },

    async fetchDevices() {
      return this.deviceList
    },

    async trustDevice(deviceId) {
      const dev = this.deviceList.find(d => d.id === deviceId)
      if (dev) dev.trusted = true
      setStorage(STORAGE_KEYS.DEVICE_LIST, this.deviceList)
    },

    async untrustDevice(deviceId) {
      const dev = this.deviceList.find(d => d.id === deviceId)
      if (dev) dev.trusted = false
      setStorage(STORAGE_KEYS.DEVICE_LIST, this.deviceList)
    },

    async removeDevice(deviceId) {
      this.deviceList = this.deviceList.filter(d => d.id !== deviceId)
      setStorage(STORAGE_KEYS.DEVICE_LIST, this.deviceList)
    },
  },
})
