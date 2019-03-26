// pages/rank/rank.js
const app = getApp();
const domain = app.globalData.domain;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    select: 0,

    allRankData: {},

    championDescription: ["夺得今日步数冠军", "夺得健康首富", "夺得今日早起冠军", "夺得昨日早睡冠军"],

    description: ["今日步数", "总健康币", "起床时间", "睡觉时间"],

    imgIcon: ["../../image/step.png", "../../image/health_coin.png", "../../image/get_up.png", "../../image/sleep.png"]
  },

  /**
   * 访问他人主页
   */
  visit(event) {
    var toUid = event.currentTarget.dataset.uid;
    console.log("event ", event);
    wx.navigateTo({
      url: '/pages/home_page/home_page?toUid=' + toUid,
    })
  },

  /**
   * 步数榜
   */
  stepRank() {
    this.setData({
      select: 0
    })
  },

  /**
   * 健康币榜
   */
  coinRank() {
    this.setData({
      select: 1
    })
  },

  /**
   * 早起榜
   */
  getUpRank() {
    this.setData({
      select: 2
    })
  },

  /**
   * 早睡榜
   */
  sleepRank() {
    this.setData({
      select: 3
    })
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
      select: 0
    })
    // 获取排名信息
    var self = this;
    wx.request({
      url: domain + '/user/rank',
      data: {
        token: app.globalData.token
      },
      success(res) {
        self.setData({
          allRankData: res.data.data
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