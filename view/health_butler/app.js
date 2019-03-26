//app.js
App({
  data: {
    code: {}
  },

  onLaunch: function () {
    var that = this;
    wx.getSystemInfo({
      success: function (res) {
        console.log("app windowHeight ", res.windowHeight);
        that.globalData.height_01 = res.windowHeight;
      },
      fail(res) {
        console.log("res", res);
      }
    })

    that.getToken();
    // 查看是否授权
    wx.getSetting({
      success: function (res) {
        if (res.authSetting['scope.userInfo']) {
          wx.getUserInfo({
            withCredentials: true,
            lang: 'zh_CN',
            success: function (res) {
              //用户已经授权过
              that.globalData.userInfo = res.userInfo;
              wx.switchTab({
                url: '/pages/health_center/health_center'
              })
            }
          });
        } else {
          //用户还未授权
          wx.reLaunch({
            url: '/pages/auth/auth'
          })
        }
      }
    })

    // 展示本地存储能力
    var logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    // 定时器， 每2小时更新token
    setInterval(this.getToken, 7200000);
    
    // 每10分钟上传步数
    setInterval(this.uploadRunData, 1200000);
  },

  onLoad: function() {

  },

  onShow: function() {
    // this.getToken();
  },

  getToken: function () {
    var that = this;
    // 调用登录接口
    wx.login({
      success: function (res) {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId 对应的 token(标志用户,用于识别用户)
        var code = res.code;
        console.log("code " + code);
        wx.request({
          url: that.globalData.domain + "/user/getToken?code=" + code,
          success(res) {
            console.log("login ", res.data.data);
            that.globalData.token = res.data.data;
            console.log("token", res.data.data);
            // 上传一次步数
            that.uploadRunData();
          }
        })
      }
    })
  },

  /**
   * 上传步数
   */
  uploadRunData() {
    console.log("uploadRunData", this.globalData.token);
    var self = this;
    wx.getWeRunData({
      success(res) {
        const encryptedData = res.encryptedData;
        const iv = res.iv;
        wx.request({
          url: self.globalData.domain + '/record/getAndUploadRunData',
          data: {
            token: self.globalData.token,
            iv: iv,
            encryptedData: encryptedData
          }
        })
      }
    });
  },

  // /**
  //  * 获取token
  //  */
  // getToken() {
  //   var self = this;
  //   // 登录
  //   wx.login({
  //     success: res => {
  //       // 发送 res.code 到后台换取 openId, sessionKey, unionId 对应的 token(标志用户,用于识别用户)
  //       var code = res.code;
  //       console.log("code " + code);
  //       wx.request({
  //         url: self.globalData.domain + "/user/getToken?code=" + code,
  //         success(res) {
  //           console.log("login ", res.data.data);
  //           self.globalData.token = res.data.data;
  //           // 上传一次步数
  //           self.uploadRunData();
  //         }
  //       })
  //     }
  //   });
  // },

  globalData: {
    userInfo: null,
    domain: 'https://ruanjiangongcheng2.xyz:8086/health_butler',
    token: '',
    userData:  {},
    employId: '',
    height_01: 0
  }
})