// pages/cookbook/cookbook.js
var app = getApp();
const domain = app.globalData.domain;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    foods: {},
    inputValue: '',
    isSearch: 1
  },

  /**
   * 获取食物
   */
  getFoodList(event) {
    var fcid = event.currentTarget.dataset.id;
    wx.navigateTo({
      url: '/pages/food_list/food_list?fcid=' + fcid,
      success() {
        console.log('跳转成功');
      }
    })
  },

  /**
   * 输入数据时
   */
  searchInput(event) {
    var cursor = event.detail.cursor;
    if(cursor > 0) {
      this.setData({
        isSearch: 0
      })
    } else {
      this.setData({
        isSearch: 1
      })
    }
  },

  /**
   * 查找
   */
  search(event) {
    var keyword = event.detail.value;
    wx.navigateTo({
      url: '/pages/food_list/food_list?keyword=' + keyword,
    })
  },

  /**
   * 点击取消
   */
  cancel(event) {
    this.setData({
      isSearch: 1,
      inputValue: ''
    })
  },

  /**
   * 获取食物分类
   */
  getFoods() {
    var self = this;
    wx.request({
      url: domain + '/food/category',
      data: {
      },
      header: {
        'content-type': 'application/json' // 默认值
      },
      success(res) {
        self.setData({
          foods: res.data.data
        });
        wx.setStorage({
          key: 'foods',
          data: res.data.data
        })
      }
    })

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      isSearch: 1
    })
    this.getFoods();
    wx.showLoading({
      title: '加载中',
      mask: true
    })

    setTimeout(function () {
      wx.hideLoading()
    }, 500)
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