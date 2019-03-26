// pages/info_record/info_record.js
const app = getApp();
const domain = app.globalData.domain;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    infoList: [],
    isMore: false
  },

  /**
   * 获取更早的消息
   */
  getMore() {
    var that = this;
    var readInfoList = [];
    wx.request({
      url: domain + '/infoRecord/getReadInfo',
      data: {
        token: app.globalData.token
      },
      success(res) {
        readInfoList = res.data.data;
        var infoList = that.data.infoList;
        if (readInfoList.length > 0) {
          infoList = infoList.concat(readInfoList);
        }
        that.setData({
          infoList: infoList,
          isMore: true
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
 * 访问
 */
  visit(event) {
    var toUid = event.currentTarget.dataset.uid;
    console.log("event ", event);
    wx.navigateTo({
      url: '/pages/home_page/home_page?toUid=' + toUid,
    })
  },

  /**
   * 获取详情
   */
  getDetail(event) {
    var cid = event.currentTarget.dataset.cid;
    var type = event.currentTarget.dataset.type;
    var toUid = event.currentTarget.dataset.uid;
    if(type == 1) {
      wx.navigateTo({
        url: '/pages/home_page/home_page?toUid=' + toUid,
      })
    } else if(type == 2 || type == 3) {
      wx.navigateTo({
        url: '/pages/info_detail/info_detail?cid=' + cid,
      })
    }
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
      isMore: false
    })
    var that = this;
    wx.request({
      url: domain + '/infoRecord/getUnReadInfo',
      data: {
        token: app.globalData.token
      },
      success(res) {
        that.setData({
          infoList: res.data.data
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