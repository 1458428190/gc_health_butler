// pages/ai_test/ai_test.js
const app = getApp();
const domain = app.globalData.domain;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    imgUrl: ""
  },

  /**
   * 测肤
   */
  measure() {
    wx.navigateTo({
      url: '/pages/ai_res/ai_res?imgUrl=' + this.data.imgUrl
    })
  },

  /**
   * 选择照片测试
   */
  chooseImg() {
    var self = this;
    wx.chooseImage({
      count: 1,
      sizeType: ['original', 'compressed'],
      sourceType: ['album', 'camera'],
      success(res) {
        // tempFilePath可以作为img标签的src属性显示图片
        const tempFilePaths = res.tempFilePaths;
        console.log("temp", tempFilePaths);
        self.setData({
          imgUrl: tempFilePaths[0]
        })
      }
    })
  },

  /**
   * 查询历史
   */
  getHistory() {
    wx.navigateTo({
      url: '/pages/ai_history/ai_history',
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