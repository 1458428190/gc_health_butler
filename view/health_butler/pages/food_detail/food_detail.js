// pages/foodDetail/foodDetail.js
var app = getApp();
const domain = app.globalData.domain;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    food: {},
    imgUrl: {},
    name: {}
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var fid = options.fid;
    var imgUrl = options.imgurl;
    var name = options.name;
    let self = this;
    wx.request({
      url: domain + '/food/detail', 
      data: {
        fid: fid
      },
      header: {
        'content-type': 'application/json' // 默认值
      },
      success(res) {
        self.setData({
          food: res.data.data,
          imgUrl: imgUrl,
          name: name
        })
      }
    });

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