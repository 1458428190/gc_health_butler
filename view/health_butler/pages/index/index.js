//index.js
//获取应用实例
const app = getApp();
Page({
  data: {
    inputBoxShow: false,

    height: 0,

    height_02: 0,

    commentContent: "",
  },
  bindinput(event) {
    this.setData({
      commentContent: event.detail.value
    })
  },

  /**
   * 用于取消事件冒泡
   */
  noOp() {

  },

  bindfocus: function (e) {
    let that = this;
    let height = 0;
    let height_02 = 0;
    wx.getSystemInfo({
      success: function (res) {
        height_02 = res.windowHeight;
      }
    })
    height = e.detail.height - (app.globalData.height_01 - height_02);
    console.log('app is', app.globalData.height_01);
    that.setData({
      height: height,
    })
    console.log('获得焦点的 e is', e);
  },
  //监听input失去焦点
  bindblur: function (e) {
    this.setData({
      height: 0,
      inputShow: false,
    });
    console.log('失去焦点的 e is', e);
  },

  /**
   * 显示评论框
   */
  visibleComment(event) {
    this.setData({
      inputBoxShow: true
    });
  },

  /**
   * 隐藏评论框
   */
  inVisibleComment() {
    this.setData({
      inputBoxShow: false
    });
  },

  onLoad: function (options) {

  },
})