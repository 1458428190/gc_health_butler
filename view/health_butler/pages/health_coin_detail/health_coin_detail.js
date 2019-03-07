// pages/health_coin_detail/health_coin_detail.js
var app = getApp();
const domain = app.globalData.domain;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    coinDealList: {},
    select: 0,
    detailList: []
  },

  /**
   * 点击收入明细
   */
  income () {
    var detailList = this.data.coinDealList.incomeDetail;
    this.setData({
      select: 0, 
      detailList: detailList
    })
  },

  /**
   * 点击支出明细
   */
  expense () {
    var detailList = this.data.coinDealList.expendDetail;
    this.setData({
      select: 1,
      detailList: detailList
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
    // 获取健康币详情
    var self = this;
    wx.request({
      url: domain + '/coinDetail/list',
      data: {
        token: app.globalData.token
      },
      success(res) {
        self.setData({
          coinDealList: res.data.data,
          detailList: res.data.data.incomeDetail
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