/**
 * MOYUYO Mock API Layer
 * 用于后端未就绪时的前端测试
 * 使用方法：在 api.js 之前引入 <script src="shared/mock-api.js"></script>
 */
(function () {
  'use strict';

  const MOCK_ENABLED = true;

  // Mock 数据库
  const mockDB = {
    users: [
      {
        userId: 10086,
        email: 'test@moyuyo.com',
        phone: '13800138000',
        nickname: '宠物达人小雅',
        avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=PetLover',
        memberLevel: 'GOLD',
        birthday: '1995-06-15',
        country: 'CN',
        locale: 'zh_CN'
      }
    ],
    wallets: {
      10086: { balance: 326.50, points: 1280, coupons: 3, giftCards: 1 }
    },
    orderCounts: {
      10086: { pendingPay: 2, pendingShip: 1, pendingReceive: 3, pendingReview: 1, total: 15 }
    },
    codes: {}, // 存储验证码
    tokens: {}
  };

  function generateToken() {
    return 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.' + btoa(Math.random().toString(36));
  }

  function success(data) {
    return { code: 0, message: 'ok', data: data, traceId: 'mock-' + Date.now(), serverTime: Date.now() };
  }

  function error(code, message) {
    return { code: code, message: message, data: null, traceId: 'mock-' + Date.now(), serverTime: Date.now() };
  }

  // Mock 处理器
  const mockHandlers = {
    'POST /api/v1/user/sms/send': function(body) {
      var phone = body.phone;
      var code = Math.floor(100000 + Math.random() * 900000).toString();
      mockDB.codes[phone] = { code: code, time: Date.now() };
      console.log('[MOCK] SMS code for', phone, ':', code);
      return success({ sent: true, expire: 300 });
    },

    'POST /api/v1/user/email/send': function(body) {
      var email = body.email;
      var code = Math.floor(100000 + Math.random() * 900000).toString();
      mockDB.codes[email] = { code: code, time: Date.now() };
      console.log('[MOCK] Email code for', email, ':', code);
      return success({ sent: true, expire: 300 });
    },

    'POST /api/v1/user/login/email': function(body) {
      var email = body.email;
      var password = body.password;

      var user = mockDB.users.find(function(u) { return u.email === email; });
      if (!user) {
        // 自动创建测试用户
        user = {
          userId: 10086,
          email: email,
          nickname: 'Test User',
          avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + email,
          memberLevel: 'NORMAL'
        };
      }

      var token = generateToken();
      mockDB.tokens[token] = { userId: user.userId, time: Date.now() };

      return success({
        userId: user.userId,
        accessToken: token,
        refreshToken: 'rt_' + Date.now(),
        expiresIn: 7200,
        require2fa: false,
        isNewUser: false,
        profile: {
          nickname: user.nickname,
          avatar: user.avatar,
          memberLevel: user.memberLevel
        }
      });
    },

    'POST /api/v1/user/login/sms': function(body) {
      var phone = body.phone;
      var code = body.code;
      var stored = mockDB.codes[phone];

      // 开发模式：任何验证码都通过（除了空）
      if (!code) {
        return error(2001, '请输入验证码');
      }

      var user = mockDB.users.find(function(u) { return u.phone === phone; });
      if (!user) {
        user = {
          userId: 10087,
          phone: phone,
          nickname: '手机用户',
          avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + phone,
          memberLevel: 'NORMAL'
        };
        mockDB.users.push(user);
      }

      var token = generateToken();
      mockDB.tokens[token] = { userId: user.userId, time: Date.now() };

      return success({
        userId: user.userId,
        accessToken: token,
        refreshToken: 'rt_' + Date.now(),
        expiresIn: 7200,
        require2fa: false,
        isNewUser: false,
        profile: {
          nickname: user.nickname,
          avatar: user.avatar,
          memberLevel: user.memberLevel
        }
      });
    },

    'POST /api/v1/user/register/email': function(body) {
      var email = body.email;
      var existing = mockDB.users.find(function(u) { return u.email === email; });
      if (existing) {
        return error(1101, '该邮箱已被注册');
      }

      var newUser = {
        userId: 10000 + mockDB.users.length + 1,
        email: email,
        nickname: body.nickname || email.split('@')[0],
        avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + email,
        memberLevel: 'NORMAL',
        birthday: body.birthday,
        country: body.country || 'US',
        locale: body.locale || 'en_US'
      };
      mockDB.users.push(newUser);

      return success({
        userId: newUser.userId,
        email: email,
        verifyEmailSent: true,
        verifyEmailExpire: 86400
      });
    },

    'POST /api/v1/user/register/phone': function(body) {
      var phone = body.phone;
      var existing = mockDB.users.find(function(u) { return u.phone === phone; });
      if (existing) {
        return error(1101, '该手机号已被注册');
      }

      var newUser = {
        userId: 10000 + mockDB.users.length + 1,
        phone: phone,
        nickname: body.nickname || '手机用户',
        avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + phone,
        memberLevel: 'NORMAL',
        country: body.country || 'CN',
        locale: body.locale || 'zh_CN'
      };
      mockDB.users.push(newUser);

      return success({
        userId: newUser.userId,
        phone: phone,
        verifyPhoneSent: true,
        verifyPhoneExpire: 86400
      });
    },

    'GET /api/v1/user/profile': function() {
      // 从 token 获取用户
      var token = localStorage.getItem('moyuyo_access_token');
      var tokenData = mockDB.tokens[token];
      if (!tokenData) {
        return error(1001, '未登录');
      }

      var user = mockDB.users.find(function(u) { return u.userId === tokenData.userId; });
      if (!user) {
        return error(2002, '用户不存在');
      }

      return success({
        userId: user.userId,
        email: user.email,
        phone: user.phone,
        nickname: user.nickname,
        avatar: user.avatar,
        birthday: user.birthday,
        memberLevel: user.memberLevel,
        country: user.country,
        locale: user.locale
      });
    },

    'GET /api/v1/user/wallet': function() {
      var token = localStorage.getItem('moyuyo_access_token');
      var tokenData = mockDB.tokens[token];
      var wallet = tokenData ? mockDB.wallets[tokenData.userId] : null;

      if (!wallet) {
        return success({ balance: 0, points: 0, coupons: 0, giftCards: 0 });
      }

      return success(wallet);
    },

    'GET /api/v1/order/count': function() {
      var token = localStorage.getItem('moyuyo_access_token');
      var tokenData = mockDB.tokens[token];
      var counts = tokenData ? mockDB.orderCounts[tokenData.userId] : null;

      if (!counts) {
        return success({ pendingPay: 0, pendingShip: 0, pendingReceive: 0, pendingReview: 0, total: 0 });
      }

      return success(counts);
    },

    'POST /api/v1/user/logout': function() {
      var token = localStorage.getItem('moyuyo_access_token');
      delete mockDB.tokens[token];
      return success({ loggedOut: true });
    },

    // ===== 商品模块 =====
    'GET /api/v1/home/feed': function() {
      return success({
        banners: [
          { id: 1, image: 'https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=750&q=80', link: '/product-detail.html?slug=shampoo-set', title: '春季护理节' },
          { id: 2, image: 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=750&q=80', link: '/product-detail.html?slug=pet-bed', title: '舒适睡眠' }
        ],
        kingKong: [
          { id: 1, name: '洗护', icon: 'https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=100&q=60', link: '/category.html?catId=1' },
          { id: 2, name: '食品', icon: 'https://images.unsplash.com/photo-1589924691195-41432c84c161?w=100&q=60', link: '/category.html?catId=2' },
          { id: 3, name: '玩具', icon: 'https://images.unsplash.com/photo-1576201836106-db1758fd1c97?w=100&q=60', link: '/category.html?catId=3' },
          { id: 4, name: '服饰', icon: 'https://images.unsplash.com/photo-1592194996308-7b43878e84a6?w=100&q=60', link: '/category.html?catId=4' },
          { id: 5, name: '牵引', icon: 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=100&q=60', link: '/category.html?catId=5' },
          { id: 6, name: '窝垫', icon: 'https://images.unsplash.com/photo-1541781774459-bb2af2f05b55?w=100&q=60', link: '/category.html?catId=6' },
          { id: 7, name: '清洁', icon: 'https://images.unsplash.com/photo-1516734212186-a967f81ad0d7?w=100&q=60', link: '/category.html?catId=7' },
          { id: 8, name: '保健', icon: 'https://images.unsplash.com/photo-1623366302587-b38b1ddaefd9?w=100&q=60', link: '/category.html?catId=8' }
        ],
        products: [
          { id: 101, slug: 'shampoo-set', name: '温和植物萃取宠物洗护套装', subtitle: '天然成分 温和不刺激', price: 12800, originalPrice: 19800, coverImage: 'https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=400&q=80', rating: 4.8, reviewCount: 2340 },
          { id: 102, slug: 'pet-harness', name: '城市探索者宠物牵引套装', subtitle: '防爆冲设计 舒适透气', price: 8900, originalPrice: 12900, coverImage: 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=400&q=80', rating: 4.9, reviewCount: 1856 },
          { id: 103, slug: 'cat-tree', name: '云端猫爬架', subtitle: '多层设计 磨爪休息一体', price: 29900, originalPrice: 39900, coverImage: 'https://images.unsplash.com/photo-1545249390-6bdfa286032f?w=400&q=80', rating: 4.7, reviewCount: 890 },
          { id: 104, slug: 'dog-food', name: '全价无谷犬粮', subtitle: '高蛋白 低敏配方', price: 16800, originalPrice: 22800, coverImage: 'https://images.unsplash.com/photo-1589924691195-41432c84c161?w=400&q=80', rating: 4.9, reviewCount: 3421 },
          { id: 105, slug: 'pet-toy', name: '智能互动球', subtitle: '自动滚动 陪伴玩耍', price: 6900, originalPrice: 9900, coverImage: 'https://images.unsplash.com/photo-1576201836106-db1758fd1c97?w=400&q=80', rating: 4.6, reviewCount: 567 },
          { id: 106, slug: 'pet-bed', name: '记忆棉宠物床垫', subtitle: ' orthopedic支撑 保护关节', price: 19900, originalPrice: 29900, coverImage: 'https://images.unsplash.com/photo-1541781774459-bb2af2f05b55?w=400&q=80', rating: 4.8, reviewCount: 1234 }
        ],
        brands: [
          { id: 1, name: 'MOYUYO', logo: 'https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=200&q=60' },
          { id: 2, name: 'PetCare', logo: 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=200&q=60' },
          { id: 3, name: 'FurryLove', logo: 'https://images.unsplash.com/photo-1576201836106-db1758fd1c97?w=200&q=60' }
        ]
      });
    },

    'GET /api/v1/goods/categories': function() {
      return success([
        {
          id: 1, name: '洗护美容', icon: 'sparkles', subcategories: [
            { id: 11, name: '洗发水' }, { id: 12, name: '护毛素' }, { id: 13, name: '梳子' }, { id: 14, name: '指甲剪' }
          ]
        },
        {
          id: 2, name: '宠物食品', icon: 'utensils', subcategories: [
            { id: 21, name: '犬粮' }, { id: 22, name: '猫粮' }, { id: 23, name: '零食' }, { id: 24, name: '保健品' }
          ]
        },
        {
          id: 3, name: '玩具娱乐', icon: 'gamepad-2', subcategories: [
            { id: 31, name: '球类' }, { id: 32, name: '绳结' }, { id: 33, name: '益智' }, { id: 34, name: '电动' }
          ]
        },
        {
          id: 4, name: '服饰配件', icon: 'shirt', subcategories: [
            { id: 41, name: '衣服' }, { id: 42, name: '项圈' }, { id: 43, name: '鞋子' }, { id: 44, name: '帽子' }
          ]
        },
        {
          id: 5, name: '出行牵引', icon: 'map-pin', subcategories: [
            { id: 51, name: '牵引绳' }, { id: 52, name: '胸背带' }, { id: 53, name: '项圈' }, { id: 54, name: '背包' }
          ]
        },
        {
          id: 6, name: '窝垫家具', icon: 'home', subcategories: [
            { id: 61, name: '床垫' }, { id: 62, name: '猫爬架' }, { id: 63, name: '狗屋' }, { id: 64, name: '垫子' }
          ]
        },
        {
          id: 7, name: '清洁用品', icon: 'droplets', subcategories: [
            { id: 71, name: '猫砂' }, { id: 72, name: '尿垫' }, { id: 73, name: '除臭' }, { id: 74, name: '湿巾' }
          ]
        },
        {
          id: 8, name: '医疗保健', icon: 'heart-pulse', subcategories: [
            { id: 81, name: '驱虫' }, { id: 82, name: '维生素' }, { id: 83, name: '疫苗' }, { id: 84, name: '急救' }
          ]
        }
      ]);
    },

    'GET /api/v1/goods/search/hot': function() {
      return success({
        list: [
          { keyword: '宠物驱虫', tag: '热' },
          { keyword: '夏季护理', tag: '新' },
          { keyword: '猫爬架', tag: '' },
          { keyword: '狗狗零食', tag: '热' },
          { keyword: '宠物床垫', tag: '' },
          { keyword: '宠物推车', tag: '新' },
          { keyword: '猫砂', tag: '' },
          { keyword: '狗狗衣服', tag: '' },
          { keyword: '宠物玩具', tag: '热' },
          { keyword: '猫粮', tag: '' }
        ]
      });
    },

    'GET /api/v1/goods/search/suggest': function() {
      return success({
        suggestions: ['宠物洗发水', '宠物沐浴露', '宠物护毛素', '宠物梳子', '宠物指甲剪']
      });
    },

    'GET /api/v1/goods/[^/?]+': function(body, url) {
      if (url.indexOf('/reviews') !== -1) return null;
      var slugMatch = url.match(/\/goods\/([^?]+)/);
      var slug = slugMatch ? slugMatch[1] : '';

      var products = {
        'shampoo-set': {
          id: 101, slug: 'shampoo-set', name: '温和植物萃取宠物洗护套装',
          brand: 'MOYUYO CARE', price: 12800, originalPrice: 19800, memberPrice: 10900,
          description: '采用天然植物萃取精华，温和清洁不刺激，适合敏感肌肤的宠物使用。',
          images: [
            { url: 'https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=600&q=80', alt: '产品图1' },
            { url: 'https://images.unsplash.com/photo-1516734212186-a967f81ad0d7?w=600&q=80', alt: '产品图2' },
            { url: 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=600&q=80', alt: '产品图3' }
          ],
          skus: [
            { id: 'sku-101-1', price: 12800, attributes: { '颜色': '香草白', '规格': '250ml' } },
            { id: 'sku-101-2', price: 19800, attributes: { '颜色': '香草白', '规格': '500ml' } },
            { id: 'sku-101-3', price: 32800, attributes: { '颜色': '清新绿', '规格': '500ml' } }
          ]
        },
        'pet-harness': {
          id: 102, slug: 'pet-harness', name: '城市探索者宠物牵引套装',
          brand: 'MOYUYO GO', price: 8900, originalPrice: 12900, memberPrice: 7900,
          description: '防爆冲设计，透气网布，反光条夜间安全。',
          images: [
            { url: 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=600&q=80', alt: '产品图1' },
            { url: 'https://images.unsplash.com/photo-1592194996308-7b43878e84a6?w=600&q=80', alt: '产品图2' }
          ],
          skus: [
            { id: 'sku-102-1', price: 8900, attributes: { '颜色': '石墨黑', '尺寸': 'S' } },
            { id: 'sku-102-2', price: 9900, attributes: { '颜色': '石墨黑', '尺寸': 'M' } },
            { id: 'sku-102-3', price: 10900, attributes: { '颜色': '焦糖棕', '尺寸': 'M' } }
          ]
        }
      };

      var product = products[slug] || products['shampoo-set'];
      return success(product);
    },

    'GET /api/v1/goods/[^/?]+/reviews': function(body, url) {
      return success({
        avgRating: '98%',
        total: 2340,
        tags: ['质量好', '物流快', '性价比高', '宠物喜欢'],
        list: [
          { userName: '小美妈妈', avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=1', rating: 5, content: '我家金毛用了这个洗发水后毛发特别柔顺，香味也很好闻，不刺鼻。', createTime: '2026-07-15T10:30:00Z' },
          { userName: '铲屎官小李', avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=2', rating: 5, content: '已经回购第三次了，猫咪不抗拒洗澡，泡沫丰富好冲洗。', createTime: '2026-07-12T14:20:00Z' },
          { userName: '宠物达人', avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=3', rating: 4, content: '整体不错，就是价格稍微贵了一点，等有活动再囤货。', createTime: '2026-07-10T09:15:00Z' }
        ]
      });
    },

    'POST /api/v1/cart/add': function(body) {
      var token = localStorage.getItem('moyuyo_access_token');
      if (!token || !mockDB.tokens[token]) {
        return error(1001, '未登录');
      }
      return success({ cartId: 'cart-' + Date.now(), skuId: body.skuId, quantity: body.quantity });
    },

    'POST /api/v1/favorites/toggle': function(body) {
      var token = localStorage.getItem('moyuyo_access_token');
      if (!token || !mockDB.tokens[token]) {
        return error(1001, '未登录');
      }
      return success({ isFavorite: true });
    }
  };

  // 拦截 fetch
  const originalFetch = window.fetch;

  window.fetch = function(url, options) {
    if (!MOCK_ENABLED) {
      return originalFetch(url, options);
    }

    // 只拦截 API 请求
    if (typeof url === 'string' && url.indexOf('/api/') !== -1) {
      var method = (options && options.method) || 'GET';

      // 查找匹配的 handler（支持正则表达式 key）
      var matchedHandler = null;
      var matchedKey = null;
      Object.keys(mockHandlers).forEach(function(key) {
        if (matchedHandler) return;
        var pattern = key;
        // 将正则语法转换为 RegExp
        if (pattern.indexOf('\\w+') !== -1 || pattern.indexOf('\\d+') !== -1 || pattern.indexOf('[^') !== -1) {
          var parts = pattern.split(' ');
          var methodPart = parts[0];
          var urlPattern = parts.slice(1).join(' ');
          // 转义特殊字符但保留正则语法
          var escapedPattern = urlPattern.replace(/\./g, '\\.').replace(/\//g, '\\/');
          var regex = new RegExp('^' + methodPart + ' ' + escapedPattern + '$');
          if (regex.test(method + ' ' + url)) {
            matchedHandler = mockHandlers[key];
            matchedKey = key;
          }
        } else {
          // 忽略 query string 进行匹配
          var urlWithoutQuery = url.split('?')[0];
          if (key === method + ' ' + url || key === method + ' ' + urlWithoutQuery) {
            matchedHandler = mockHandlers[key];
            matchedKey = key;
          }
        }
      });

      if (matchedHandler) {
        return new Promise(function(resolve) {
          setTimeout(function() {
            var body = null;
            if (options && options.body) {
              try {
                body = JSON.parse(options.body);
              } catch (e) {
                body = options.body;
              }
            }

            var result = matchedHandler(body, url);
            console.log('[MOCK]', matchedKey || (method + ' ' + url), '=>', result.code === 0 ? 'success' : 'error', result.code);

            resolve({
              ok: true,
              status: 200,
              json: function() { return Promise.resolve(result); }
            });
          }, 300 + Math.random() * 700); // 模拟网络延迟 300-1000ms
        });
      }
    }

    return originalFetch(url, options);
  };

  console.log('[MOCK API] Mock layer loaded. Enabled:', MOCK_ENABLED);
})();
