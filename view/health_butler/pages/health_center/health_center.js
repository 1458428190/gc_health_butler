// pages/health_center/health_center.js
var app = getApp();
const domain = app.globalData.domain;
var wxRequest = require('../../utils/wxRequest.js')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userData: {},
    navSelect: {},
    taskList: []
  },

  /**
   * aiTest
   */
  nvAiTest() {
    wx.navigateTo({
      url: '/pages/404/clockSifi/canvas',
    })
  },

  nvToBmi() {
    wx.navigateTo({
      url: '/pages/bmi_cal/bmi_cal',
    })
  },

  /**
   * 选择健康测试
   */
  navToHealthTest() {
    this.setData({
      navSelect: 0
    })
  },

  /**
   * 选择挑战任务
   */
  navToTask() {
    var self = this;
    var taskList = wx.getStorageSync('taskList');
    console.log(taskList);
    if(taskList != '') {
      self.setData({
        taskList: taskList
      })
    } else {
      wx.showLoading({
        title: '加载中',
        mask: true
      })

      setTimeout(function () {
        wx.hideLoading()
      }, 1000)
      // 查询任务列表
      wx.request({
        url: domain + '/record/task',
        success(res) {
          console.log("res " , res);
          wx.setStorageSync("taskList", res.data.data);
          self.setData({
            taskList: res.data.data
          })
        }
      })
    }
    this.setData({
      navSelect: 1
    })
  },

  /**
   * 获取攻略
   */
  getStrategy() {
    // wx.navigateTo({
    //   url: '/pages/strategy/strategy'
    // })
    wx.navigateTo({
      url: '/pages/404/clockSifi/canvas',
    })
  },

  /**
    * 邀请用户
    */
  invite() {
    // 跳转页面
    wx.navigateTo({
      url: '/pages/metrix/canvas',
      success() {
        console.log('跳转成功');
      }
    })
  },

  /**
   * 跳转至我的主页
   */
  getStep() {
    // 跳转页面
    wx.navigateTo({
      url: '/pages/home_page/home_page',
      success() {
        console.log('跳转成功');
      }
    })
  },

  /**
   * 获取健康币
   */
  getHealthCoin() {
    // 跳转页面
    wx.navigateTo({
      url: '/pages/health_coin_detail/health_coin_detail',
      success() {
        console.log('跳转成功');
      }
    })
  },

  /**
   * 打卡
   */
  clockIn() {
    // 跳转页面
    wx.navigateTo({
      url: '/pages/clock_in/clock_in',
      success() {
        console.log('跳转成功');
      }
    })
  },

  /**
   * 兑换
   */
  exchange() {
    // 跳转页面
    wx.navigateTo({
      url: '/pages/conversion/conversion',
      success() {
        console.log('跳转成功');
      }
    })
  },

  /**
   * 查看排行榜
   */
  getRank() {
    // 跳转页面
    wx.navigateTo({
      url: '/pages/rank/rank',
      success() {
        console.log('跳转成功');
      }
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log("health_center ", "onLoad Start");

    var that = this;

    // 查询主页信息并上传信息
    this.waitToken().then(function () {
      that.getInfo();
      // 查看是否授权
      wx.getSetting({
        success: function (res) {
          if (res.authSetting['scope.userInfo']) {
            wx.getUserInfo({
              withCredentials: true,
              lang: 'zh_CN',
              success: function (res) {
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
              }
            });
          }
        }
      })
    })
    console.log("health_center ", "onLoad End");
  },

  waitToken: function () {
    var p = new Promise(function (resolve, reject) {
      var interval = setInterval(function () {
        if (app.globalData.token != undefined && app.globalData.token!="") {
          clearInterval(interval)
          resolve()
        }
        console.log("token is undefined")
      }, 300)
    })
    return p;
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    console.log("health_center ", "onshow start");

    var self = this;
    // 查询主页信息(封面, 提醒, 总步数, 总健康币, 排名, 个人信息)
    self.getInfo();
    // 默认健康测试
    this.setData({
      navSelect: 0
    });

    console.log("health_center ", "onshow end");
  },

  /**
   * 请求
   */
  getInfo() {
    var self = this;
    wx.request({
      url: domain + '/user/info',
      data: {
        token: app.globalData.token,
      },
      success(res) {
        if(res.data)
        self.setData({
          userData: res.data.data
        })
        app.globalData.userData = res.data.data;
      }
    })
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
    if (res.from === 'button') {
      // 来自页面内转发按钮
      console.log(res.target)
    }
    return {
      title: '健康管家, 督促您健康生活',
      path: '/page/health_center/health_center',
      success: function (res) {
        console.log(res.shareTickets[0])
        // console.log
        wx.getShareInfo({
          shareTicket: res.shareTickets[0],
          success: function (res) { console.log(res) },
          fail: function (res) { console.log(res) },
          complete: function (res) { console.log(res) }
        })
      }
    }
  }
})