const app = getApp();
const domain = app.globalData.domain;
Page({
  data: {
    //判断小程序的API，回调，参数，组件等是否在当前版本可用。
    canIUse: wx.canIUse('button.open-type.getUserInfo')
  },
  onLoad: function () {

    // 获取步数
    wx.getWeRunData({
    })

    var that = this;
    // 查看是否授权
    wx.getSetting({
      success: function (res) {
        if (res.authSetting['scope.userInfo']) {
          wx.getUserInfo({
            withCredentials: true,
            lang: 'zh_CN',
            success: function (res) {
              app.globalData.userInfo = res.userInfo;
              // 保存或更新用户信息
              var encryptedData = res.encryptedData;
              var iv = res.iv;
              wx.request({
                url: domain + '/user/save/userInfo',
                data: {
                  token: app.globalData.token,
                  iv: iv,
                  encryptedData: encryptedData
                }
              })
              //用户已经授权过
              wx.switchTab({
                url: '/pages/health_center/health_center'
              })
            }
          });
        }
      }
    })
  },

  bindGetUserInfo: function (e) {
    if (e.detail.userInfo) {
      //用户按了允许授权按钮
      var that = this;
      // 保存或更新用户信息
      var encryptedData = e.detail.encryptedData;
      var iv = e.detail.iv;
      wx.request({
        url: domain + '/user/save/userInfo',
        data: {
          token: app.globalData.token,
          iv: iv,
          encryptedData: encryptedData
        }
      })
      //授权成功后，跳转进入小程序首页
      wx.switchTab({
        url: '/pages/health_center/health_center'
      })
    } else {
      //用户按了拒绝按钮
      wx.showModal({
        title: '警告',
        content: '您点击了拒绝授权，将无法进入小程序，请授权之后再进入!!!',
        showCancel: false,
        confirmText: '返回授权',
        success: function (res) {
          if (res.confirm) {
            console.log('用户点击了“返回授权”')
          }
        }
      })
    }
  }
})