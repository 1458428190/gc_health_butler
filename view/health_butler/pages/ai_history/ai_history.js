// pages/ai_history/ai_history.js
const app = getApp();
const domain = app.globalData.domain;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userInfo: app.globalData.userInfo,
    recordList: []
  },

  inquiry(event) {
    console.log("event", event)
    var id = event.currentTarget.dataset.id;
    wx.navigateTo({
      url: '/pages/ai_res/ai_res?id=' + id
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var self = this;
    self.setData({
      userInfo: app.globalData.userInfo
    })
    wx.request({
      url: domain + '/aiSkin/history',
      data: {
        token: app.globalData.token
      },
      success(res) {
        self.setData({
          recordList: res.data.data
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
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

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