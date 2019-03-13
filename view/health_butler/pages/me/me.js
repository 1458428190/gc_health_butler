// pages/me/me.js
const app = getApp();
const domain = app.globalData.domain;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    info: {},
    userData: {}
  },


  getOperation() {
    wx.navigateTo({
      url: '/pages/operation/operation',
    })
  },


  /**
   * 跳转到我的主页
   */
  me() {
    wx.navigateTo({
      url: '/pages/home_page/home_page',
      success() {
        console.log('跳转成功');
      }
    })
  },

  getHealthCoin() {
    wx.navigateTo({
      url: '/pages/health_coin_detail/health_coin_detail',
      success() {
        console.log('跳转成功');
      }
    })
  },

  getRank() {
    wx.navigateTo({
      url: '/pages/rank/rank',
      success() {
        console.log('跳转成功');
      }
    })
  },

  nvToConversion() {
    wx.navigateTo({
      url: '/pages/conversion/conversion',
      success() {
        console.log('跳转成功');
      }
    })
  },

  /**
   * 兑换记录
   */
  conversionRecord() {
    wx.navigateTo({
      url: '/pages/conversion_record/conversion_record',
      success() {
        console.log('跳转成功');
      }
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
      userData: app.globalData.userData
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

  }
})