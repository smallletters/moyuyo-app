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
        nickname: 'PetLover Xiaoya',
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
        return error(2001, 'Please enter verification code');
      }

      var user = mockDB.users.find(function(u) { return u.phone === phone; });
      if (!user) {
        user = {
          userId: 10087,
          phone: phone,
          nickname: 'Phone User',
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
        return error(1101, 'This email is already registered');
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
        return error(1101, 'This phone number is already registered');
      }

      var newUser = {
        userId: 10000 + mockDB.users.length + 1,
        phone: phone,
        nickname: body.nickname || 'Phone User',
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
        return error(1001, 'Not logged in');
      }

      var user = mockDB.users.find(function(u) { return u.userId === tokenData.userId; });
      if (!user) {
        return error(2002, 'User not found');
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

    'GET /api/v1/user/transactions': function() {
      return success({
        records: [
          { id: 'tx-1', type: 'INCOME', title: 'Shopping points reward', amount: 350, balance: 12850, date: '2026-07-20 14:30', status: 'COMPLETED' },
          { id: 'tx-2', type: 'EXPENSE', title: 'Order payment', amount: -12800, balance: 12500, date: '2026-07-18 10:15', status: 'COMPLETED', orderNo: 'MOY1784683777099' },
          { id: 'tx-3', type: 'INCOME', title: 'Friend invitation reward', amount: 500, balance: 25300, date: '2026-07-15 09:00', status: 'COMPLETED' },
          { id: 'tx-4', type: 'EXPENSE', title: 'Redeem coupon', amount: -500, balance: 24800, date: '2026-07-10 16:00', status: 'COMPLETED' },
          { id: 'tx-5', type: 'INCOME', title: 'Daily check-in', amount: 50, balance: 25300, date: '2026-07-09 08:00', status: 'COMPLETED' }
        ],
        total: 12850
      });
    },

    'POST /api/v1/user/account/delete': function() {
      return success({ message: 'Account deletion request submitted, can be revoked within 7 days', deleted: false, pendingDays: 7 });
    },

    'POST /api/v1/user/data/export': function() {
      return success({ message: 'Data export request submitted, will be sent to your email within 24 hours', exportId: 'exp-' + Date.now() });
    },

    'GET /api/v1/address/list': function() {
      return success({
        addresses: [
          { id: 'addr-1', name: 'Xiaoya', phone: '138****6789', province: 'Shanghai', city: 'Shanghai', district: 'Pudong New Area', detail: '1088 Lujiazui Ring Road', isDefault: true },
          { id: 'addr-2', name: 'Xiaoya', phone: '138****6789', province: 'Zhejiang', city: 'Hangzhou', district: 'Yuhang District', detail: 'Cangqian Street MOYUYO Logistics Park', isDefault: false },
          { id: 'addr-3', name: 'Xiaoming', phone: '139****1234', province: 'Beijing', city: 'Beijing', district: 'Chaoyang District', detail: 'Wangjing SOHO T1, 15F', isDefault: false }
        ]
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
          { id: 1, image: 'https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=750&q=80', link: '/product-detail.html?slug=shampoo-set', title: 'Spring Care Festival' },
          { id: 2, image: 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=750&q=80', link: '/product-detail.html?slug=pet-bed', title: 'Comfortable Sleep' }
        ],
        kingKong: [
          { id: 1, name: 'Grooming', icon: 'https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=100&q=60', link: '/category.html?catId=1' },
          { id: 2, name: 'Food', icon: 'https://images.unsplash.com/photo-1589924691195-41432c84c161?w=100&q=60', link: '/category.html?catId=2' },
          { id: 3, name: 'Toys', icon: 'https://images.unsplash.com/photo-1576201836106-db1758fd1c97?w=100&q=60', link: '/category.html?catId=3' },
          { id: 4, name: 'Clothing', icon: 'https://images.unsplash.com/photo-1592194996308-7b43878e84a6?w=100&q=60', link: '/category.html?catId=4' },
          { id: 5, name: 'Leashes', icon: 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=100&q=60', link: '/category.html?catId=5' },
          { id: 6, name: 'Beds', icon: 'https://images.unsplash.com/photo-1541781774459-bb2af2f05b55?w=100&q=60', link: '/category.html?catId=6' },
          { id: 7, name: 'Cleaning', icon: 'https://images.unsplash.com/photo-1516734212186-a967f81ad0d7?w=100&q=60', link: '/category.html?catId=7' },
          { id: 8, name: 'Healthcare', icon: 'https://images.unsplash.com/photo-1623366302587-b38b1ddaefd9?w=100&q=60', link: '/category.html?catId=8' }
        ],
        products: [
          { id: 101, slug: 'shampoo-set', name: 'Gentle Plant-Based Pet Grooming Set', subtitle: 'Natural ingredients, gentle and non-irritating', price: 12800, originalPrice: 19800, coverImage: 'https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=400&q=80', rating: 4.8, reviewCount: 2340 },
          { id: 102, slug: 'pet-harness', name: 'Urban Explorer Pet Harness Set', subtitle: 'Anti-pull design, comfortable and breathable', price: 8900, originalPrice: 12900, coverImage: 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=400&q=80', rating: 4.9, reviewCount: 1856 },
          { id: 103, slug: 'cat-tree', name: 'Cloud Cat Tree', subtitle: 'Multi-level design, scratch and rest in one', price: 29900, originalPrice: 39900, coverImage: 'https://images.unsplash.com/photo-1545249390-6bdfa286032f?w=400&q=80', rating: 4.7, reviewCount: 890 },
          { id: 104, slug: 'dog-food', name: 'All-Natural Grain-Free Dog Food', subtitle: 'High protein, hypoallergenic formula', price: 16800, originalPrice: 22800, coverImage: 'https://images.unsplash.com/photo-1589924691195-41432c84c161?w=400&q=80', rating: 4.9, reviewCount: 3421 },
          { id: 105, slug: 'pet-toy', name: 'Smart Interactive Ball', subtitle: 'Auto-rolling, keeps your pet company', price: 6900, originalPrice: 9900, coverImage: 'https://images.unsplash.com/photo-1576201836106-db1758fd1c97?w=400&q=80', rating: 4.6, reviewCount: 567 },
          { id: 106, slug: 'pet-bed', name: 'Memory Foam Pet Mattress', subtitle: 'Orthopedic support, joint protection', price: 19900, originalPrice: 29900, coverImage: 'https://images.unsplash.com/photo-1541781774459-bb2af2f05b55?w=400&q=80', rating: 4.8, reviewCount: 1234 }
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
          id: 1, name: 'Grooming & Beauty', icon: 'sparkles', subcategories: [
            { id: 11, name: 'Shampoo' }, { id: 12, name: 'Conditioner' }, { id: 13, name: 'Brush' }, { id: 14, name: 'Nail Clippers' }
          ]
        },
        {
          id: 2, name: 'Pet Food', icon: 'utensils', subcategories: [
            { id: 21, name: 'Dog Food' }, { id: 22, name: 'Cat Food' }, { id: 23, name: 'Treats' }, { id: 24, name: 'Supplements' }
          ]
        },
        {
          id: 3, name: 'Toys & Play', icon: 'gamepad-2', subcategories: [
            { id: 31, name: 'Balls' }, { id: 32, name: 'Ropes' }, { id: 33, name: 'Puzzle' }, { id: 34, name: 'Electronic' }
          ]
        },
        {
          id: 4, name: 'Clothing & Accessories', icon: 'shirt', subcategories: [
            { id: 41, name: 'Clothes' }, { id: 42, name: 'Collars' }, { id: 43, name: 'Shoes' }, { id: 44, name: 'Hats' }
          ]
        },
        {
          id: 5, name: 'Outdoor & Leashes', icon: 'map-pin', subcategories: [
            { id: 51, name: 'Leash' }, { id: 52, name: 'Harness' }, { id: 53, name: 'Collar' }, { id: 54, name: 'Backpack' }
          ]
        },
        {
          id: 6, name: 'Beds & Furniture', icon: 'home', subcategories: [
            { id: 61, name: 'Mattress' }, { id: 62, name: 'Cat Tree' }, { id: 63, name: 'Dog House' }, { id: 64, name: 'Mat' }
          ]
        },
        {
          id: 7, name: 'Cleaning Supplies', icon: 'droplets', subcategories: [
            { id: 71, name: 'Cat Litter' }, { id: 72, name: 'Pee Pads' }, { id: 73, name: 'Deodorizer' }, { id: 74, name: 'Wipes' }
          ]
        },
        {
          id: 8, name: 'Health & Medical', icon: 'heart-pulse', subcategories: [
            { id: 81, name: 'Deworming' }, { id: 82, name: 'Vitamins' }, { id: 83, name: 'Vaccine' }, { id: 84, name: 'First Aid' }
          ]
        }
      ]);
    },

    'GET /api/v1/goods/search/hot': function() {
      return success({
        list: [
          { keyword: 'Pet Deworming', tag: 'Hot' },
          { keyword: 'Summer Care', tag: 'New' },
          { keyword: 'Cat Tree', tag: '' },
          { keyword: 'Dog Treats', tag: 'Hot' },
          { keyword: 'Pet Mattress', tag: '' },
          { keyword: 'Pet Stroller', tag: 'New' },
          { keyword: 'Cat Litter', tag: '' },
          { keyword: 'Dog Clothes', tag: '' },
          { keyword: 'Pet Toys', tag: 'Hot' },
          { keyword: 'Cat Food', tag: '' }
        ]
      });
    },

    'GET /api/v1/goods/search/suggest': function() {
      return success({
        suggestions: ['Pet Shampoo', 'Pet Body Wash', 'Pet Conditioner', 'Pet Brush', 'Pet Nail Clippers']
      });
    },

    'GET /api/v1/goods/[^/?]+': function(body, url) {
      if (url.indexOf('/reviews') !== -1) return null;
      var slugMatch = url.match(/\/goods\/([^?]+)/);
      var slug = slugMatch ? slugMatch[1] : '';

      var products = {
        'shampoo-set': {
          id: 101, slug: 'shampoo-set', name: 'Gentle Plant-Based Pet Grooming Set',
          brand: 'MOYUYO CARE', price: 12800, originalPrice: 19800, memberPrice: 10900,
          description: 'Made with natural plant extracts, gentle cleansing without irritation, suitable for pets with sensitive skin.',
          images: [
            { url: 'https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=600&q=80', alt: 'Product Image 1' },
            { url: 'https://images.unsplash.com/photo-1516734212186-a967f81ad0d7?w=600&q=80', alt: 'Product Image 2' },
            { url: 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=600&q=80', alt: 'Product Image 3' }
          ],
          skus: [
            { id: 'sku-101-1', price: 12800, attributes: { 'Color': 'Vanilla White', 'Size': '250ml' } },
            { id: 'sku-101-2', price: 19800, attributes: { 'Color': 'Vanilla White', 'Size': '500ml' } },
            { id: 'sku-101-3', price: 32800, attributes: { 'Color': 'Fresh Green', 'Size': '500ml' } }
          ]
        },
        'pet-harness': {
          id: 102, slug: 'pet-harness', name: 'Urban Explorer Pet Harness Set',
          brand: 'MOYUYO GO', price: 8900, originalPrice: 12900, memberPrice: 7900,
          description: 'Anti-pull design, breathable mesh, reflective strips for night safety.',
          images: [
            { url: 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=600&q=80', alt: 'Product Image 1' },
            { url: 'https://images.unsplash.com/photo-1592194996308-7b43878e84a6?w=600&q=80', alt: 'Product Image 2' }
          ],
          skus: [
            { id: 'sku-102-1', price: 8900, attributes: { 'Color': 'Graphite Black', 'Size': 'S' } },
            { id: 'sku-102-2', price: 9900, attributes: { 'Color': 'Graphite Black', 'Size': 'M' } },
            { id: 'sku-102-3', price: 10900, attributes: { 'Color': 'Caramel Brown', 'Size': 'M' } }
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
        tags: ['Good quality', 'Fast shipping', 'Great value', 'Pet loves it'],
        list: [
          { userName: 'Xiaomei\'s Mom', avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=1', rating: 5, content: 'My Golden Retriever\'s coat became incredibly soft after using this shampoo, and the scent is pleasant without being overpowering.', createTime: '2026-07-15T10:30:00Z' },
          { userName: 'Scooper Xiao Li', avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=2', rating: 5, content: 'Already repurchased for the third time. My cat doesn\'t resist bath time, and the foam is rich and easy to rinse off.', createTime: '2026-07-12T14:20:00Z' },
          { userName: 'Pet Expert', avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=3', rating: 4, content: 'Overall good, but the price is a bit high. Will stock up when there\'s a sale.', createTime: '2026-07-10T09:15:00Z' }
        ]
      });
    },

    'POST /api/v1/cart/add': function(body) {
      var token = localStorage.getItem('moyuyo_access_token');
      if (!token || !mockDB.tokens[token]) {
        return error(1001, 'Not logged in');
      }
      // 确保购物车数据存在
      var userId = mockDB.tokens[token].userId;
      if (!mockDB.carts) mockDB.carts = {};
      if (!mockDB.carts[userId]) mockDB.carts[userId] = [];
      mockDB.carts[userId].push({
        id: 'cart-' + mockDB.carts[userId].length, skuId: body.skuId, quantity: body.quantity || 1,
        name: 'Pet Grooming Set', price: 12800, image: 'https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=200&q=60', spec: 'Vanilla White / 500ml'
      });
      return success({ cartId: 'cart-' + mockDB.carts[userId].length, skuId: body.skuId, quantity: body.quantity });
    },

    'GET /api/v1/cart/list': function() {
      var token = localStorage.getItem('moyuyo_access_token');
      if (!token || !mockDB.tokens[token]) {
        return error(1001, 'Not logged in');
      }
      var userId = mockDB.tokens[token].userId;
      if (!mockDB.carts) mockDB.carts = {};
      if (!mockDB.carts[userId]) {
        mockDB.carts[userId] = [
          { id: 'cart-1', skuId: 'sku-101-1', quantity: 1, name: 'Gentle Plant-Based Pet Grooming Set', price: 12800, image: 'https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=200&q=60', spec: 'Vanilla White / 250ml' },
          { id: 'cart-2', skuId: 'sku-102-2', quantity: 1, name: 'Urban Explorer Pet Harness Set', price: 9900, image: 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=200&q=60', spec: 'Graphite Black / M' },
          { id: 'cart-3', skuId: 'sku-103-1', quantity: 2, name: 'Cloud Cat Tree', price: 29900, image: 'https://images.unsplash.com/photo-1545249390-6bdfa286032f?w=200&q=60', spec: 'Standard' }
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
        return error(1001, 'Not logged in');
      }
      var userId = mockDB.tokens[token].userId;
      if (!mockDB.carts) mockDB.carts = {};
      if (!mockDB.carts[userId]) {
        mockDB.carts[userId] = [
          { id: 'cart-1', skuId: 'sku-101-1', quantity: 1, name: 'Gentle Plant-Based Pet Grooming Set', price: 12800, image: 'https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=200&q=60', spec: 'Vanilla White / 250ml' },
          { id: 'cart-2', skuId: 'sku-102-2', quantity: 1, name: 'Urban Explorer Pet Harness Set', price: 9900, image: 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=200&q=60', spec: 'Graphite Black / M' }
        ];
      }
      return success({
        items: mockDB.carts[userId],
        subtotal: mockDB.carts[userId].reduce(function(sum, item) { return sum + item.price * item.quantity; }, 0),
        shippingFee: 0,
        discount: 2000,
        total: mockDB.carts[userId].reduce(function(sum, item) { return sum + item.price * item.quantity; }, 0) - 2000,
        addressId: 'addr-1',
        address: { name: 'Xiaoya', phone: '138****6789', full: '1088 Lujiazui Ring Road, Pudong New Area, Shanghai' }
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
          { id: 'order-1', orderNo: 'MOY20260715001', status: 'COMPLETED', totalAmount: 12800, createdAt: '2026-07-15T10:30:00Z', items: [{ name: 'Gentle Plant-Based Pet Grooming Set', image: 'https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=200&q=60', quantity: 1, price: 12800 }] },
          { id: 'order-2', orderNo: 'MOY20260718002', status: 'SHIPPED', totalAmount: 9900, createdAt: '2026-07-18T14:20:00Z', items: [{ name: 'Urban Explorer Pet Harness Set', image: 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=200&q=60', quantity: 1, price: 9900 }] },
          { id: 'order-3', orderNo: 'MOY20260720003', status: 'PENDING', totalAmount: 59800, createdAt: '2026-07-20T09:15:00Z', items: [{ name: 'Cloud Cat Tree', image: 'https://images.unsplash.com/photo-1545249390-6bdfa286032f?w=200&q=60', quantity: 2, price: 29900 }] },
          { id: 'order-4', orderNo: 'MOY20260721004', status: 'PAID', totalAmount: 16800, createdAt: '2026-07-21T16:45:00Z', items: [{ name: 'All-Natural Grain-Free Dog Food', image: 'https://images.unsplash.com/photo-1589924691195-41432c84c161?w=200&q=60', quantity: 1, price: 16800 }] }
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
        paymentMethod: 'WeChat Pay',
        createdAt: '2026-07-10T15:22:00Z',
        etd: 'July 12, 2026',
        address: { name: 'Xiaoya', phone: '138****8888', full: '690 Bibo Road, Zhangjiang Hi-Tech Park, Pudong New Area, Shanghai' },
        items: [
          { name: 'Classic Nylon Pet Leash Set', image: 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=200&q=60', quantity: 1, price: 8900, spec: 'Navy Blue / Size M' }
        ],
        logistics: [
          { time: '2026-07-11T14:30:00Z', status: 'Arrived', desc: 'Package arrived at Shanghai Pudong distribution station' },
          { time: '2026-07-10T22:15:00Z', status: 'In Transit', desc: 'Package dispatched from Hangzhou sorting center' },
          { time: '2026-07-10T08:00:00Z', status: 'Shipped', desc: 'Seller has shipped' },
          { time: '2026-07-09T16:30:00Z', status: 'Paid', desc: 'Order has been paid' }
        ]
      });
    },

    // ===== 宠物档案模块 =====

    'GET /api/v1/pet/list': function() {
      return success([
        {
          id: 'pet-1', name: 'Wangcai', breed: 'Golden Retriever', gender: 'MALE', age: '3 years 6 months',
          weight: 28.5, birthday: '2023-01-15', avatar: '../assets/pet-avatar-dog.jpg',
          cover: '../assets/pet-profile-dog.jpg', isDefault: true
        },
        {
          id: 'pet-2', name: 'Tuanzi', breed: 'Golden Retriever', gender: 'MALE', age: '2 years 3 months',
          weight: 28.5, birthday: '2024-04-10', avatar: '../assets/pet-avatar-dog.jpg',
          cover: '../assets/pet-profile-dog.jpg', isDefault: false
        },
        {
          id: 'pet-3', name: 'DouDou', breed: 'British Shorthair', gender: 'FEMALE', age: '1 year 2 months',
          weight: 4.2, birthday: '2025-05-20', avatar: '../assets/pet-avatar-dog.jpg',
          cover: '../assets/pet-profile-dog.jpg', isDefault: false
        }
      ]);
    },

    'GET /api/v1/pet/[^/?]+$': function(body, url) {
      var petId = url.match(/\/pet\/([^/?]+)/)[1];
      var pets = {
        'pet-1': {
          id: 'pet-1', name: 'Wangcai', breed: 'Golden Retriever', gender: 'MALE', age: '3 years 6 months',
          weight: 28.5, height: 58, birthday: '2023-01-15', color: 'Light Gold',
          avatar: '../assets/pet-avatar-dog.jpg', cover: '../assets/pet-profile-dog.jpg',
          health: {
            weight: { value: 28.5, unit: 'kg', status: 'NORMAL', label: 'Normal' },
            temperature: { value: 38.5, unit: '°C', status: 'NORMAL', label: 'Normal' },
            heartRate: { value: null, unit: 'bpm', status: 'NONE', label: 'No data' },
            foodIntake: { value: null, unit: 'g', status: 'NORMAL', label: 'Normal' }
          },
          careTimeline: [
            { type: 'BATH', title: 'Bath', date: '2026-07-15', desc: 'Used MOYUYO Gentle Grooming Set', icon: 'droplets', color: 'brand' },
            { type: 'DEWORM', title: 'Deworming', date: '2026-07-10', desc: 'Internal + external deworming', icon: 'shield', color: 'success' },
            { type: 'VACCINE', title: 'Vaccine', date: '2026-06-28', desc: 'Rabies vaccine booster', icon: 'check', color: 'brand' },
            { type: 'CHECKUP', title: 'Checkup', date: '2026-06-15', desc: 'Annual checkup, all normal', icon: 'heart', color: 'error' }
          ]
        },
        'pet-2': {
          id: 'pet-2', name: 'Tuanzi', breed: 'Golden Retriever', gender: 'MALE', age: '2 years 3 months',
          weight: 28.5, height: 55, birthday: '2024-04-10', color: 'Dark Gold',
          avatar: '../assets/pet-avatar-dog.jpg', cover: '../assets/pet-profile-dog.jpg',
          health: {
            weight: { value: 28.5, unit: 'kg', status: 'NORMAL', label: 'Normal' },
            temperature: { value: 38.2, unit: '°C', status: 'NORMAL', label: 'Normal' },
            heartRate: { value: 95, unit: 'bpm', status: 'NORMAL', label: 'Normal' },
            foodIntake: { value: 350, unit: 'g', status: 'NORMAL', label: 'Normal' }
          },
          careTimeline: [
            { type: 'DEWORM', title: 'Deworming', date: '2026-07-05', desc: 'Internal deworming, took Drontal', icon: 'shield', color: 'success' },
            { type: 'CHECKUP', title: 'Checkup', date: '2026-06-28', desc: 'Semi-annual checkup', icon: 'heart', color: 'error' },
            { type: 'VACCINE', title: 'Vaccine', date: '2026-06-10', desc: 'Annual vaccine', icon: 'check', color: 'brand' }
          ]
        },
        'pet-3': {
          id: 'pet-3', name: 'DouDou', breed: 'British Shorthair', gender: 'FEMALE', age: '1 year 2 months',
          weight: 4.2, height: 25, birthday: '2025-05-20', color: 'Blue-Gray',
          avatar: '../assets/pet-avatar-dog.jpg', cover: '../assets/pet-profile-dog.jpg',
          health: {
            weight: { value: 4.2, unit: 'kg', status: 'NORMAL', label: 'Normal' },
            temperature: { value: 38.8, unit: '°C', status: 'NORMAL', label: 'Normal' },
            heartRate: { value: null, unit: 'bpm', status: 'NONE', label: 'No data' },
            foodIntake: { value: null, unit: 'g', status: 'NORMAL', label: 'Normal' }
          },
          careTimeline: [
            { type: 'BATH', title: 'Bath', date: '2026-07-12', desc: 'Gentle body wash grooming', icon: 'droplets', color: 'brand' },
            { type: 'DEWORM', title: 'Deworming', date: '2026-07-01', desc: 'External deworming', icon: 'shield', color: 'success' }
          ]
        }
      };
      return success(pets[petId] || pets['pet-1']);
    },

    'GET /api/v1/pet/[^/?]+/diary': function(body, url) {
      return success({
        pages: [
          {
            month: 'July 2026', count: 6,
            entries: [
              { id: 'diary-1', date: '2026-07-08', weekday: 'Tuesday', content: 'Took Tuanzi to the park for a walk today and ran into a Corgi friend we haven\'t seen in ages. The two little guys chased each other on the grass for half an hour. On the way home, Tuanzi fell asleep right on the passenger seat — too cute.', tags: ['Daily', 'Outdoor'], likes: 12, comments: 3, photos: 3 },
              { id: 'diary-2', date: '2026-07-05', weekday: 'Saturday', content: 'Checkup day! Tuanzi was very well-behaved and only whimpered once during the shot. The vet says everything looks great and weight is well-controlled — keep up the current diet and exercise routine.', tags: ['Checkup', 'Weight', 'Vaccine'], likes: 24, comments: 8, photos: 2, weight: { value: 28.5, unit: 'kg', change: -0.3, trend: 'down' } },
              { id: 'diary-3', date: '2026-07-02', weekday: 'Wednesday', content: 'The new cat teaser toy arrived (even though Tuanzi is a dog), but he surprisingly loves this toy. Chased it around for half the evening — looks like exercise is covered.', tags: ['Daily', 'New Toy'], likes: 9, comments: 1, photos: 2 }
            ]
          },
          {
            month: 'June 2026', count: 4,
            entries: [
              { id: 'diary-4', date: '2026-06-28', weekday: 'Saturday', content: 'Appetite has been especially good this week, probably due to the new dog food. But we need to watch the treat intake, or the weight will go up too fast.', tags: ['Daily', 'Weight'], likes: 6, comments: 2, photos: 0, weight: { value: 28.8, unit: 'kg', change: 0.5, trend: 'up' } },
              { id: 'diary-5', date: '2026-06-15', weekday: 'Sunday', content: 'Summer is here so I gave Tuanzi a fresh trim — he looks like a little lion now. Turned a lot of heads on our walk. He seems to feel much cooler too.', tags: ['Styling', 'Grooming'], likes: 18, comments: 5, photos: 1 },
              { id: 'diary-6', date: '2026-06-03', weekday: 'Tuesday', content: 'Deworming medicine taken on schedule. Tuanzi was a bit drowsy afterward but recovered in about half an hour. Noting here that the next deworming is August 3rd.', tags: ['Daily', 'Deworming'], likes: 4, comments: 0, photos: 0 }
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
          change: 0.3, trend: 'up', status: 'NORMAL', label: 'Normal range'
        }
      });
    },

    'GET /api/v1/pet/[^/?]+/health/calendar': function(body, url) {
      return success({
        year: 2026, month: 7,
        events: {
          '3': [{ type: 'BATH', label: 'Bath' }],
          '7': [{ type: 'DIARY', label: 'Diary' }],
          '9': [{ type: 'DEWORM', label: 'Deworming' }],
          '12': [{ type: 'VACCINE', label: 'Vaccine' }, { type: 'BATH', label: 'Bath' }],
          '14': [{ type: 'CHECKUP', label: 'Checkup' }],
          '17': [{ type: 'BATH', label: 'Bath' }],
          '20': [{ type: 'DIARY', label: 'Diary' }],
          '25': [{ type: 'VACCINE', label: 'Vaccine' }],
          '29': [{ type: 'BATH', label: 'Bath' }]
        }
      });
    },

    'GET /api/v1/pet/[^/?]+/health/reminders': function(body, url) {
      return success({
        list: [
          { type: 'BATH', title: 'Bath', dueDate: '2026-07-06', remaining: -3, status: 'OVERDUE', desc: 'Expected date: July 6' },
          { type: 'VACCINE', title: 'Vaccine', dueDate: '2026-07-15', remaining: 6, status: 'UPCOMING', desc: 'Rabies vaccine booster' },
          { type: 'DEWORM', title: 'Deworming', dueDate: '2026-07-27', remaining: 18, status: 'NORMAL', desc: 'External deworming' },
          { type: 'CHECKUP', title: 'Checkup', dueDate: '2026-08-03', remaining: 25, status: 'NORMAL', desc: 'Semi-annual checkup' }
        ]
      });
    },

    'GET /api/v1/pet/[^/?]+/health/records': function(body, url) {
      return success({
        date: '2026-07-09',
        petName: 'DouDou',
        records: [
          { type: 'DEWORM', title: 'Deworming', time: '10:30', desc: 'Internal deworming, took 1 tablet of Drontal', icon: 'shield' },
          { type: 'WEIGHT', title: 'Weight', time: '09:15', desc: '4.2 kg (down 0.1 kg from last month)', icon: 'arrow-down' }
        ]
      });
    },

    'GET /api/v1/pet/[^/?]+/health/stats': function(body, url) {
      return success({
        month: 'July 2026',
        stats: {
          BATH: { count: 3, label: 'Bath' },
          VACCINE: { count: 1, label: 'Vaccine' },
          DEWORM: { count: 1, label: 'Deworming' },
          CHECKUP: { count: 0, label: 'Checkup' },
          DIARY: { count: 2, label: 'Diary' }
        }
      });
    },

    // ===== 收藏模块 =====

    'GET /api/v1/favorites/list': function() {
      return success({
        items: [
          { id: 'fav-1', goods: { id: 101, slug: 'shampoo-set', name: 'Gentle Plant-Based Pet Grooming Set', price: 12800, coverImage: 'https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=400&q=80' }, priceDrop: false },
          { id: 'fav-2', goods: { id: 102, slug: 'pet-harness', name: 'Classic Nylon Pet Leash Set', price: 8900, coverImage: 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=400&q=80' }, priceDrop: false },
          { id: 'fav-3', goods: { id: 105, slug: 'pet-toy', name: 'Eco-Friendly Plush Interactive Toy Ball', price: 4500, coverImage: 'https://images.unsplash.com/photo-1576201836106-db1758fd1c97?w=400&q=80' }, priceDrop: false },
          { id: 'fav-4', goods: { id: 201, slug: 'pet-jacket', name: 'Designer Pet Tech Jacket', price: 16800, originalPrice: 22800, coverImage: 'https://images.unsplash.com/photo-1592194996308-7b43878e84a6?w=400&q=80' }, priceDrop: true },
          { id: 'fav-5', goods: { id: 202, slug: 'pet-carrier', name: 'Premium Pet Travel Carrier', price: 25900, coverImage: 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=400&q=80' }, priceDrop: false },
          { id: 'fav-6', goods: { id: 203, slug: 'pet-treats', name: 'Organic Healthy Pet Treat Gift Box', price: 5600, coverImage: 'https://images.unsplash.com/photo-1589924691195-41432c84c161?w=400&q=80' }, priceDrop: false }
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
        levelName: 'Gold Member',
        levelIcon: 'crown',
        points: 12850,
        nextLevelPoints: 20000,
        pointsToNext: 7150,
        benefits: [
          { id: 'b1', name: '10% off Storewide', desc: '10% off regular-priced items', icon: 'badge-percent', active: true },
          { id: 'b2', name: 'Free Shipping', desc: 'Free shipping on orders over ¥99', icon: 'truck', active: true },
          { id: 'b3', name: 'Birthday Gift', desc: '200 bonus points in your birthday month', icon: 'gift', active: true },
          { id: 'b4', name: 'Priority Shipping', desc: 'Gold member priority shipping', icon: 'zap', active: true },
          { id: 'b5', name: 'Double Points', desc: 'Earn double points on purchases', icon: 'coins', active: false },
          { id: 'b6', name: 'Dedicated Support', desc: '1-on-1 pet consultant', icon: 'headset', active: false }
        ],
        levelProgress: 64.2,
        levelHistory: [
          { level: 'SILVER', name: 'Silver Member', achievedAt: '2025-06-15', icon: 'star' },
          { level: 'GOLD', name: 'Gold Member', achievedAt: '2026-01-20', icon: 'crown' }
        ],
        nextLevels: [
          { level: 'PLATINUM', name: 'Platinum Member', pointsRequired: 20000, icon: 'trophy' },
          { level: 'DIAMOND', name: 'Diamond Member', pointsRequired: 50000, icon: 'gem' }
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
          { id: 'pr-1', type: 'EARN', title: 'Shopping reward', amount: 350, date: '2026-07-20', desc: 'Order #MOY17846830001' },
          { id: 'pr-2', type: 'EARN', title: 'Daily check-in', amount: 50, date: '2026-07-19', desc: '7th consecutive check-in day' },
          { id: 'pr-3', type: 'REDEEM', title: 'Redeem coupon', amount: -500, date: '2026-07-18', desc: '¥30 off ¥200 coupon' },
          { id: 'pr-4', type: 'EARN', title: 'Review reward', amount: 100, date: '2026-07-17', desc: 'Product review' },
          { id: 'pr-5', type: 'EARN', title: 'Birthday gift', amount: 200, date: '2026-07-15', desc: 'Member birthday points' },
          { id: 'pr-6', type: 'EARN', title: 'Shopping reward', amount: 280, date: '2026-07-10', desc: 'Order #MOY1784678005' },
          { id: 'pr-7', type: 'REDEEM', title: 'Redeem product', amount: -1000, date: '2026-07-05', desc: 'Pet toy ball' },
          { id: 'pr-8', type: 'EARN', title: 'Invite friend', amount: 500, date: '2026-06-28', desc: 'Friend registration reward' }
        ],
        total: 12850,
        page: 1,
        pageSize: 20
      });
    },

    'GET /api/v1/points/mall': function() {
      return success({
        categories: [
          { id: 'c1', name: 'Coupons', icon: 'ticket' },
          { id: 'c2', name: 'Physical Goods', icon: 'gift' },
          { id: 'c3', name: 'Virtual Cards', icon: 'credit-card' },
          { id: 'c4', name: 'Lucky Draw', icon: 'sparkles' }
        ],
        products: [
          { id: 'pm-1', name: '¥30 off ¥200 coupon', points: 500, stock: 100, image: 'https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=200&q=80', category: 'c1', sold: 2340 },
          { id: 'pm-2', name: '¥80 off ¥500 coupon', points: 1200, stock: 50, image: 'https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=200&q=80', category: 'c1', sold: 890 },
          { id: 'pm-3', name: 'Pet Toy Ball', points: 1000, stock: 30, image: 'https://images.unsplash.com/photo-1576201836106-db1758fd1c97?w=200&q=80', category: 'c2', sold: 156 },
          { id: 'pm-4', name: 'MOYUYO Custom Scarf', points: 3000, stock: 15, image: 'https://images.unsplash.com/photo-1592194996308-7b43878e84a6?w=200&q=80', category: 'c2', sold: 78 },
          { id: 'pm-5', name: 'Starbucks Pet-Friendly Voucher', points: 800, stock: 200, image: 'https://images.unsplash.com/photo-1504630083234-14187a9df0f5?w=200&q=80', category: 'c3', sold: 450 },
          { id: 'pm-6', name: 'Lucky Spin Wheel', points: 100, stock: 999, image: 'https://images.unsplash.com/photo-1518895949257-7621c3fb1f8b?w=200&q=80', category: 'c4', sold: 5600 }
        ]
      });
    },

    // ===== 优惠券模块 =====

    'GET /api/v1/coupon/list': function() {
      return success({
        stats: { total: 5, available: 3, used: 1, expired: 1 },
        coupons: [
          { id: 'cp-1', name: '¥30 off ¥200', type: 'FULL_REDUCTION', threshold: 20000, discount: 3000, status: 'AVAILABLE', validFrom: '2026-07-01', validTo: '2026-08-31', desc: 'Valid on regular-priced items', icon: 'ticket' },
          { id: 'cp-2', name: 'New User Exclusive 20% off', type: 'PERCENTAGE', threshold: 0, discount: 20, status: 'AVAILABLE', validFrom: '2026-07-15', validTo: '2026-08-15', desc: 'Up to ¥50 off', icon: 'badge-percent' },
          { id: 'cp-3', name: '¥15 off ¥99 Pet Grooming', type: 'FULL_REDUCTION', threshold: 9900, discount: 1500, status: 'AVAILABLE', validFrom: '2026-07-01', validTo: '2026-07-31', desc: 'Grooming products only', icon: 'droplets' },
          { id: 'cp-4', name: 'Dragon Boat ¥50 off ¥300', type: 'FULL_REDUCTION', threshold: 30000, discount: 5000, status: 'USED', validFrom: '2026-06-01', validTo: '2026-06-30', usedAt: '2026-06-20', desc: 'Used', icon: 'ticket' },
          { id: 'cp-5', name: 'Labor Day ¥20 off ¥100', type: 'FULL_REDUCTION', threshold: 10000, discount: 2000, status: 'EXPIRED', validFrom: '2026-05-01', validTo: '2026-05-07', desc: 'Expired', icon: 'ticket' }
        ]
      });
    },

    'GET /api/v1/coupon/center': function() {
      return success({
        todayNew: 3,
        claimable: [
          { id: 'cc-1', name: 'New User ¥20 off ¥99', type: 'FULL_REDUCTION', threshold: 9900, discount: 2000, stock: 500, claimed: 320, validDays: 30, icon: 'badge-percent', desc: 'Storewide' },
          { id: 'cc-2', name: 'Pet Treats 20% off', type: 'PERCENTAGE', threshold: 0, discount: 20, stock: 200, claimed: 156, validDays: 15, icon: 'cookie', desc: 'Treats only' },
          { id: 'cc-3', name: '¥50 off ¥300', type: 'FULL_REDUCTION', threshold: 30000, discount: 5000, stock: 100, claimed: 67, validDays: 30, icon: 'ticket', desc: 'Storewide' },
          { id: 'cc-4', name: '¥100 off ¥500', type: 'FULL_REDUCTION', threshold: 50000, discount: 10000, stock: 50, claimed: 23, validDays: 30, icon: 'ticket', desc: 'Storewide' },
          { id: 'cc-5', name: 'Pet Grooming 10% off', type: 'PERCENTAGE', threshold: 0, discount: 10, stock: 300, claimed: 189, validDays: 15, icon: 'droplets', desc: 'Grooming products only' },
          { id: 'cc-6', name: 'Members Exclusive ¥40 off ¥200', type: 'FULL_REDUCTION', threshold: 20000, discount: 4000, stock: 100, claimed: 45, validDays: 30, icon: 'crown', desc: 'Gold tier and above' }
        ]
      });
    },

    'POST /api/v1/coupon/claim': function(body) {
      return success({ claimed: true, couponId: body.couponId, name: body.couponName || 'Coupon' });
    },

    // ===== 售后模块 =====

    'GET /api/v1/after-sale/list': function(body, url) {
      var status = url.match(/status=(\w+)/);
      status = status ? status[1] : 'ALL';
      var allItems = [
        {
          id: 'as-1', orderNo: 'MOY1784683777099', type: 'RETURN_REFUND', typeName: 'Return & Refund',
          status: 'WAIT_RETURN', statusName: 'Awaiting Return', statusColor: 'brand',
          goods: { name: 'Gentle Plant-Based Pet Grooming Set', spec: '500ml / Classic', image: 'https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=200&q=80' },
          refundAmount: 12800, applyDate: '2026-07-05', progress: 3, totalSteps: 6,
          steps: ['Applied', 'Approved', 'Awaiting Return', 'Warehouse Received', 'Refunding', 'Completed']
        },
        {
          id: 'as-2', orderNo: 'MOY1784683000020', type: 'REFUND_ONLY', typeName: 'Refund Only',
          status: 'REFUNDING', statusName: 'Refunding', statusColor: 'warning',
          goods: { name: 'Classic Nylon Pet Leash Set', spec: 'Size M / Black', image: 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=200&q=80' },
          refundAmount: 8900, applyDate: '2026-06-28', progress: 4, totalSteps: 6,
          steps: ['Applied', 'Approved', 'Warehouse Received', 'Warehouse Received', 'Refunding', 'Completed']
        },
        {
          id: 'as-3', orderNo: 'MOY1784678005001', type: 'EXCHANGE', typeName: 'Exchange',
          status: 'COMPLETED', statusName: 'Completed', statusColor: 'success',
          goods: { name: 'Interactive Puzzle Toy', spec: 'Large / Chew-Resistant', image: 'https://images.unsplash.com/photo-1576201836106-db1758fd1c97?w=200&q=80' },
          refundAmount: 0, applyDate: '2026-06-15', progress: 6, totalSteps: 6,
          steps: ['Applied', 'Approved', 'Awaiting Return', 'Warehouse Received', 'Replacement Shipped', 'Completed']
        },
        {
          id: 'as-4', orderNo: 'MOY1784678006002', type: 'RETURN_REFUND', typeName: 'Return & Refund',
          status: 'REJECTED', statusName: 'Rejected', statusColor: 'error',
          goods: { name: 'Pet Chicken Jerky Treats', spec: '200g / Original', image: 'https://images.unsplash.com/photo-1589924691195-41432c84c161?w=200&q=80' },
          refundAmount: 4500, applyDate: '2026-06-10', progress: 1, totalSteps: 6,
          steps: ['Applied', 'Approved', 'Awaiting Return', 'Warehouse Received', 'Refunding', 'Completed'],
          rejectReason: 'Food products are not eligible for return'
        },
        {
          id: 'as-5', orderNo: 'MOY1784678007003', type: 'REFUND_ONLY', typeName: 'Refund Only',
          status: 'CANCELLED', statusName: 'Cancelled', statusColor: 'muted',
          goods: { name: 'Pet Dental Chew Set', spec: '3-pack', image: 'https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=200&q=80' },
          refundAmount: 3200, applyDate: '2026-05-20', progress: 0, totalSteps: 6,
          steps: ['Applied', 'Approved', 'Awaiting Return', 'Warehouse Received', 'Refunding', 'Completed']
        }
      ];

      var filtered = status === 'ALL' ? allItems : allItems.filter(function(item) {
        if (status === 'PROCESSING') return item.status === 'WAIT_RETURN' || item.status === 'REFUNDING';
        if (status === 'COMPLETED') return item.status === 'COMPLETED';
        if (status === 'REJECTED') return item.status === 'REJECTED';
        if (status === 'CANCELLED') return item.status === 'CANCELLED';
        return true;
      });

      return success({ items: filtered, total: allItems.length });
    },

    'GET /api/v1/after-sale/[^/?]+$': function(body, url) {
      var id = url.match(/\/after-sale\/([^/?]+)/)[1];
      return success({
        id: id,
        orderNo: 'MOY1784683777099',
        type: 'RETURN_REFUND', typeName: 'Return & Refund',
        status: 'WAIT_RETURN', statusName: 'Awaiting Return',
        goods: { name: 'Gentle Plant-Based Pet Grooming Set', spec: '500ml / Classic', image: 'https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=200&q=80', price: 12800 },
        refundAmount: 12800,
        applyDate: '2026-07-05 14:30',
        reason: 'Damaged product packaging',
        description: 'Outer packaging showed significant crushing upon arrival, slight leakage from the bottle',
        returnAddress: { name: 'MOYUYO Returns Warehouse', phone: '400-888-6688', address: 'Warehouse 3, Zone B, MOYUYO Logistics Park, Cangqian Street, Yuhang District, Hangzhou, Zhejiang' },
        timeline: [
          { status: 'APPLY', title: 'Application Submitted', time: '2026-07-05 14:30', desc: 'Return & refund request submitted' },
          { status: 'APPROVE', title: 'Approved', time: '2026-07-05 18:00', desc: 'Your return request has been approved. Please send back the item as soon as possible.' },
          { status: 'WAIT_RETURN', title: 'Awaiting Return', time: '--', desc: 'Please return the item within 7 days and provide the tracking number' }
        ],
        progress: 3, totalSteps: 6
      });
    },

    // ===== 反馈模块 =====

    'GET /api/v1/feedback/list': function() {
      return success({
        items: [
          { id: 'fb-1', title: 'Feature suggestion: Add pet social features', type: 'Experience Suggestion', status: 'PENDING', statusName: 'Processing', date: '2026-07-01' },
          { id: 'fb-2', title: 'Product issue: Dog clothing sizes run small', type: 'Product Issue', status: 'REPLIED', statusName: 'Replied', date: '2026-06-25' },
          { id: 'fb-3', title: 'Shipping issue: Delivery delayed over 3 days', type: 'Shipping Issue', status: 'COMPLETED', statusName: 'Completed', date: '2026-06-10' }
        ],
        total: 3
      });
    },

    'POST /api/v1/feedback/submit': function(body) {
      return success({
        id: 'fb-' + Date.now(),
        submitted: true,
        message: 'Feedback submitted. We will process it as soon as possible.'
      });
    },

    // ===== 社区模块 =====

    'GET /api/v1/community/feed': function(body, url) {
      var category = url.match(/category=(\w+)/);
      category = category ? category[1] : 'ALL';
      var allPosts = [
        {
          id: 'post-1', title: 'First time taking your cat to the vet? Here\'s what you need to prepare', author: { name: 'Little Joys of Cat Parenting', avatar: '', isVIP: true }, pet: 'Tuanzi',
          cover: '', tags: ['Pet Daily', 'Cat Care Tips'], ipTags: ['MILO', 'OLIVE'],
          likes: 328, comments: 56, stars: 142, time: '3 hours ago', category: 'EXPERIENCE',
          isLiked: false, isStarred: false
        },
        {
          id: 'post-2', title: 'MOYUYO\'s new pet grooming set is amazing!', author: { name: 'Dog Lover Xiao Wang', avatar: '', isVIP: false }, pet: 'DouDou',
          cover: '', tags: ['Product Share', 'Grooming'], ipTags: ['MILO'],
          likes: 215, comments: 43, stars: 89, time: '5 hours ago', category: 'SHARE',
          isLiked: false, isStarred: false
        },
        {
          id: 'post-3', title: 'Took Wangcai out for the weekend and found an amazing pet-friendly park', author: { name: 'Outdoor Enthusiast Ajie', avatar: '', isVIP: false }, pet: 'Wangcai',
          cover: '', tags: ['Outdoor', 'Pet-Friendly'], ipTags: ['OLIVE', 'LUNA'],
          likes: 156, comments: 28, stars: 67, time: '8 hours ago', category: 'LIFE',
          isLiked: false, isStarred: false
        },
        {
          id: 'post-4', title: 'Cat Behavior Decoded: Why do cats always knock things off tables?', author: { name: 'Pet Nutritionist Lucy', avatar: '', isVIP: true, isVerified: true }, pet: 'Tuanzi',
          cover: '', tags: ['Science', 'Cat Behavior'], ipTags: ['MILO'],
          likes: 492, comments: 87, stars: 203, time: '12 hours ago', category: 'SCIENCE',
          isLiked: false, isStarred: false
        },
        {
          id: 'post-5', title: 'Dog shedding season is here! These brushes are personally tested and great', author: { name: 'Golden Retriever Mom', avatar: '', isVIP: false }, pet: 'Wangcai',
          cover: '', tags: ['Product Share', 'Shedding Season'], ipTags: ['OLIVE'],
          likes: 189, comments: 35, stars: 76, time: '1 day ago', category: 'SHARE',
          isLiked: false, isStarred: false
        },
        {
          id: 'post-6', title: 'Complete Pet Checkup Guide: What to check at different ages?', author: { name: 'Vet Xiao Zhang', avatar: '', isVIP: true, isVerified: true }, pet: 'DouDou',
          cover: '', tags: ['Science', 'Checkup'], ipTags: ['MILO', 'LUNA'],
          likes: 367, comments: 62, stars: 145, time: '1 day ago', category: 'SCIENCE',
          isLiked: false, isStarred: false
        },
        {
          id: 'post-7', title: 'New dog owners must read! Complete guide from breed selection to daily care', author: { name: 'Newbie Pet Owner', avatar: '', isVIP: false }, pet: 'Wangcai',
          cover: '', tags: ['Beginner', 'Dog Care'], ipTags: ['OLIVE'],
          likes: 278, comments: 51, stars: 112, time: '2 days ago', category: 'EXPERIENCE',
          isLiked: false, isStarred: false
        },
        {
          id: 'post-8', title: 'Homemade Pet Treats: Chicken Jerky & Carrot Chips', author: { name: 'Pet Baker', avatar: '', isVIP: true }, pet: 'Tuanzi',
          cover: '', tags: ['DIY', 'Treats'], ipTags: ['MILO'],
          likes: 198, comments: 41, stars: 88, time: '2 days ago', category: 'LIFE',
          isLiked: false, isStarred: false
        }
      ];

      var filtered = category === 'ALL' ? allPosts : allPosts.filter(function(p) { return p.category === category; });
      return success({ posts: filtered, total: allPosts.length, page: 1, pageSize: 20 });
    },

    'GET /api/v1/community/topics': function() {
      return success({
        topics: [
          { id: 't1', name: 'Product Share', icon: 'star', count: 2340, color: 'brand' },
          { id: 't2', name: 'Pet Care Tips', icon: 'lightbulb', count: 1890, color: 'warning' },
          { id: 't3', name: 'Cute Pet Daily', icon: 'camera', count: 3210, color: 'success' },
          { id: 't4', name: 'Science & Knowledge', icon: 'book-open', count: 1560, color: 'brand' },
          { id: 't5', name: 'Outdoor Activities', icon: 'map-pin', count: 890, color: 'success' },
          { id: 't6', name: 'Q&A', icon: 'help-circle', count: 670, color: 'warning' }
        ]
      });
    },

    'GET /api/v1/post/[^/?]+$': function(body, url) {
      var id = url.match(/\/post\/([^/?]+)/)[1];
      return success({
        id: id,
        title: 'First time taking your cat to the vet? Here\'s what you need to prepare',
        author: { name: 'Little Joys of Cat Parenting', avatar: '', isVIP: true, level: 'PRIME', followers: 1280 },
        pet: 'Tuanzi',
        content: '<p>Last week I took Tuanzi for his first comprehensive checkup. Here are some tips for new pet owners. As long as you prepare in advance, the whole process goes much more smoothly than you\'d expect.</p><p>First and most importantly, choose the right clinic. I recommend checking online reviews beforehand and choosing a properly licensed veterinary hospital. Pay attention to whether the clinic environment is clean and whether the vet is professional and patient.</p><p>Before heading out, remember to bring your cat\'s favorite blanket or toy — this helps them feel secure in an unfamiliar environment. If you have previous vaccination records or health history, bring those along too so the vet can get a full picture.</p><p>As for costs, a basic checkup typically ranges from ¥200-500, and it may be higher if additional tests are needed. I suggest discussing the price range with the vet in advance.</p>',
        tags: ['Pet Daily', 'Cat Care Tips'],
        ipTags: ['MILO', 'OLIVE'],
        images: 6,
        likes: 328, comments: 56, stars: 142, shares: 23,
        time: '3 hours ago',
        isLiked: false, isStarred: false, isFollowed: false,
        linkedProduct: { id: '101', name: 'Pet Portable Travel Carrier - Standard', price: 25900, originalPrice: 35900 }
      });
    },

    'GET /api/v1/post/[^/?]+/comments': function(body, url) {
      return success({
        total: 56,
        comments: [
          { id: 'c1', author: { name: 'Cat Lover Xiaomei', avatar: '', isVIP: false }, content: 'Super useful! My cat was so nervous the first time we went to the vet. Wish I had seen this earlier. Will definitely prepare better next time.', time: '2 hours ago', likes: 12, isLiked: false },
          { id: 'c2', author: { name: 'Scooper Diary', avatar: '', isVIP: false }, content: 'How long does a checkup usually take? I booked a 3pm appointment and I\'m worried it won\'t be enough time.', time: '1 hour ago', likes: 5, isLiked: false },
          { id: 'c3', author: { name: 'Pet Nutritionist Lucy', avatar: '', isVIP: true, isVerified: true }, content: 'To add to this — it\'s best to have your cat fast for 6-8 hours before the checkup for more accurate blood test results. Also, kittens and senior cats should have checkups every 6 months.', time: '45 minutes ago', likes: 28, isLiked: false },
          { id: 'c4', author: { name: 'Pet Lover', avatar: '', isVIP: false }, content: 'Bookmarked! I\'m taking my cat for a checkup next week — perfect timing.', time: '30 minutes ago', likes: 3, isLiked: false },
          { id: 'c5', author: { name: 'Cat Guardian', avatar: '', isVIP: false }, content: 'Do you need to fast before a checkup? Some say yes, others say no...', time: '15 minutes ago', likes: 1, isLiked: false }
        ]
      });
    },

    'POST /api/v1/post/create': function(body) {
      return success({ id: 'post-' + Date.now(), created: true, message: 'Post published successfully' });
    },

    'GET /api/v1/collection/list': function() {
      return success({
        collections: [
          { id: 'col-1', name: 'Cat Bathing Tutorial', desc: 'From preparation to bathing techniques, a step-by-step guide to bathing your cat', postCount: 5, followers: 128, updatedAt: '3 days ago', cover: 'cat' },
          { id: 'col-2', name: 'Dog Fashion Inspiration', desc: 'Seasonal trend styling to make your dog a street-style star', postCount: 3, followers: 56, updatedAt: '1 week ago', cover: 'dog' },
          { id: 'col-3', name: 'Pet Healthy Recipes', desc: 'Homemade nutritious meals for a balanced diet for your fur baby', postCount: 7, followers: 203, updatedAt: '5 days ago', cover: 'salad' }
        ],
        official: [
          { id: 'oc-1', name: 'New Pet Owner Guide', desc: 'A pet care handbook from beginner to expert', postCount: 12, cover: 'book-open' },
          { id: 'oc-2', name: 'Grooming Encyclopedia', desc: 'Scientific grooming for healthier pets', postCount: 8, cover: 'droplets' },
          { id: 'oc-3', name: 'Training Classroom', desc: 'Simple and practical training techniques', postCount: 6, cover: 'graduation-cap' }
        ],
        total: 3
      });
    },

    // ===== 营销活动模块 =====

    'GET /api/v1/lottery/info': function() {
      return success({
        remainingSpins: 3,
        prizes: [
          { id: 'p1', name: 'MILO Collaboration Harness', icon: 'gift', type: 'PRODUCT', value: 39900 },
          { id: 'p2', name: '¥50 Coupon', icon: 'ticket', type: 'COUPON', value: 5000 },
          { id: 'p3', name: '100 Points', icon: 'coins', type: 'POINTS', value: 100 },
          { id: 'p4', name: 'Free Shipping Coupon', icon: 'truck', type: 'COUPON', value: 0 },
          { id: 'p5', name: '¥20 Coupon', icon: 'ticket', type: 'COUPON', value: 2000 },
          { id: 'p6', name: 'MOYUYO Custom Scarf', icon: 'gift', type: 'PRODUCT', value: 29900 },
          { id: 'p7', name: '10 Points', icon: 'coins', type: 'POINTS', value: 10 },
          { id: 'p8', name: 'Thanks for Playing', icon: 'circle', type: 'NONE', value: 0 }
        ],
        rules: [
          'Each user can spin 3 times per day',
          'Prizes will be distributed within 7 business days after the event ends',
          'Physical prizes require a shipping address',
          'Coupons valid for 30 days, points credited instantly'
        ]
      });
    },

    'POST /api/v1/lottery/spin': function() {
      var prizes = ['p3', 'p5', 'p8', 'p7', 'p4', 'p8', 'p7', 'p8'];
      var prizeId = prizes[Math.floor(Math.random() * prizes.length)];
      var prizeMap = {
        'p3': { id: 'p3', name: '100 Points', icon: 'coins', type: 'POINTS', value: 100 },
        'p5': { id: 'p5', name: '¥20 Coupon', icon: 'ticket', type: 'COUPON', value: 2000 },
        'p8': { id: 'p8', name: 'Thanks for Playing', icon: 'circle', type: 'NONE', value: 0 },
        'p7': { id: 'p7', name: '10 Points', icon: 'coins', type: 'POINTS', value: 10 },
        'p4': { id: 'p4', name: 'Free Shipping Coupon', icon: 'truck', type: 'COUPON', value: 0 }
      };
      return success({ prize: prizeMap[prizeId], remainingSpins: Math.max(0, Math.floor(Math.random() * 3)) });
    },

    'GET /api/v1/lottery/records': function() {
      return success({
        records: [
          { id: 'lr-1', prizeName: '10 Points', icon: 'coins', type: 'POINTS', time: '2026-07-20 14:30', status: 'RECEIVED' },
          { id: 'lr-2', prizeName: '¥20 Coupon', icon: 'ticket', type: 'COUPON', time: '2026-07-19 10:15', status: 'RECEIVED' },
          { id: 'lr-3', prizeName: 'Thanks for Playing', icon: 'circle', type: 'NONE', time: '2026-07-19 10:14', status: 'NONE' },
          { id: 'lr-4', prizeName: '100 Points', icon: 'coins', type: 'POINTS', time: '2026-07-18 16:00', status: 'RECEIVED' },
          { id: 'lr-5', prizeName: 'Free Shipping Coupon', icon: 'truck', type: 'COUPON', time: '2026-07-17 09:30', status: 'RECEIVED' }
        ]
      });
    },

    'GET /api/v1/flash-sale/list': function() {
      return success({
        sessions: [
          { id: 's1', name: '10:00', label: '10:00 Session', startTime: '10:00', endTime: '14:00', status: 'ACTIVE', timeLeft: 7200 },
          { id: 's2', name: '14:00', label: '14:00 Session', startTime: '14:00', endTime: '18:00', status: 'UPCOMING', timeLeft: null },
          { id: 's3', name: '18:00', label: '18:00 Session', startTime: '18:00', endTime: '22:00', status: 'UPCOMING', timeLeft: null }
        ],
        products: [
          { id: 'fs-1', name: 'Plant-Based Pet Grooming Set', price: 8900, originalPrice: 12800, stock: 45, totalStock: 100, image: 'https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=200&q=80', progress: 55, sessionId: 's1' },
          { id: 'fs-2', name: 'Classic Nylon Leash Set', price: 5900, originalPrice: 8900, stock: 23, totalStock: 80, image: 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=200&q=80', progress: 71, sessionId: 's1' },
          { id: 'fs-3', name: 'Interactive Puzzle Toy Ball', price: 2900, originalPrice: 4500, stock: 12, totalStock: 60, image: 'https://images.unsplash.com/photo-1576201836106-db1758fd1c97?w=200&q=80', progress: 80, sessionId: 's1' },
          { id: 'fs-4', name: 'Pet Treat Gift Box', price: 3500, originalPrice: 5600, stock: 30, totalStock: 50, image: 'https://images.unsplash.com/photo-1589924691195-41432c84c161?w=200&q=80', progress: 40, sessionId: 's2' }
        ]
      });
    },

    'GET /api/v1/flash-sale/[^/?]+$': function(body, url) {
      return success({
        id: 'fs-1',
        name: 'Plant-Based Pet Grooming Set',
        price: 8900, originalPrice: 12800,
        stock: 45, totalStock: 100,
        progress: 55,
        images: ['https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=400&q=80'],
        specs: [
          { id: 'spec-1', name: '500ml / Classic', price: 8900, stock: 15 },
          { id: 'spec-2', name: '1000ml / Family Size', price: 13900, stock: 10 },
          { id: 'spec-3', name: '250ml / Travel Size', price: 4900, stock: 20 }
        ],
        sessionEnd: '14:00:00',
        timeLeft: 7200,
        rules: [
          'Flash sale items limited to 1 per person',
          'Flash sale prices cannot be combined with other discounts',
          'Original price restored when sold out or after event ends',
          'Orders cancelled automatically if not paid within 15 minutes'
        ]
      });
    },

    'GET /api/v1/group-buy/recommend': function() {
      return success({
        products: [
          { id: 'gb-2', name: 'MILO Collaboration Adventure Leash', price: 1999, originalPrice: 2900, progress: { current: 2, total: 3 } },
          { id: 'gb-3', name: 'Smart GPS Pet Tracking Collar', price: 5999, originalPrice: 8900, progress: { current: 1, total: 3 } },
          { id: 'gb-4', name: 'Organic Chicken Recipe Treats', price: 1299, originalPrice: 1800, progress: { current: 3, total: 3 } }
        ]
      });
    },

    'GET /api/v1/group-buy/[^/?]+$': function(body, url) {
      return success({
        id: 'gb-1',
        product: { id: '101', name: 'MILO Collaboration Adventure Harness', price: 3599, originalPrice: 4900, save: 1301, image: '' },
        progress: { current: 2, total: 3, percent: 66.67 },
        members: [
          { name: 'Me', isLeader: true, isMe: true },
          { name: 'J', isLeader: false, isMe: false },
          { name: null, isLeader: false, isMe: false }
        ],
        countdown: { hours: 18, minutes: 32, seconds: 15 },
        rules: ['24-hour countdown', 'Auto-ship when group is complete', 'Auto-refund if group is not formed', 'Limit 1 group per person']
      });
    },

    'GET /api/v1/invite/info': function() {
      return success({
        inviteCode: 'MOYUYO-2026-X7K9',
        reward: 1000,
        stats: { invited: 5, completed: 3, earned: 3000 },
        ranking: { position: 8, total: 156 },
        friends: [
          { name: 'Sam*****on', initial: 'S', status: 'COMPLETED', reward: 1000, time: '2 days ago' },
          { name: 'Jes*****cia', initial: 'J', status: 'REGISTERED', reward: 0, time: '5 days ago' },
          { name: 'Mic*****el', initial: 'M', status: 'COMPLETED', reward: 1000, time: '1 week ago' }
        ],
        rules: [
          'Invite a friend to complete their first order and both earn $10 in points',
          'Points credited instantly, can be used for shopping discounts',
          'Monthly invitation leaderboard TOP 10 receive extra rewards'
        ]
      });
    },

    'GET /api/v1/new-user/info': function() {
      return success({
        daysLeft: 28,
        benefits: [
          { id: 'b1', name: 'First Order 20% off', desc: 'Up to $20 off', icon: 'tag', type: 'COUPON', status: 'AVAILABLE' },
          { id: 'b2', name: 'Free Shipping', desc: 'No minimum spend', icon: 'box', type: 'COUPON', status: 'AVAILABLE' },
          { id: 'b3', name: '$5 Points', desc: 'Credited', icon: 'circle-check', type: 'POINTS', status: 'RECEIVED' }
        ],
        products: [
          { id: 'np-1', name: 'Premium Pet Grooming Set', desc: 'Gentle cleansing, long-lasting fragrance', price: 9500, originalPrice: 18900, image: '../assets/product-shampoo-bottle.jpg' },
          { id: 'np-2', name: 'Comfort Harness', desc: 'Breathable mesh, even pressure distribution', price: 6500, originalPrice: 12900, image: '../assets/product-harness-gear.jpg' },
          { id: 'np-3', name: 'Interactive Puzzle Toy', desc: 'Chew-resistant material, unleash their nature', price: 3500, originalPrice: 6900, image: '../assets/product-toy-play.jpg' },
          { id: 'np-4', name: 'Trendy Pet Jacket', desc: 'Waterproof fabric, versatile for spring and autumn', price: 11000, originalPrice: 21900, image: '../assets/product-pet-jacket.jpg' }
        ],
        tasks: [
          { id: 't1', name: 'Complete Pet Profile', reward: 50, icon: 'pen-line', status: 'PENDING', progress: 0, link: 'pet-profile.html' },
          { id: 't2', name: 'Set Care Reminders', reward: 30, icon: 'circle-alert', status: 'PENDING', progress: 0, link: 'health-calendar.html' },
          { id: 't3', name: 'Complete First Order', reward: 100, icon: 'star', status: 'PENDING', progress: 0, link: 'home.html' }
        ]
      });
    },

    'POST /api/v1/favorites/toggle': function(body) {
      var token = localStorage.getItem('moyuyo_access_token');
      if (!token || !mockDB.tokens[token]) {
        return error(1001, 'Not logged in');
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