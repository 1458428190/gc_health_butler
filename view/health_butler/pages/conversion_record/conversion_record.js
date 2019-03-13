// pages/conversion_record/conversion_record.js
const app = getApp();
const domain = app.globalData.domain;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    conversionRecord:[]
  },

  getDetail(event) {
    var rid = event.currentTarget.dataset.rid;
    console.log("rid ", rid);
    wx.request({
      url: domain + '/record/getDetail',
      data: {
        token: app.globalData.token,
        rid: rid
      }, 
      success(res) {
        wx.showModal({
          title: '兑换详情',
          content: res.data.data,
          showCancel: false,
          confirmText: "已记住",
          confirmColor: "#ffa500"
        })
      }
    })

    wx.showLoading({
      title: '加载中',
      mask: true
    })

    setTimeout(function () {
      wx.hideLoading()
    }, 800)
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

    var self = this;
    wx.request({
      url: domain + '/record/conversion',
      data: {
        token: app.globalData.token
      },
      success(res) {
        self.setData({
          conversionRecord: res.data.data
        })
      }
    })

    wx.showLoading({
      title: '加载中',
      mask: true
    })

    setTimeout(function () {
      wx.hideLoading()
    }, 1000)
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