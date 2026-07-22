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
      // 确保购物车数据存在
      var userId = mockDB.tokens[token].userId;
      if (!mockDB.carts) mockDB.carts = {};
      if (!mockDB.carts[userId]) mockDB.carts[userId] = [];
      mockDB.carts[userId].push({
        id: 'cart-' + mockDB.carts[userId].length, skuId: body.skuId, quantity: body.quantity || 1,
        name: '宠物洗护套装', price: 12800, image: 'https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=200&q=60', spec: '香草白 / 500ml'
      });
      return success({ cartId: 'cart-' + mockDB.carts[userId].length, skuId: body.skuId, quantity: body.quantity });
    },

    'GET /api/v1/cart/list': function() {
      var token = localStorage.getItem('moyuyo_access_token');
      if (!token || !mockDB.tokens[token]) {
        return error(1001, '未登录');
      }
      var userId = mockDB.tokens[token].userId;
      if (!mockDB.carts) mockDB.carts = {};
      if (!mockDB.carts[userId]) {
        mockDB.carts[userId] = [
          { id: 'cart-1', skuId: 'sku-101-1', quantity: 1, name: '温和植物萃取宠物洗护套装', price: 12800, image: 'https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=200&q=60', spec: '香草白 / 250ml' },
          { id: 'cart-2', skuId: 'sku-102-2', quantity: 1, name: '城市探索者宠物牵引套装', price: 9900, image: 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=200&q=60', spec: '石墨黑 / M' },
          { id: 'cart-3', skuId: 'sku-103-1', quantity: 2, name: '云端猫爬架', price: 29900, image: 'https://images.unsplash.com/photo-1545249390-6bdfa286032f?w=200&q=60', spec: '标准款' }
        ];
      }
      return success({
        items: mockDB.carts[userId],
        total: mockDB.carts[userId].reduce(function(sum, item) { return sum + item.price * item.quantity; }, 0),
        count: mockDB.carts[userId].length
      });
    },

    'GET /api/v1/cart/preview': function() {
      var token = localStorage.getItem('moyuyo_access_token');
      if (!token || !mockDB.tokens[token]) {
        return error(1001, '未登录');
      }
      var userId = mockDB.tokens[token].userId;
      if (!mockDB.carts) mockDB.carts = {};
      if (!mockDB.carts[userId]) {
        mockDB.carts[userId] = [
          { id: 'cart-1', skuId: 'sku-101-1', quantity: 1, name: '温和植物萃取宠物洗护套装', price: 12800, image: 'https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=200&q=60', spec: '香草白 / 250ml' },
          { id: 'cart-2', skuId: 'sku-102-2', quantity: 1, name: '城市探索者宠物牵引套装', price: 9900, image: 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=200&q=60', spec: '石墨黑 / M' }
        ];
      }
      return success({
        items: mockDB.carts[userId],
        subtotal: mockDB.carts[userId].reduce(function(sum, item) { return sum + item.price * item.quantity; }, 0),
        shippingFee: 0,
        discount: 2000,
        total: mockDB.carts[userId].reduce(function(sum, item) { return sum + item.price * item.quantity; }, 0) - 2000,
        addressId: 'addr-1',
        address: { name: '小雅', phone: '138****6789', full: '上海市浦东新区陆家嘴环路1088号' }
      });
    },

    'POST /api/v1/order': function(body) {
      return success({
        orderNo: 'MOY' + Date.now(),
        orderId: 'order-' + Date.now(),
        totalAmount: 22700,
        status: 'PAID',
        payUrl: 'https://pay.example.com/order/MOY' + Date.now()
      });
    },

    'GET /api/v1/order/list': function() {
      return success({
        orders: [
          { id: 'order-1', orderNo: 'MOY20260715001', status: 'COMPLETED', totalAmount: 12800, createdAt: '2026-07-15T10:30:00Z', items: [{ name: '温和植物萃取宠物洗护套装', image: 'https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=200&q=60', quantity: 1, price: 12800 }] },
          { id: 'order-2', orderNo: 'MOY20260718002', status: 'SHIPPED', totalAmount: 9900, createdAt: '2026-07-18T14:20:00Z', items: [{ name: '城市探索者宠物牵引套装', image: 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=200&q=60', quantity: 1, price: 9900 }] },
          { id: 'order-3', orderNo: 'MOY20260720003', status: 'PENDING', totalAmount: 59800, createdAt: '2026-07-20T09:15:00Z', items: [{ name: '云端猫爬架', image: 'https://images.unsplash.com/photo-1545249390-6bdfa286032f?w=200&q=60', quantity: 2, price: 29900 }] },
          { id: 'order-4', orderNo: 'MOY20260721004', status: 'PAID', totalAmount: 16800, createdAt: '2026-07-21T16:45:00Z', items: [{ name: '全价无谷犬粮', image: 'https://images.unsplash.com/photo-1589924691195-41432c84c161?w=200&q=60', quantity: 1, price: 16800 }] }
        ],
        total: 4,
        page: 1,
        pageSize: 20
      });
    },

    'GET /api/v1/order/detail': function() {
      return success({
        orderNo: 'MOY20260710002',
        status: 'SHIPPED',
        totalAmount: 8900,
        shippingFee: 0,
        discount: 2000,
        paidAmount: 6900,
        paymentMethod: '微信支付',
        createdAt: '2026-07-10T15:22:00Z',
        etd: '2026年7月12日',
        address: { name: '小雅', phone: '138****8888', full: '上海市浦东新区张江高科技园区碧波路690号' },
        items: [
          { name: '经典尼龙宠物牵引套装', image: 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=200&q=60', quantity: 1, price: 8900, spec: '海军蓝 / M码' }
        ],
        logistics: [
          { time: '2026-07-11T14:30:00Z', status: '已到达', desc: '包裹已到达上海浦东配送站' },
          { time: '2026-07-10T22:15:00Z', status: '运输中', desc: '包裹从杭州分拨中心发出' },
          { time: '2026-07-10T08:00:00Z', status: '已发货', desc: '卖家已发货' },
          { time: '2026-07-09T16:30:00Z', status: '已支付', desc: '订单已支付' }
        ]
      });
    },

    // ===== 宠物档案模块 =====

    'GET /api/v1/pet/list': function() {
      return success([
        {
          id: 'pet-1', name: '旺财', breed: '金毛寻回犬', gender: 'MALE', age: '3岁6个月',
          weight: 28.5, birthday: '2023-01-15', avatar: '../assets/pet-avatar-dog.jpg',
          cover: '../assets/pet-profile-dog.jpg', isDefault: true
        },
        {
          id: 'pet-2', name: '团子', breed: '金毛寻回犬', gender: 'MALE', age: '2岁3个月',
          weight: 28.5, birthday: '2024-04-10', avatar: '../assets/pet-avatar-dog.jpg',
          cover: '../assets/pet-profile-dog.jpg', isDefault: false
        },
        {
          id: 'pet-3', name: '豆豆', breed: '英国短毛猫', gender: 'FEMALE', age: '1岁2个月',
          weight: 4.2, birthday: '2025-05-20', avatar: '../assets/pet-avatar-dog.jpg',
          cover: '../assets/pet-profile-dog.jpg', isDefault: false
        }
      ]);
    },

    'GET /api/v1/pet/[^/?]+$': function(body, url) {
      var petId = url.match(/\/pet\/([^/?]+)/)[1];
      var pets = {
        'pet-1': {
          id: 'pet-1', name: '旺财', breed: '金毛寻回犬', gender: 'MALE', age: '3岁6个月',
          weight: 28.5, height: 58, birthday: '2023-01-15', color: '浅金色',
          avatar: '../assets/pet-avatar-dog.jpg', cover: '../assets/pet-profile-dog.jpg',
          health: {
            weight: { value: 28.5, unit: 'kg', status: 'NORMAL', label: '正常' },
            temperature: { value: 38.5, unit: '°C', status: 'NORMAL', label: '正常' },
            heartRate: { value: null, unit: 'bpm', status: 'NONE', label: '暂无数据' },
            foodIntake: { value: null, unit: 'g', status: 'NORMAL', label: '正常' }
          },
          careTimeline: [
            { type: 'BATH', title: '洗澡', date: '2026-07-15', desc: '使用MOYUYO温和洗护套装', icon: 'droplets', color: 'brand' },
            { type: 'DEWORM', title: '驱虫', date: '2026-07-10', desc: '体内+体外驱虫', icon: 'shield', color: 'success' },
            { type: 'VACCINE', title: '疫苗', date: '2026-06-28', desc: '狂犬疫苗加强针', icon: 'check', color: 'brand' },
            { type: 'CHECKUP', title: '体检', date: '2026-06-15', desc: '年度体检，一切正常', icon: 'heart', color: 'error' }
          ]
        },
        'pet-2': {
          id: 'pet-2', name: '团子', breed: '金毛寻回犬', gender: 'MALE', age: '2岁3个月',
          weight: 28.5, height: 55, birthday: '2024-04-10', color: '深金色',
          avatar: '../assets/pet-avatar-dog.jpg', cover: '../assets/pet-profile-dog.jpg',
          health: {
            weight: { value: 28.5, unit: 'kg', status: 'NORMAL', label: '正常' },
            temperature: { value: 38.2, unit: '°C', status: 'NORMAL', label: '正常' },
            heartRate: { value: 95, unit: 'bpm', status: 'NORMAL', label: '正常' },
            foodIntake: { value: 350, unit: 'g', status: 'NORMAL', label: '正常' }
          },
          careTimeline: [
            { type: 'DEWORM', title: '驱虫', date: '2026-07-05', desc: '体内驱虫，服用拜宠清', icon: 'shield', color: 'success' },
            { type: 'CHECKUP', title: '体检', date: '2026-06-28', desc: '半年度体检', icon: 'heart', color: 'error' },
            { type: 'VACCINE', title: '疫苗', date: '2026-06-10', desc: '年度疫苗', icon: 'check', color: 'brand' }
          ]
        },
        'pet-3': {
          id: 'pet-3', name: '豆豆', breed: '英国短毛猫', gender: 'FEMALE', age: '1岁2个月',
          weight: 4.2, height: 25, birthday: '2025-05-20', color: '蓝灰色',
          avatar: '../assets/pet-avatar-dog.jpg', cover: '../assets/pet-profile-dog.jpg',
          health: {
            weight: { value: 4.2, unit: 'kg', status: 'NORMAL', label: '正常' },
            temperature: { value: 38.8, unit: '°C', status: 'NORMAL', label: '正常' },
            heartRate: { value: null, unit: 'bpm', status: 'NONE', label: '暂无数据' },
            foodIntake: { value: null, unit: 'g', status: 'NORMAL', label: '正常' }
          },
          careTimeline: [
            { type: 'BATH', title: '洗澡', date: '2026-07-12', desc: '温和沐浴露洗护', icon: 'droplets', color: 'brand' },
            { type: 'DEWORM', title: '驱虫', date: '2026-07-01', desc: '体外驱虫', icon: 'shield', color: 'success' }
          ]
        }
      };
      return success(pets[petId] || pets['pet-1']);
    },

    'GET /api/v1/pet/[^/?]+/diary': function(body, url) {
      return success({
        pages: [
          {
            month: '2026年7月', count: 6,
            entries: [
              { id: 'diary-1', date: '2026-07-08', weekday: '周二', content: '今天带团子去公园散步，遇到了好久不见的柯基朋友，两个小家伙在草地上追了半小时，回家路上团子直接趴在副驾驶睡着了，太可爱了。', tags: ['日常', '户外'], likes: 12, comments: 3, photos: 3 },
              { id: 'diary-2', date: '2026-07-05', weekday: '周六', content: '体检日！团子非常乖，打针的时候只哼了一声。医生说状态很好，体重控制得也不错，继续保持现在的饮食和运动量。', tags: ['体检', '体重', '疫苗'], likes: 24, comments: 8, photos: 2, weight: { value: 28.5, unit: 'kg', change: -0.3, trend: 'down' } },
              { id: 'diary-3', date: '2026-07-02', weekday: '周三', content: '新买的逗猫棒到了（虽然团子是狗），但它意外地很喜欢这个玩具，追着跑了半个晚上，看来运动量是够了。', tags: ['日常', '新玩具'], likes: 9, comments: 1, photos: 2 }
            ]
          },
          {
            month: '2026年6月', count: 4,
            entries: [
              { id: 'diary-4', date: '2026-06-28', weekday: '周六', content: '这周食欲特别好，可能是换新狗粮的原因。不过要注意控制零食量了，不然体重增长太快。', tags: ['日常', '体重'], likes: 6, comments: 2, photos: 0, weight: { value: 28.8, unit: 'kg', change: 0.5, trend: 'up' } },
              { id: 'diary-5', date: '2026-06-15', weekday: '周日', content: '夏天到了给团子剃了个清爽的造型，看起来像个小狮子，走在路上回头率超高。自己也觉得凉快了不少。', tags: ['造型', '护理'], likes: 18, comments: 5, photos: 1 },
              { id: 'diary-6', date: '2026-06-03', weekday: '周二', content: '驱虫药按时吃了，团子吃完之后有点犯困，大概半小时就好了。记录一下下次驱虫时间是8月3号。', tags: ['日常', '驱虫'], likes: 4, comments: 0, photos: 0 }
            ]
          }
        ]
      });
    },

    'GET /api/v1/pet/[^/?]+/weight': function(body, url) {
      return success({
        unit: 'kg',
        records: [
          { date: '2026-01-15', value: 27.5 },
          { date: '2026-02-15', value: 27.8 },
          { date: '2026-03-15', value: 28.0 },
          { date: '2026-04-15', value: 28.2 },
          { date: '2026-05-15', value: 28.8 },
          { date: '2026-06-15', value: 28.8 },
          { date: '2026-07-05', value: 28.5 }
        ],
        stats: {
          current: 28.5, min: 27.5, max: 28.8, avg: 28.23,
          change: 0.3, trend: 'up', status: 'NORMAL', label: '正常范围'
        }
      });
    },

    'GET /api/v1/pet/[^/?]+/health/calendar': function(body, url) {
      return success({
        year: 2026, month: 7,
        events: {
          '3': [{ type: 'BATH', label: '洗澡' }],
          '7': [{ type: 'DIARY', label: '日记' }],
          '9': [{ type: 'DEWORM', label: '驱虫' }],
          '12': [{ type: 'VACCINE', label: '疫苗' }, { type: 'BATH', label: '洗澡' }],
          '14': [{ type: 'CHECKUP', label: '体检' }],
          '17': [{ type: 'BATH', label: '洗澡' }],
          '20': [{ type: 'DIARY', label: '日记' }],
          '25': [{ type: 'VACCINE', label: '疫苗' }],
          '29': [{ type: 'BATH', label: '洗澡' }]
        }
      });
    },

    'GET /api/v1/pet/[^/?]+/health/reminders': function(body, url) {
      return success({
        list: [
          { type: 'BATH', title: '洗澡', dueDate: '2026-07-06', remaining: -3, status: 'OVERDUE', desc: '预计日期：7月6日' },
          { type: 'VACCINE', title: '疫苗', dueDate: '2026-07-15', remaining: 6, status: 'UPCOMING', desc: '狂犬疫苗加强' },
          { type: 'DEWORM', title: '驱虫', dueDate: '2026-07-27', remaining: 18, status: 'NORMAL', desc: '体外驱虫' },
          { type: 'CHECKUP', title: '体检', dueDate: '2026-08-03', remaining: 25, status: 'NORMAL', desc: '半年度体检' }
        ]
      });
    },

    'GET /api/v1/pet/[^/?]+/health/records': function(body, url) {
      return success({
        date: '2026-07-09',
        petName: '豆豆',
        records: [
          { type: 'DEWORM', title: '驱虫', time: '10:30', desc: '体内驱虫，服用拜宠清 1 片', icon: 'shield' },
          { type: 'WEIGHT', title: '体重', time: '09:15', desc: '4.2 kg（较上月 -0.1 kg）', icon: 'arrow-down' }
        ]
      });
    },

    'GET /api/v1/pet/[^/?]+/health/stats': function(body, url) {
      return success({
        month: '2026年7月',
        stats: {
          BATH: { count: 3, label: '洗澡' },
          VACCINE: { count: 1, label: '疫苗' },
          DEWORM: { count: 1, label: '驱虫' },
          CHECKUP: { count: 0, label: '体检' },
          DIARY: { count: 2, label: '日记' }
        }
      });
    },

    // ===== 收藏模块 =====

    'GET /api/v1/favorites/list': function() {
      return success({
        items: [
          { id: 'fav-1', goods: { id: 101, slug: 'shampoo-set', name: '温和植物萃取宠物洗护套装', price: 12800, coverImage: 'https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=400&q=80' }, priceDrop: false },
          { id: 'fav-2', goods: { id: 102, slug: 'pet-harness', name: '经典尼龙宠物牵引套装', price: 8900, coverImage: 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=400&q=80' }, priceDrop: false },
          { id: 'fav-3', goods: { id: 105, slug: 'pet-toy', name: '环保毛绒互动玩具球', price: 4500, coverImage: 'https://images.unsplash.com/photo-1576201836106-db1758fd1c97?w=400&q=80' }, priceDrop: false },
          { id: 'fav-4', goods: { id: 201, slug: 'pet-jacket', name: '设计师宠物机能风衣', price: 16800, originalPrice: 22800, coverImage: 'https://images.unsplash.com/photo-1592194996308-7b43878e84a6?w=400&q=80' }, priceDrop: true },
          { id: 'fav-5', goods: { id: 202, slug: 'pet-carrier', name: '轻奢宠物外出航空包', price: 25900, coverImage: 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=400&q=80' }, priceDrop: false },
          { id: 'fav-6', goods: { id: 203, slug: 'pet-treats', name: '有机健康宠物零食礼盒', price: 5600, coverImage: 'https://images.unsplash.com/photo-1589924691195-41432c84c161?w=400&q=80' }, priceDrop: false }
        ],
        total: 12,
        page: 1,
        pageSize: 20
      });
    },

    'POST /api/v1/favorites/remove': function(body) {
      return success({ removed: true, favoriteId: body.favoriteId });
    },

    // ===== 会员模块 =====

    'GET /api/v1/member/info': function() {
      return success({
        userId: 'u1',
        level: 'GOLD',
        levelName: '金卡会员',
        levelIcon: 'crown',
        points: 12850,
        nextLevelPoints: 20000,
        pointsToNext: 7150,
        benefits: [
          { id: 'b1', name: '全场9折', desc: '正价商品享9折优惠', icon: 'badge-percent', active: true },
          { id: 'b2', name: '包邮', desc: '满99元免运费', icon: 'truck', active: true },
          { id: 'b3', name: '生日礼', desc: '生日当月赠200积分', icon: 'gift', active: true },
          { id: 'b4', name: '优先发货', desc: '金卡会员优先发货权', icon: 'zap', active: true },
          { id: 'b5', name: '双倍积分', desc: '消费享双倍积分', icon: 'coins', active: false },
          { id: 'b6', name: '专属客服', desc: '1对1宠物顾问', icon: 'headset', active: false }
        ],
        levelProgress: 64.2,
        levelHistory: [
          { level: 'SILVER', name: '银卡会员', achievedAt: '2025-06-15', icon: 'star' },
          { level: 'GOLD', name: '金卡会员', achievedAt: '2026-01-20', icon: 'crown' }
        ],
        nextLevels: [
          { level: 'PLATINUM', name: '铂金会员', pointsRequired: 20000, icon: 'trophy' },
          { level: 'DIAMOND', name: '钻石会员', pointsRequired: 50000, icon: 'gem' }
        ]
      });
    },

    // ===== 积分模块 =====

    'GET /api/v1/points/balance': function() {
      return success({
        total: 12850,
        available: 12850,
        frozen: 0,
        expiring: 1200,
        expireDate: '2026-09-15'
      });
    },

    'GET /api/v1/points/records': function() {
      return success({
        records: [
          { id: 'pr-1', type: 'EARN', title: '购物奖励', amount: 350, date: '2026-07-20', desc: '订单 #MOY17846830001' },
          { id: 'pr-2', type: 'EARN', title: '每日签到', amount: 50, date: '2026-07-19', desc: '连续签到第7天' },
          { id: 'pr-3', type: 'REDEEM', title: '兑换优惠券', amount: -500, date: '2026-07-18', desc: '满200减30券' },
          { id: 'pr-4', type: 'EARN', title: '评价奖励', amount: 100, date: '2026-07-17', desc: '商品评价' },
          { id: 'pr-5', type: 'EARN', title: '生日礼', amount: 200, date: '2026-07-15', desc: '会员生日积分' },
          { id: 'pr-6', type: 'EARN', title: '购物奖励', amount: 280, date: '2026-07-10', desc: '订单 #MOY1784678005' },
          { id: 'pr-7', type: 'REDEEM', title: '兑换商品', amount: -1000, date: '2026-07-05', desc: '宠物玩具球' },
          { id: 'pr-8', type: 'EARN', title: '邀请好友', amount: 500, date: '2026-06-28', desc: '好友注册奖励' }
        ],
        total: 12850,
        page: 1,
        pageSize: 20
      });
    },

    'GET /api/v1/points/mall': function() {
      return success({
        categories: [
          { id: 'c1', name: '优惠券', icon: 'ticket' },
          { id: 'c2', name: '实物商品', icon: 'gift' },
          { id: 'c3', name: '虚拟卡券', icon: 'credit-card' },
          { id: 'c4', name: '抽奖', icon: 'sparkles' }
        ],
        products: [
          { id: 'pm-1', name: '满200减30优惠券', points: 500, stock: 100, image: 'https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=200&q=80', category: 'c1', sold: 2340 },
          { id: 'pm-2', name: '满500减80优惠券', points: 1200, stock: 50, image: 'https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=200&q=80', category: 'c1', sold: 890 },
          { id: 'pm-3', name: '宠物玩具球', points: 1000, stock: 30, image: 'https://images.unsplash.com/photo-1576201836106-db1758fd1c97?w=200&q=80', category: 'c2', sold: 156 },
          { id: 'pm-4', name: 'MOYUYO定制围巾', points: 3000, stock: 15, image: 'https://images.unsplash.com/photo-1592194996308-7b43878e84a6?w=200&q=80', category: 'c2', sold: 78 },
          { id: 'pm-5', name: '星巴克宠物友好券', points: 800, stock: 200, image: 'https://images.unsplash.com/photo-1504630083234-14187a9df0f5?w=200&q=80', category: 'c3', sold: 450 },
          { id: 'pm-6', name: '幸运大转盘抽奖', points: 100, stock: 999, image: 'https://images.unsplash.com/photo-1518895949257-7621c3fb1f8b?w=200&q=80', category: 'c4', sold: 5600 }
        ]
      });
    },

    // ===== 优惠券模块 =====

    'GET /api/v1/coupon/list': function() {
      return success({
        stats: { total: 5, available: 3, used: 1, expired: 1 },
        coupons: [
          { id: 'cp-1', name: '满200减30', type: 'FULL_REDUCTION', threshold: 20000, discount: 3000, status: 'AVAILABLE', validFrom: '2026-07-01', validTo: '2026-08-31', desc: '正价商品可用', icon: 'ticket' },
          { id: 'cp-2', name: '新用户专享8折', type: 'PERCENTAGE', threshold: 0, discount: 20, status: 'AVAILABLE', validFrom: '2026-07-15', validTo: '2026-08-15', desc: '最高减50元', icon: 'badge-percent' },
          { id: 'cp-3', name: '宠物洗护满99减15', type: 'FULL_REDUCTION', threshold: 9900, discount: 1500, status: 'AVAILABLE', validFrom: '2026-07-01', validTo: '2026-07-31', desc: '限洗护品类', icon: 'droplets' },
          { id: 'cp-4', name: '端午满300减50', type: 'FULL_REDUCTION', threshold: 30000, discount: 5000, status: 'USED', validFrom: '2026-06-01', validTo: '2026-06-30', usedAt: '2026-06-20', desc: '已使用', icon: 'ticket' },
          { id: 'cp-5', name: '五一满100减20', type: 'FULL_REDUCTION', threshold: 10000, discount: 2000, status: 'EXPIRED', validFrom: '2026-05-01', validTo: '2026-05-07', desc: '已过期', icon: 'ticket' }
        ]
      });
    },

    'GET /api/v1/coupon/center': function() {
      return success({
        todayNew: 3,
        claimable: [
          { id: 'cc-1', name: '新人满99减20', type: 'FULL_REDUCTION', threshold: 9900, discount: 2000, stock: 500, claimed: 320, validDays: 30, icon: 'badge-percent', desc: '全场通用' },
          { id: 'cc-2', name: '宠物零食8折券', type: 'PERCENTAGE', threshold: 0, discount: 20, stock: 200, claimed: 156, validDays: 15, icon: 'cookie', desc: '限零食品类' },
          { id: 'cc-3', name: '满300减50', type: 'FULL_REDUCTION', threshold: 30000, discount: 5000, stock: 100, claimed: 67, validDays: 30, icon: 'ticket', desc: '全场通用' },
          { id: 'cc-4', name: '满500减100', type: 'FULL_REDUCTION', threshold: 50000, discount: 10000, stock: 50, claimed: 23, validDays: 30, icon: 'ticket', desc: '全场通用' },
          { id: 'cc-5', name: '宠物洗护9折', type: 'PERCENTAGE', threshold: 0, discount: 10, stock: 300, claimed: 189, validDays: 15, icon: 'droplets', desc: '限洗护品类' },
          { id: 'cc-6', name: '会员专享满200减40', type: 'FULL_REDUCTION', threshold: 20000, discount: 4000, stock: 100, claimed: 45, validDays: 30, icon: 'crown', desc: '金卡及以上可用' }
        ]
      });
    },

    'POST /api/v1/coupon/claim': function(body) {
      return success({ claimed: true, couponId: body.couponId, name: body.couponName || '优惠券' });
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
          var urlWithoutQuery = url.split('?')[0];
          var regex = new RegExp('^' + methodPart + ' ' + escapedPattern + '$');
          if (regex.test(method + ' ' + urlWithoutQuery)) {
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
