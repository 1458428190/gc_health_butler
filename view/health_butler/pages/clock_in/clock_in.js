const app = getApp();
const domain = app.globalData.domain;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    navSelect: {},
    
    clockRecordList: [],

    count_txt_user: [
      {'count_txt': '蓝色：', 'count_ber': '当天' },
      {'count_txt': '绿色：', 'count_ber': '已打卡' },
      {'count_txt': '无色：', 'count_ber': '未打卡' }
    ]
  },

  /**
   * 改变时间
   */
  bindTimeChange(event) {
    const value = event.detail.value;
    console.log('value' , value);
    var express = 'clockRecordList[' + this.data.navSelect + '].remindTime';
    this.setData({
      [express]: value + '-remind'
    })

    // 改变提醒时间
    var type = this.data.navSelect + 2;
    wx.request({
      url: domain + '/user/set/clockInRemind',
      data: {
        token: app.globalData.token,
        type: type,
        time: value
      }
    })
  },

  // 取消
  bindTimeCancel() {
    // 取消提醒
    var type = this.data.navSelect + 2;
    wx.request({
      url: domain + '/user/set/clockInRemind',
      data: {
        token: app.globalData.token,
        type: type
      }
    })

    var remind = this.data.clockRecordList[this.data.navSelect].remindTime;
    remind = remind.split("-")[0] + "-cancel";
    var express = 'clockRecordList[' + this.data.navSelect + '].remindTime';
    this.setData({
      [express]: remind
    })
  },

  /**
   * 设置提醒
   */
  remind() {
    // 设置为提醒
    var time = this.data.clockRecordList[this.data.navSelect].remindTime;
    if (time == null || time.length < 5) {
      wx.showToast({
        title: '请选择提醒时间',
        icon: 'none'
      })
    } else {
      var remindTime = time + "-remind";
      var express = 'clockRecordList[' + this.data.navSelect + '].remindTime';
      var type = this.data.navSelect + 2;
      wx.request({
        url: domain + '/user/set/clockInRemind',
        data: {
          token: app.globalData.token,
          type: type,
          time: time
        }
      })
      this.setData({
        [express]: remindTime
      })
      }
  },

  /**
   * 取消提醒
   */
  cancelRemind() {
    var time = this.data.clockRecordList[this.data.navSelect].remindTime;
    if (time == null || time.length < 5) {
      wx.showToast({
        title: '请选择提醒时间',
        icon: 'none'
      })
    } else {
      // 取消提醒
      time = time.split('-')[0] + '-cancel';
      var type = this.data.navSelect + 2;
      var express = 'clockRecordList[' + this.data.navSelect + '].remindTime';
      wx.request({
        url: domain + '/user/set/clockInRemind',
        data: {
          token: app.globalData.token,
          type: type
        }
      })
      this.setData({
        [express]: time
      })
    } 
  },

  //是否是打卡时间
  canClock() {
    var date = new Date();
    var hour = date.getHours();
    var resList = [];
    if (hour >= 5 && hour <= 8) {
      resList.push(true);
    } else {
      resList.push(false);
    }

    if (hour >= 19 && hour <= 23) {
      resList.push(true);
    } else {
      resList.push(false);
    }
    return resList;
  },

  /**
   * 打卡
   */
  clockIn() {
    // 判断是否是打卡时间
    var navSelect = this.data.navSelect;
    if(navSelect<=1) {
      var resList = this.canClock();
      if(!resList[navSelect]) {
        wx.showModal({
          title: '提示',
          content: '打卡时间已过或未到',
          showCancel: false,
          confirmColor: "#1ECBBE",
          success(res) {
            if (res.confirm) {
              console.log('用户点击确定')
            } else if (res.cancel) {
              console.log('用户点击取消')
            }
          }
        })
        return;
      }
    }

    var express = 'clockRecordList[' + navSelect + '].hasClock';
    var type = navSelect + 2;
    var self = this;

    // 后台记录打卡
    wx.request({
      url: domain + '/clockIn/clockIn',
      data: {
        token: app.globalData.token,
        type: type
      },
      success(res) {
        self.refreshData();
      }
    })

    wx.showLoading({
      title: '正在打卡',
      mask: true
    })

    setTimeout(function () {
      wx.hideLoading()
    }, 1000)

    this.setData({
      [express]:true
    })

  },

  // 刷新打卡记录
  refreshData() {
    
    var self = this;
    wx.request({
      url: domain + '/clockIn/list',
      data: {
        token: app.globalData.token,
      },
      success(res) {
        self.setData({
          clockRecordList: res.data.data
        })
      }
    })
  },
  /**
   * 早起
   */
  getUpEarly() {
    this.setData({
      navSelect: 0
    })
    wx.showLoading({
      title: '加载中',
      mask: true
    })

    setTimeout(function () {
      wx.hideLoading()
    }, 200)
  },

  /**
   * 晚安
   */
  goodNight() {
    this.setData({
      navSelect: 1
    })
    wx.showLoading({
      title: '加载中',
      mask: true
    })

    setTimeout(function () {
      wx.hideLoading()
    }, 200)
  },

  /**
   * 运动
   */
  movement() {
    this.setData({
      navSelect: 2
    })
    wx.showLoading({
      title: '加载中',
      mask: true
    })

    setTimeout(function () {
      wx.hideLoading()
    }, 200)
  },

  // 获取当前日期 yyyy-MM-dd
  getDate() {
    var date = new Date();
    var year = date.getFullYear()
    var month = date.getMonth() + 1
    var day = date.getDate()
    return year+'-'+month+'-'+day;
  },

  // 获取当前时间 hh:mm 
  getTime() {
    var date = new Date();
    var hour = date.getHours()
    var minute = date.getMinutes()
    return hour + ':' + minute;
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

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

    this.setData({
      navSelect: 0
    })

    // 获取打卡记录
    this.refreshData();

    wx.showLoading({
      title: '加载中',
      mask: true
    })

    setTimeout(function () {
      wx.hideLoading()
    }, 500)
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

  }
})