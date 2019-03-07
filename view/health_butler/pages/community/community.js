// pages/community/community.js
const app = getApp();
const domain = app.globalData.domain;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    allShare: [],

    meShare: [],

    isMe: false,

    user: app.globalData.userInfo,

    userData: app.globalData.userData
  },

  /**
   * 访问
   */
  visit(event) {
    var toUid = event.currentTarget.dataset.uid;
    console.log("event ", event);
    wx.navigateTo({
      url: '/pages/home_page/home_page?toUid='+toUid,
    })
  },

  /**
   * 发表
   */
  share() {
    wx.navigateTo({
      url: '/pages/share/share',
      success() {
        console.log('跳转成功');
      }
    })
  },

  /** 
   * 预览图片
   */
  previewImage: function (e) {  
    var current=e.target.dataset.src;
    var urlList=e.target.dataset.list;
    wx.previewImage({
        current: current, // 当前显示图片的http链接
        urls: urlList // 需要预览的图片http链接列表
    })
  },

  /**
   * 查看自己发表的历史
   */
  visitMe() {
    this.setData({
      share: this.data.meShare,
      isMe: true
    })
  },

  /**
   * 返回
   */
  back() {
    this.setData({
      isMe: false,
      share: this.data.allShare
    })
  },

  /**
   * 取消点赞
   */
  cancel(event) {
    var cid = event.currentTarget.dataset.cid;
    var self = this;
    // 后端
    wx.request({
      url: domain + '/communityRecord/praise',
      data: {
        token: app.globalData.token,
        cid: cid,
        type: 0
      },
      success(res) {
        // 刷新 
        self.refresh();
      }
    })
  },
  /**
   * 点赞
   */
  praise(event) {
    var cid = event.currentTarget.dataset.cid;
    var self = this;
    // 后端
    wx.request({
      url: domain + '/communityRecord/praise',
      data: {
        token: app.globalData.token,
        cid: cid,
        type: 1
      }, 
      success(res) {
        // 刷新 
        self.refresh();
      }
    })
  },

  /**
   * 刷新
   */
  refresh() {
    // 获取健康小圈动态
    var self = this;
    wx.request({
      url: domain + '/community/list',
      data: {
        token: app.globalData.token
      },
      success(res) {
        self.setData({
          allShare: res.data.data
        })
      }
    });

    // 获取自己的健康动态
    wx.request({
      url: domain + '/community/getMe',
      data: {
        token: app.globalData.token
      },
      success(res) {
        self.setData({
          meShare: res.data.data
        })
      }
    });
  },

  /**
   * 删除
   */
  delete(event) {
    var cid = event.currentTarget.dataset.cid;
    var self = this;
    wx.showModal({
      title: '提示',
      content: '确定删除此动态？',
      cancelColor: "#1ECBBE",
      confirmColor: "#1ECBBE",
      success(res) {
        if (res.confirm) {
          console.log('用户点击确定');
          wx.showLoading({
            title: '加载中',
            mask: true
          })
          setTimeout(function () {
            wx.hideLoading()
          }, 1000)
          wx.request({
            url: domain + '/community/delete',
            data: {
              token: app.globalData.token,
              cid: cid
            },
            success(res) {
              wx.showLoading({
                title: '加载中',
                mask: true
              })
              setTimeout(function () {
                wx.hideLoading()
              }, 1000);
              self.refresh();
            }
          })
        } else if (res.cancel) {
          console.log('用户点击取消')
        }
      }
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      userData: app.globalData.userData
    })
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
      isMe: false,
      user: app.globalData.userInfo
    })
    // 刷新 
    this.refresh();

    wx.showLoading({
      title: '加载中',
      mask: true
    })

    setTimeout(function () {
      wx.hideLoading()
    }, 1200)
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