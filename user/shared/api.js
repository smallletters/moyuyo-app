/**
 * MOYUYO API Client
 * 对接 d:\app\接口文档.md V1.2.2
 * JWT Bearer + HMAC-SHA256 签名 + 自动刷新 Token
 *
 * 使用方式：
 *   import { api } from './shared/api.js';
 *   const user = await api.get('/api/v1/user/profile');
 *   const result = await api.post('/api/v1/user/register/email', { email, password });
 */

(function () {
  'use strict';

  /* ============================================================
   * 配置
   */
  const CONFIG = {
    BASE_URL: '',           // 同源留空；跨域填写完整域名
    TOKEN_KEY: 'moyuyo_access_token',
    REFRESH_KEY: 'moyuyo_refresh_token',
    DEVICE_ID_KEY: 'moyuyo_device_id',
    PLATFORM: 'h5',
    APP_VERSION: '1.0.0',
    TOKEN_REFRESH_URL: '/api/v1/user/token/refresh',
    LOGIN_URL: 'login.html',
  };

  /* ============================================================
   * 工具函数
   */

  /** 生成 UUID v4 */
  function uuid() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
      const r = (Math.random() * 16) | 0;
      return (c === 'x' ? r : (r & 0x3) | 0x8).toString(16);
    });
  }

  /** 获取或生成设备 ID */
  function getDeviceId() {
    let id = localStorage.getItem(CONFIG.DEVICE_ID_KEY);
    if (!id) {
      id = uuid();
      localStorage.setItem(CONFIG.DEVICE_ID_KEY, id);
    }
    return id;
  }

  /** 对参数按 key 排序并拼接 */
  function sortedParams(body) {
    if (!body || typeof body !== 'object') return '';
    const keys = Object.keys(body).sort();
    return keys.map(function (k) { return k + '=' + encodeURIComponent(body[k]); }).join('&');
  }

  /**
   * HMAC-SHA256 签名
   * 算法：HMAC-SHA256(secret, sortedParams + timestamp + nonce)
   * 注意：浏览器原生 Web Crypto API 实现
   */
  async function signRequest(body, secret) {
    if (!secret) return '';
    const timestamp = Date.now().toString();
    const nonce = uuid().replace(/-/g, '').substring(0, 16);
    const paramsStr = sortedParams(body);
    const payload = paramsStr + timestamp + nonce;

    const encoder = new TextEncoder();
    const keyData = encoder.encode(secret);
    const cryptoKey = await crypto.subtle.importKey(
      'raw', keyData, { name: 'HMAC', hash: 'SHA-256' }, false, ['sign']
    );
    const sig = await crypto.subtle.sign('HMAC', cryptoKey, encoder.encode(payload));
    const signHex = Array.from(new Uint8Array(sig))
      .map(function (b) { return b.toString(16).padStart(2, '0'); })
      .join('');

    return { sign: signHex, timestamp: timestamp, nonce: nonce };
  }

  /* ============================================================
   * Token 管理
   */

  function getToken() {
    return localStorage.getItem(CONFIG.TOKEN_KEY);
  }

  function setToken(accessToken, refreshToken) {
    if (accessToken) localStorage.setItem(CONFIG.TOKEN_KEY, accessToken);
    if (refreshToken) localStorage.setItem(CONFIG.REFRESH_KEY, refreshToken);
  }

  function clearToken() {
    localStorage.removeItem(CONFIG.TOKEN_KEY);
    localStorage.removeItem(CONFIG.REFRESH_KEY);
  }

  function getSignSecret() {
    return localStorage.getItem('moyuyo_sign_secret') || '';
  }

  function setSignSecret(secret) {
    if (secret) localStorage.setItem('moyuyo_sign_secret', secret);
  }

  /** 判断是否需要跳转登录 */
  function redirectToLogin() {
    clearToken();
    if (window.location.pathname.indexOf('login.html') === -1) {
      window.location.href = CONFIG.LOGIN_URL;
    }
  }

  /* ============================================================
   * 请求刷新 Token
   */
  let refreshPromise = null;

  async function refreshToken() {
    if (refreshPromise) return refreshPromise;
    const refreshTokenVal = localStorage.getItem(CONFIG.REFRESH_KEY);
    if (!refreshTokenVal) {
      redirectToLogin();
      throw new Error('No refresh token');
    }

    refreshPromise = fetch(CONFIG.BASE_URL + CONFIG.TOKEN_REFRESH_URL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json; charset=utf-8',
        'X-Platform': CONFIG.PLATFORM,
        'X-App-Version': CONFIG.APP_VERSION,
        'X-Device-Id': getDeviceId(),
        'X-Request-Id': uuid(),
      },
      body: JSON.stringify({ refreshToken: refreshTokenVal }),
    })
      .then(function (r) { return r.json(); })
      .then(function (res) {
        refreshPromise = null;
        if (res.code === 0 && res.data) {
          setToken(res.data.accessToken, res.data.refreshToken);
          if (res.data.signSecret) setSignSecret(res.data.signSecret);
          return res.data.accessToken;
        }
        redirectToLogin();
        throw new Error('Token refresh failed');
      })
      .catch(function (err) {
        refreshPromise = null;
        redirectToLogin();
        throw err;
      });

    return refreshPromise;
  }

  /* ============================================================
   * 核心请求
   */
  async function request(method, url, body, options) {
    options = options || {};
    const isFormData = body instanceof FormData;
    const headers = {
      'X-Platform': CONFIG.PLATFORM,
      'X-App-Version': CONFIG.APP_VERSION,
      'X-Device-Id': getDeviceId(),
      'X-Request-Id': uuid(),
      'X-Timestamp': Date.now().toString(),
    };

    if (!isFormData) {
      headers['Content-Type'] = 'application/json; charset=utf-8';
    }

    // JWT Token
    const token = getToken();
    if (token) {
      headers['Authorization'] = 'Bearer ' + token;
    }

    // HMAC-SHA256 签名（敏感接口）
    const secret = getSignSecret();
    if (secret && !options.skipSign) {
      const sig = await signRequest(isFormData ? null : body, secret);
      if (sig) {
        headers['X-Sign'] = sig.sign;
        headers['X-Timestamp'] = sig.timestamp;
        headers['X-Nonce'] = sig.nonce;
      }
    }

    const fetchOptions = {
      method: method,
      headers: headers,
    };

    if (body && method !== 'GET') {
      fetchOptions.body = isFormData ? body : JSON.stringify(body);
    }

    let response = await fetch(CONFIG.BASE_URL + url, fetchOptions);
    let data;

    try {
      data = await response.json();
    } catch (e) {
      throw new Error('Invalid JSON response');
    }

    // Token 失效 → 自动刷新并重试一次
    if (data.code === 1001 && getToken()) {
      try {
        const newToken = await refreshToken();
        headers['Authorization'] = 'Bearer ' + newToken;
        const retryResponse = await fetch(CONFIG.BASE_URL + url, fetchOptions);
        data = await retryResponse.json();
      } catch (e) {
        redirectToLogin();
        throw e;
      }
    }

    // 签名错误
    if (data.code === 1003) {
      console.warn('[MOYUYO API] 签名校验失败，请检查 signSecret');
    }

    return data;
  }

  /* ============================================================
   * 公开 API
   */
  window.moyuyoAPI = {
    /** 是否已登录 */
    get isLoggedIn() {
      return !!getToken();
    },

    /** 设置登录凭证（登录成功后调用） */
    setAuth: function (accessToken, refreshToken, signSecret) {
      setToken(accessToken, refreshToken);
      if (signSecret) setSignSecret(signSecret);
    },

    /** 登出 */
    logout: function () {
      clearToken();
      localStorage.removeItem('moyuyo_sign_secret');
    },

    /** GET 请求 */
    get: function (url, options) {
      return request('GET', url, null, options);
    },

    /** POST 请求 */
    post: function (url, body, options) {
      return request('POST', url, body, options);
    },

    /** PUT 请求 */
    put: function (url, body, options) {
      return request('PUT', url, body, options);
    },

    /** DELETE 请求 */
    delete: function (url, options) {
      return request('DELETE', url, null, options);
    },

    /** PATCH 请求 */
    patch: function (url, body, options) {
      return request('PATCH', url, body, options);
    },

    /** 上传文件 */
    upload: function (url, formData, options) {
      return request('POST', url, formData, options);
    },

    /** 获取设备 ID */
    getDeviceId: getDeviceId,

    /** 获取当前 Token（调试用） */
    getToken: getToken,
  };

})();